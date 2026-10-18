package ordering;

import java.util.Map;
import static ordering.TestSupport.*;
import static ordering.OrderResult.Status.*;

public class OrderFacadeTests {
    private record Fixture(InventoryService inventory, PaymentProcessor payments,
            ShippingService shipping, NotificationService notifications, OrderFacade facade) { }
    private static Fixture fixture(boolean shippingAvailable) {
        InventoryService inventory = new InventoryService();
        inventory.addStock("BOOK", 5);
        PaymentProcessor payments = new PaymentProcessor();
        ShippingService shipping = new ShippingService(shippingAvailable);
        NotificationService notifications = new NotificationService();
        return new Fixture(inventory, payments, shipping, notifications,
                new OrderFacade(inventory, payments, shipping, notifications, Map.of("BOOK", 2500)));
    }
    private static void noSideEffects(Fixture f) {
        equal(5, f.inventory.available("BOOK"));
        equal(0, f.payments.netChargedInCents());
        equal(0, f.shipping.shipments().size());
        equal(0, f.notifications.confirmations().size());
    }
    public static void main(String[] args) {
        Fixture success = fixture(true);
        OrderResult result = success.facade.placeOrder("BOOK", 2, "customer");
        equal(SUCCESS, result.status());
        equal(true, result.successful());
        equal(3, success.inventory.available("BOOK"));
        equal(5000, success.payments.netChargedInCents());
        equal(1, success.shipping.shipments().size());
        equal("customer", success.shipping.shipments().get(0).customerId());
        equal(result.shipmentId(), success.notifications.confirmations().get(0).shipmentId());

        Fixture declined = fixture(true);
        declined.payments.declineCustomer("customer");
        equal(PAYMENT_DECLINED, declined.facade.placeOrder("BOOK", 2, "customer").status());
        noSideEffects(declined);
        equal(SUCCESS, declined.facade.placeOrder("BOOK", 5, "other").status());
        equal(0, declined.inventory.available("BOOK"));

        Fixture unavailable = fixture(false);
        equal(SHIPPING_FAILED, unavailable.facade.placeOrder("BOOK", 2, "customer").status());
        noSideEffects(unavailable);

        Fixture insufficient = fixture(true);
        equal(OUT_OF_STOCK, insufficient.facade.placeOrder("BOOK", 6, "customer").status());
        noSideEffects(insufficient);
        equal(UNKNOWN_PRODUCT, insufficient.facade.placeOrder("MISSING", 1, "customer").status());
        noSideEffects(insufficient);
        System.out.println("PASS: order success, decline rollback, shipping refund, stock and unknown-product failures");
    }
}
