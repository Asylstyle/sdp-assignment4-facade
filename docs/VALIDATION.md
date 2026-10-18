# Actual validation evidence

Checked on DESKTOP-6287ARH on 18 October 2026.

## Environment

- Compiler/runtime: Eclipse Temurin JDK 25.0.2 on PATH.
- Java 17 compatibility: `javac --release 17 -Xlint:all` completed without warnings.
- `javap -verbose out/ordering/Client.class` reported major version 61 (Java 17).
- No JDK 17 was found in inspected standard Adoptium/Java installation directories.
  An actual JDK 17 runtime was not tested. No software was installed.
- VS Code 1.142.0 is installed; the project window was opened.

## Executed checks

`./test.ps1` compiled source and tests, then completed all three suites with exit code 0:

```text
PASS: subsystem state, validation, idempotence, immutable snapshots
PASS: order success, decline rollback, shipping refund, stock and unknown-product failures
PASS: invalid input and price overflow fail before side effects
All 3 test suites passed.
```

| Scenario | Observed assertion evidence |
| --- | --- |
| Success: 2 of 5 BOOK units, 2500 cents each | 3 units remain; net charge 5000; one shipment; matching confirmation |
| Declined payment | 5 units remain; net charge 0; no shipment/confirmation; subsequent order for all 5 succeeds |
| Shipping unavailable | 5 units remain; net charge 0 after refund; no shipment/confirmation |
| Insufficient stock / unknown product | All four subsystem states unchanged |
| Null/blank identifiers, nonpositive quantities | Rejected; order state unchanged |
| Price multiplication overflow | ArithmeticException before reservation; state unchanged |
| Duplicate release/refund | No extra stock or money returned |
| Completed reservation | Cannot subsequently restore sold stock by releasing its old ID |
| Observation snapshots | Caller cannot clear shipment or confirmation lists |

`./run.ps1` completed with exit code 0 and printed success, declined payment,
insufficient stock, and unknown product outcomes shown in README.md.

These are deterministic local automated checks, not real gateway integration or exhaustive
fault-injection tests. There is no concurrency, persistence, real network operation, or
crash recovery. Presenter understanding and personal defense cannot be verified by these tests.
