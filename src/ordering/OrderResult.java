package ordering;

/** Explicit outcomes keep expected business failures separate from invalid inputs. */
public record OrderResult(Status status, String message, String shipmentId) {
    public enum Status { SUCCESS, UNKNOWN_PRODUCT, OUT_OF_STOCK, PAYMENT_DECLINED, SHIPPING_FAILED }
    public boolean successful() { return status == Status.SUCCESS; }
}
