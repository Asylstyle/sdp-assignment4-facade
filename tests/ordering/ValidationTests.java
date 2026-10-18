package ordering;

import java.util.Map;
import static ordering.TestSupport.*;

public class ValidationTests {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addStock("BOOK", 5);
        PaymentProcessor payments = new PaymentProcessor();
        ShippingService shipping = new ShippingService();
        NotificationService notifications = new NotificationService();
        OrderFacade facade = new OrderFacade(inventory, payments, shipping, notifications,
                Map.of("BOOK", Integer.MAX_VALUE));
        rejects(IllegalArgumentException.class, () -> facade.placeOrder(null, 1, "customer"));
        rejects(IllegalArgumentException.class, () -> facade.placeOrder("BOOK", 0, "customer"));
        rejects(IllegalArgumentException.class, () -> facade.placeOrder("BOOK", -1, "customer"));
        rejects(IllegalArgumentException.class, () -> facade.placeOrder("BOOK", 1, " "));
        rejects(ArithmeticException.class, () -> facade.placeOrder("BOOK", 2, "customer"));
        equal(5, inventory.available("BOOK"));
        equal(0, payments.netChargedInCents());
        equal(0, shipping.shipments().size());
        equal(0, notifications.confirmations().size());
        rejects(IllegalArgumentException.class, () -> new OrderFacade(inventory, payments,
                shipping, notifications, Map.of("BOOK", -1)));
        rejects(IllegalArgumentException.class, () -> shipping.createShipment("customer", "BOOK", 0));
        rejects(IllegalArgumentException.class, () -> notifications.sendConfirmation(null, "SHIP-1"));
        System.out.println("PASS: invalid input and price overflow fail before side effects");
    }
}
