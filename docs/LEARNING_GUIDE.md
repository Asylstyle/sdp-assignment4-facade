# Teaching and extension guide

Run the demonstration, then trace each outcome against the sequence diagram.
Before changing code, predict the state of all four subsystems. Compare your prediction
with the assertions in `tests/ordering`.

Suggested exercises:

1. Add another product and explain how price and stock differ.
2. Add a test for two successful orders and check unique shipment identifiers.
3. Define a result for a cancelled shipment before changing implementation.
4. Discuss the recovery problem if the process crashes after payment.
5. Explain why simply marking placeOrder synchronized would not protect direct subsystem calls.

Use the five annotated excerpts in REPORT.md as discussion prompts. Each participant
should identify a tradeoff and an alternative rather than memorize a description.

The original report outline is retained as a worksheet; the completed reference is REPORT.md.
History contains real scaffold and implementation commits. Earlier incomplete scaffold
notes describe earlier versions and are not evidence that the current implementation is unfinished.
