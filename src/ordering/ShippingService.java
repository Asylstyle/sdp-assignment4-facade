package ordering;

import java.util.ArrayList;
import java.util.List;

/** Records simulated shipments; can model an unavailable delivery service. */
public class ShippingService {
    public record Shipment(String id, String customerId, String productId, int quantity) { }
    private final List<Shipment> shipments = new ArrayList<>();
    private final boolean available;
    public ShippingService() { this(true); }
    public ShippingService(boolean available) { this.available = available; }

    public String createShipment(String customerId, String productId, int quantity) {
        Checks.text(customerId, "customerId");
        Checks.text(productId, "productId");
        Checks.positive(quantity, "quantity");
        if (!available) throw new IllegalStateException("Shipping service unavailable");
        String id = "SHIP-" + (shipments.size() + 1);
        shipments.add(new Shipment(id, customerId, productId, quantity));
        return id;
    }
    public List<Shipment> shipments() { return List.copyOf(shipments); }
}
