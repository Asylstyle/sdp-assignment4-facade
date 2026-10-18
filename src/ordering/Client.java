package ordering;

/** Ordinary application client depends on the facade and its result only. */
public class Client {
    public static void main(String[] args) {
        OrderFacade orders = new OrderFacade();
        show("Successful order", orders.placeOrder("BOOK-1", 2, "customer-1"));
        show("Insufficient stock", orders.placeOrder("BOOK-1", 10, "customer-1"));
        show("Unknown product", orders.placeOrder("MISSING", 1, "customer-1"));
    }
    private static void show(String scenario, OrderResult result) {
        System.out.println(scenario + ": " + result.status() + " - " + result.message());
        if (result.successful()) System.out.println("Shipment: " + result.shipmentId());
    }
}
