package ordering;

/** Ordinary clients know only the facade. This example is deliberately incomplete. */
public class Client {
    public static void main(String[] args) {
        System.out.println("LEARNING SCAFFOLD: ordering and rollback are not implemented.");
        OrderFacade orders = new OrderFacade();
        try {
            orders.placeOrder("BOOK-1", 1, "demo-customer");
        } catch (UnsupportedOperationException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
