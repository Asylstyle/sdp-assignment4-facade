package ordering;

import static ordering.TestSupport.*;

public class SubsystemTests {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.addStock("BOOK", 5);
        String reservation = inventory.reserve("BOOK", 2);
        equal(3, inventory.available("BOOK"));
        equal(null, inventory.reserve("BOOK", 4));
        equal(3, inventory.available("BOOK"));
        inventory.release(reservation);
        inventory.release(reservation);
        equal(5, inventory.available("BOOK"));
        String sold = inventory.reserve("BOOK", 1);
        inventory.complete(sold);
        inventory.release(sold);
        equal(4, inventory.available("BOOK"));
        rejects(IllegalArgumentException.class, () -> inventory.reserve("BOOK", 0));
        rejects(IllegalArgumentException.class, () -> inventory.addStock(" ", 1));
        rejects(IllegalArgumentException.class, () -> inventory.reserve("BOOK", -2));
        equal(4, inventory.available("BOOK"));

        PaymentProcessor payments = new PaymentProcessor();
        payments.declineCustomer("declined");
        equal(null, payments.charge("declined", 100));
        String payment = payments.charge("approved", 2500);
        equal(2500, payments.netChargedInCents());
        payments.refund(payment);
        payments.refund(payment);
        equal(0, payments.netChargedInCents());
        rejects(IllegalArgumentException.class, () -> payments.refund("missing"));
        rejects(IllegalArgumentException.class, () -> payments.charge("approved", -1));

        ShippingService shipping = new ShippingService();
        String shipment = shipping.createShipment("customer", "BOOK", 2);
        equal(2, shipping.shipments().get(0).quantity());
        rejects(UnsupportedOperationException.class, () -> shipping.shipments().clear());
        NotificationService notifications = new NotificationService();
        notifications.sendConfirmation("customer", shipment);
        equal(shipment, notifications.confirmations().get(0).shipmentId());
        rejects(UnsupportedOperationException.class, () -> notifications.confirmations().clear());
        System.out.println("PASS: subsystem state, validation, idempotence, immutable snapshots");
    }
}
