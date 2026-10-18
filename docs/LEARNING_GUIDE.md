# Guided independent work

## First small task

Implement only `InventoryService.reserve`: decide how initial stock is supplied,
reject nonpositive quantities, and leave stock unchanged when a request exceeds availability.
Add an observable way to inspect stock so you can verify the behavior. Explain your choices.

## Continue in small steps

1. Complete inventory reservation and release. Decide how to prevent duplicate release.
2. Implement deterministic local payment approval/decline and observable payment state.
3. Implement local shipment records and confirmation behavior.
4. Draw your planned success and failure sequence before implementing the facade.
5. Decide where price comes from; the client must not invent a trusted charge amount.
6. Implement facade coordination, then independently implement declined-payment rollback.
7. Demonstrate outcomes and inspect state before and after each attempt.
8. Replace demo client inputs with understandable scenarios through the facade only.
9. Draw UML matching your final code, write annotated excerpts, and rehearse the defense.

For each change: predict behavior, implement a small step, run a meaningful check,
record the actual result, and commit with a message describing that change.
Do not claim a passing scenario until you have run it.

## Questions to answer before coordination

- What data belongs to inventory, payments, shipments, and notifications?
- How will the facade communicate failure to its caller?
- Which changes must be undone after a declined payment? What must never happen afterward?
- What if shipping fails after payment? State your chosen scope and limits honestly.
- How could an advanced client use a subsystem without depending on the facade?

## Defense practice

- Why is this a Facade rather than merely a large service class?
- Trace one successful call through your actual methods.
- Show inventory state before and after declined payment.
- Which subsystem rules stay outside the facade, and why?
- Why can an advanced client bypass the facade?
- Explain one annotated clean-code excerpt and an alternative you rejected.
- What limitations remain in your simulation?

Use AI for concepts, small examples, and feedback on work you have written;
keep the implementation reasoning and report your own.
