# SDP Assignment 4: Facade reference project

A small Java 17 online-ordering example with four functional in-memory subsystems,
a facade, dependency-free automated tests, UML, and a reference report.
No external libraries, Maven, Gradle, database, or real financial services are required.

## Open and run

Open this folder in VS Code (`code .`). From a PowerShell terminal in the project:

```powershell
./run.ps1
./test.ps1
```

If local script execution is restricted, run the equivalent commands directly:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac --release 17 -Xlint:all -d out src/ordering/*.java tests/ordering/*.java
# Continue only if compilation succeeds:
java -cp out ordering.Client
java -cp out ordering.SubsystemTests
java -cp out ordering.OrderFacadeTests
java -cp out ordering.ValidationTests
```

Target: Java 17. Validation here used JDK 25.0.2 with `--release 17`, producing Java 17
bytecode. An actual Java 17 runtime has not been tested; no software was installed.

The default facade has five BOOK-1 units priced at 2500 cents each. The demo shows:

```text
Successful order: SUCCESS - Order confirmed
Shipment: SHIP-1
Declined payment: PAYMENT_DECLINED - Payment declined; stock restored
Insufficient stock: OUT_OF_STOCK - Insufficient stock
Unknown product: UNKNOWN_PRODUCT - Unknown product
```

`declined-customer` is a configured simulation identifier. No real card details are used.

## Design

`Client` uses only `OrderFacade` and `OrderResult`. The facade exposes one ordering method.
It validates input, calculates the price, reserves stock, charges payment, creates shipping,
and records confirmation. Declined payment releases stock without shipping or confirmation.
Unavailable shipping refunds payment and releases stock. Public subsystems are independently
usable and have no facade references. Tests demonstrate advanced direct usage.

- [Reference report with five annotated principles](docs/REPORT.md)
- [Class UML](docs/UML.md) and [sequence UML](docs/SEQUENCES.md)
- [Russian walkthrough and defense questions](docs/DEFENSE_RU.md)
- [Actual validation evidence](docs/VALIDATION.md)

## Requirements evidence

| Requirement | Implementation/evidence |
| --- | --- |
| Java 17 | `--release 17`; class major version 61 |
| At least three functional subsystems | Four: stock reservations, charge/refund records, shipments, confirmations |
| Small facade API and correct sequence | `OrderFacade.placeOrder`; sequence UML and integration tests |
| Declined-payment rollback | Restored stock, zero charge, zero shipments/confirmations checked by tests |
| Advanced subsystem access | Public classes; direct-use subsystem tests; no facade back-references |
| Clean code | Named units, focused responsibilities, shared validation, explicit results; annotated report excerpts |
| Report | Introduction, UML links, five principles, verification, conclusion, repository link |
| Personal defense | Walkthrough/questions supplied; understanding must be demonstrated by the presenter |

The supplied rubric weights are report 10%, working code 40%, and personal defense 50%.
Technical evidence does not predict a grade or guarantee institutional compliance.


