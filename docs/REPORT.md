# Reference report: online ordering with Facade

## Introduction

An online order involves several operations: reserve products, collect payment, arrange
shipping, and notify the customer. Calling all four services from every ordinary client
would repeat the ordering rules. This example puts those rules in `OrderFacade` and exposes
one operation, `placeOrder(productId, quantity, customerId)`.

The four subsystems remain public and independently usable. None refers to the facade.
The facade simplifies their common use without hiding them from advanced clients.
This is a local Java reference project, not a real commerce or payment application.

## Structure and UML

See [class diagram](UML.md) and [sequence diagram](SEQUENCES.md). The diagrams describe
actual methods and dependencies. The ordinary `Client` constructs only `OrderFacade` and
reads `OrderResult`. Tests act as advanced clients to configure and inspect subsystems.

Inventory maintains available quantities and active reservation records. Payment stores
simulated charges and refunded identifiers. Shipping stores immutable shipment records;
notifications stores confirmation records. These are four functional subsystems, rather
than classes that only print method names.

## Coordination and rollback

The facade validates input and calculates the amount from its own price map before changing
state. If inventory cannot reserve the requested quantity, it returns `OUT_OF_STOCK`.
After reservation, the payment service either records a charge or returns null for a configured
decline. A decline releases the reservation and returns `PAYMENT_DECLINED`, without creating
a shipment or confirmation. Reservation IDs make repeated release harmless.

After payment succeeds, unavailable shipping causes a refund and stock release. Otherwise,
the facade completes the reservation, records confirmation, and returns the shipment ID.
Expected business failures have explicit result statuses; malformed input uses exceptions.
This sequential compensation is intentionally limited to the built-in in-memory services.

## Five clean-code principles with annotated excerpts

### 1. Meaningful names

From `PaymentProcessor.charge`:

```java
public String charge(String customerId, int amountInCents) {
```

`amountInCents` states the unit and avoids ambiguity about decimal currency. `customerId`
identifies the payment subject. Integer amounts avoid binary floating-point rounding.
The example omits taxes, currencies, and real payment credentials.

### 2. Single responsibility

From `NotificationService.sendConfirmation`:

```java
confirmations.add(new Confirmation(customerId, shipmentId));
```

This subsystem records confirmations. It neither reserves stock nor decides whether payment
succeeded. The facade owns the cross-service ordering decision. This keeps notification
behavior independently understandable and usable by an advanced client.

### 3. Small focused methods

From `OrderResult.successful`:

```java
public boolean successful() { return status == Status.SUCCESS; }
```

The method expresses one question and prevents clients from duplicating the success rule.
It also gives the demo a readable conditional without exposing subsystem details.

### 4. Avoid duplicated validation

From `InventoryService.reserve`:

```java
Checks.text(productId, "productId");
Checks.positive(quantity, "quantity");
```

The package-private `Checks` helper centralizes common blank-text and positive-number rules.
Both facade and public subsystems validate their boundaries because advanced clients may
bypass the facade. The repeated calls enforce the same rule at different public boundaries;
the rule's implementation is written once.

### 5. Explicit error handling and early returns

From `OrderFacade.placeOrder`:

```java
if (paymentId == null) {
    inventory.release(reservationId);
    return new OrderResult(PAYMENT_DECLINED, "Payment declined; stock restored", null);
}
```

The failure branch places compensation beside the condition requiring it. The early return
makes it impossible for this declined-payment path to reach shipping or confirmation.
The caller receives a structured outcome rather than interpreting printed text.

## Verification

The dependency-free tests use explicit assertions that throw on failure, so they do not
require Java's `-ea` flag. `test.ps1` compiles all source and test files with `--release 17`
and runs three suites. See [actual validation evidence](VALIDATION.md).

The suites check successful charging and shipping, declined-payment stock restoration,
shipping-failure refund, insufficient stock, unknown products, invalid inputs, arithmetic
overflow before side effects, duplicate release/refund, completed reservation behavior,
and immutable observation snapshots. Passing these tests supports the tested behavior;
it is not a claim of exhaustive correctness or a predicted course grade.

## Conclusion

The facade provides one readable entry point for the normal ordering workflow while four
independent subsystems retain their own state and responsibilities. Compensation restores
stock after a declined payment and additionally refunds payment after unavailable shipping.
The example illustrates Facade and clean-code choices with runnable evidence. Persistence,
concurrency, real gateways, and failure recovery across process crashes are outside its scope.
Personal defense requires understanding and tracing the implementation.

## Repository

https://github.com/Asylstyle/sdp-assignment4-facade

Use the repository's real commit history for version identification. No independent-student
authorship or unperformed development activity is asserted by this reference report.
