package ordering;

import java.util.ArrayList;
import java.util.List;

/** Local confirmation outbox, independent of the facade. */
public class NotificationService {
    public record Confirmation(String customerId, String shipmentId) { }
    private final List<Confirmation> confirmations = new ArrayList<>();

    public void sendConfirmation(String customerId, String shipmentId) {
        Checks.text(customerId, "customerId");
        Checks.text(shipmentId, "shipmentId");
        confirmations.add(new Confirmation(customerId, shipmentId));
    }
    public List<Confirmation> confirmations() { return List.copyOf(confirmations); }
}
