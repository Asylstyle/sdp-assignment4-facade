package ordering;

/** Small entry point; implement coordination yourself after completing each subsystem. */
public class OrderFacade {
    private final InventoryService inventory = new InventoryService();
    private final PaymentProcessor payments = new PaymentProcessor();
    private final ShippingService shipping = new ShippingService();
    private final NotificationService notifications = new NotificationService();

    public void placeOrder(String productId, int quantity, String customerId) {
        // TODO: Define input rules and the observable outcome of this operation.
        // TODO: Plan successful ordering and declined-payment rollback before coding.
        // TODO: Coordinate the subsystems; keep their internal rules in their own classes.
        throw new UnsupportedOperationException("TODO: implement OrderFacade.placeOrder independently.");
    }
}
