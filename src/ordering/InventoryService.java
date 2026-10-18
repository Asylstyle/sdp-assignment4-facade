package ordering;

/** Public subsystem, also available directly to advanced clients. */
public class InventoryService {
    public boolean reserve(String productId, int quantity) {
        // TODO: Store stock, validate input, and reserve only when enough is available.
        throw new UnsupportedOperationException("TODO: implement inventory reservation.");
    }

    public void release(String productId, int quantity) {
        // TODO: Restore an actual reservation without inventing extra stock.
        throw new UnsupportedOperationException("TODO: implement inventory release.");
    }
}
