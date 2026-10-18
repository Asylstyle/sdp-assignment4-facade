# SDP Assignment 4 — Facade learning scaffold

**Incomplete educational starting point. Not a finished assignment or submission.**
The assignment allows AI examples and clarification, not replacement of independent work.
Implement the TODOs yourself and write your own report using your actual code and evidence.

Option B: online ordering. No external libraries, Maven, or Gradle are needed.
The six public classes are under `src/ordering`. Ordinary `Client` uses only `OrderFacade`;
advanced clients can use public subsystem classes directly. Subsystems have no facade back-references.
All business operations currently throw `UnsupportedOperationException`; no real order is placed.
The facade's fields show dependencies, but coordination and rollback are deliberately absent.

## Open and run

Open this folder in VS Code: `code .`. A Java extension is optional for editor support;
none is installed by this project. Use a JDK 17 terminal for the assignment's target runtime.
In PowerShell, from this folder:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac --release 17 -d out src/ordering/*.java
if ($LASTEXITCODE -eq 0) { java -cp out ordering.Client }
```

Expected scaffold output:

```text
LEARNING SCAFFOLD: ordering and rollback are not implemented.
TODO: implement OrderFacade.placeOrder independently.
```

Compilation only verifies the scaffold's syntax and Java 17 API/language compatibility.
It does not establish assignment functionality. See `docs/VALIDATION.md` for actual checks.

## Rubric checklist — work still required

- [ ] At least three genuinely functional subsystems, each with observable behavior.
- [ ] Small facade API with correct orchestration and clear outcomes.
- [ ] Declined payment restores reserved inventory and creates no shipment or success confirmation.
- [ ] Demonstrate success, decline, insufficient stock, and invalid input; record real results.
- [ ] Write report: introduction, UML, five annotated clean-code excerpts, conclusion, repository URL.
- [ ] Prepare personal defense and explain each design decision without reading a script.

Rubric: report 10%, working code 40%, personal defense 50%.
Deadline supplied: 18 October 2026, 23:59 UTC+5 (18:59 UTC). Defense: Week 7.
This scaffold does **not** claim to satisfy the functional rubric.

Start with `docs/LEARNING_GUIDE.md`, then fill `docs/REPORT_OUTLINE.md` in your own words.
Keep real incremental commits as you work. The desired 20–23 commits is a whole-project
target, not a reason for empty commits, artificial splitting, or backdated history.
