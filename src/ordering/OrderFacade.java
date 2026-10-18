package ordering;

import java.util.Map;
import java.util.Objects;
import static ordering.OrderResult.Status.*;

/** Small ordering API; subsystem rules remain in the subsystem classes. */
public class OrderFacade {
    private final InventoryService inventory;
    private final PaymentProcessor payments;
    private final ShippingService shipping;
    private final NotificationService notifications;
    private final Map<String, Integer> pricesInCents;

    public OrderFacade() {
        this(new InventoryService(), new PaymentProcessor(), new ShippingService(),
                new NotificationService(), Map.of("BOOK-1", 2500));
        inventory.addStock("BOOK-1", 5);
    }
    /** Injection allows advanced clients to configure and inspect independent services. */
    public OrderFacade(InventoryService inventory, PaymentProcessor payments,
            ShippingService shipping, NotificationService notifications, Map<String, Integer> pricesInCents) {
        this.inventory = Objects.requireNonNull(inventory);
        this.payments = Objects.requireNonNull(payments);
        this.shipping = Objects.requireNonNull(shipping);
        this.notifications = Objects.requireNonNull(notifications);
        this.pricesInCents = Map.copyOf(pricesInCents);
        this.pricesInCents.forEach((product, price) -> {
            Checks.text(product, "productId");
            Checks.positive(price, "priceInCents");
        });
    }
    public OrderResult placeOrder(String productId, int quantity, String customerId) {
        Checks.text(productId, "productId");
        Checks.positive(quantity, "quantity");
        Checks.text(customerId, "customerId");
        Integer price = pricesInCents.get(productId);
        if (price == null) return new OrderResult(UNKNOWN_PRODUCT, "Unknown product", null);
        int amount = Math.multiplyExact(price, quantity); // Before any state changes.
        String reservationId = inventory.reserve(productId, quantity);
        if (reservationId == null) return new OrderResult(OUT_OF_STOCK, "Insufficient stock", null);
        String paymentId = payments.charge(customerId, amount);
        if (paymentId == null) {
            inventory.release(reservationId);
            return new OrderResult(PAYMENT_DECLINED, "Payment declined; stock restored", null);
        }
        String shipmentId;
        try {
            shipmentId = shipping.createShipment(customerId, productId, quantity);
        } catch (IllegalStateException unavailable) {
            payments.refund(paymentId);
            inventory.release(reservationId);
            return new OrderResult(SHIPPING_FAILED, "Shipping unavailable; payment refunded and stock restored", null);
        }
        inventory.complete(reservationId);
        notifications.sendConfirmation(customerId, shipmentId);
        return new OrderResult(SUCCESS, "Order confirmed", shipmentId);
    }
}
