package ordering;

import java.util.HashMap;
import java.util.Map;

/** Public stock subsystem; reservation IDs prevent duplicate release. */
public class InventoryService {
    private final Map<String, Integer> stock = new HashMap<>();
    private final Map<String, Reservation> reservations = new HashMap<>();
    private int nextId = 1;
    private record Reservation(String productId, int quantity) { }

    public void addStock(String productId, int quantity) {
        Checks.text(productId, "productId");
        Checks.positive(quantity, "quantity");
        stock.put(productId, Math.addExact(available(productId), quantity));
    }
    public int available(String productId) {
        return stock.getOrDefault(Checks.text(productId, "productId"), 0);
    }
    /** Null means insufficient stock; state is unchanged. */
    public String reserve(String productId, int quantity) {
        Checks.text(productId, "productId");
        Checks.positive(quantity, "quantity");
        int available = available(productId);
        if (available < quantity) return null;
        String id = "RES-" + nextId++;
        stock.put(productId, available - quantity);
        reservations.put(id, new Reservation(productId, quantity));
        return id;
    }
    public void complete(String reservationId) {
        reservations.remove(Checks.text(reservationId, "reservationId"));
    }
    public void release(String reservationId) {
        Checks.text(reservationId, "reservationId");
        Reservation reservation = reservations.get(reservationId);
        if (reservation != null) {
            int restored = Math.addExact(available(reservation.productId()), reservation.quantity());
            stock.put(reservation.productId(), restored);
            reservations.remove(reservationId);
        }
    }
}
