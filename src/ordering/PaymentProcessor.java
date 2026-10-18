package ordering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Deterministic local simulation; never enter real financial credentials. */
public class PaymentProcessor {
    private final Set<String> declinedCustomers = new HashSet<>();
    private final Map<String, Integer> charges = new HashMap<>();
    private final Set<String> refunded = new HashSet<>();
    private int nextId = 1;

    public void declineCustomer(String customerId) {
        declinedCustomers.add(Checks.text(customerId, "customerId"));
    }
    /** Null means configured decline; otherwise returns a recorded payment ID. */
    public String charge(String customerId, int amountInCents) {
        Checks.text(customerId, "customerId");
        Checks.positive(amountInCents, "amountInCents");
        if (declinedCustomers.contains(customerId)) return null;
        String id = "PAY-" + nextId++;
        charges.put(id, amountInCents);
        return id;
    }
    public void refund(String paymentId) {
        Checks.text(paymentId, "paymentId");
        if (!charges.containsKey(paymentId)) throw new IllegalArgumentException("Unknown payment: " + paymentId);
        refunded.add(paymentId);
    }
    public int netChargedInCents() {
        int total = 0;
        for (Map.Entry<String, Integer> charge : charges.entrySet()) {
            if (!refunded.contains(charge.getKey())) total = Math.addExact(total, charge.getValue());
        }
        return total;
    }
}
