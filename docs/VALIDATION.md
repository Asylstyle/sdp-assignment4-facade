# Actual scaffold validation

Checked on DESKTOP-6287ARH on 18 October 2026.

- Installed compiler/runtime found on PATH: Eclipse Temurin JDK 25.0.2.
- No JDK 17 found in the inspected standard Adoptium/Java installation directories.
  No software was installed. Running on an actual JDK 17 remains to be verified.
- `javac --release 17 -Xlint:all -d out src/ordering/*.java` completed successfully
  without compiler warnings. This checks Java 17 language/API compatibility.
- `javap -verbose out/ordering/Client.class` reported major version 61 (Java 17).
- `java -cp out ordering.Client` completed on JDK 25 and printed:

```text
LEARNING SCAFFOLD: ordering and rollback are not implemented.
TODO: implement OrderFacade.placeOrder independently.
```

These are compilation and scaffold smoke checks, not business-functionality tests.
Inventory, payments, shipping, notifications, coordination, and rollback remain TODOs.
The student's scenario tests and personal report are not completed.
VS Code 1.142.0 was found; its command accepted opening this project.
