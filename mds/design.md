# Design

## Status

This document is normative. Where it uses **MUST / MUST NOT / SHOULD / MAY**, those words are used in the RFC sense.

If code conflicts with this spec, either:

- fix the code, or
- update this spec and *then* fix the code.

## Goals

Optimize for:

- **Correctness** — verified by tests
- **Readability** — code reads like prose
- **Determinism** — tests and builds behave the same everywhere
- **Low coupling** and **clear boundaries**
- **Long-term maintainability** over short-term cleverness

## Principles

- **Simple over clever** — prefer the straightforward solution even if a fancier one exists.
- **Immutability by default** — make things mutable only when there is a clear reason.
- **Small surfaces** — keep public APIs minimal; it is easier to add than to remove.
- **Fail loudly** — validate inputs at boundaries; throw descriptive exceptions early.
- **Separation of concerns** — data structures, business logic, and I/O live in separate layers.
- **Lossless first** — never drop information at format boundaries.
- **Actions over commands** — parse first, apply later.

## Structure

```
src/
test/
resources/
```

## Dependencies

The project SHOULD prefer:

- small, well-scoped libraries

The project SHOULD avoid:

- heavy frameworks
- deep transitive dependency graphs
- libraries that force architectural coupling

If a dependency is added, it MUST be justified by:

- a clear need, and
- a net reduction in code complexity.

## Error handling

- Use exceptions (or Result types) for conditions the caller must handle.
- Use assertions for invariants that should never be violated.
- Never swallow exceptions silently.

## Concurrency

- Prefer immutable, shared-nothing designs.
- Document every class that is or is not thread-safe.
- Synchronize at the highest level that makes sense, not inside every method.

## Versioning

- Public APIs are stable once released; breaking changes require a major version bump.
- Internal APIs may change freely.

## Refactoring

- Refactors MUST preserve behavior unless intentionally changing behavior (which requires updating tests).
- Process: ensure tests exist → refactor structure/names/packages → remove duplication → run tests → micro-optimizations.
- If tests break during refactor, that is a stop-the-line event.
- Prefer clean breaks: move code to the correct location, rename packages cleanly, remove dead compatibility layers.
- Temporary shims are only allowed if time-boxed and tracked by a test or TODO.

## Object-Oriented Design Principles

- **SRP** (Single Responsibility) — a class has one reason to change.
- **OCP** (Open/Closed) — open for extension, closed for modification.
- **LSP** (Liskov Substitution) — subtypes must be substitutable for their base types.
- **ISP** (Interface Segregation) — prefer narrow, focused interfaces over fat ones.
- **DIP** (Dependency Inversion) — depend on abstractions, not concretions.
- **DRY** (Don't Repeat Yourself) — every piece of knowledge has a single authoritative representation.
- **YAGNI** (You Aren't Gonna Need It) — don't build what isn't needed yet.
- **Law of Demeter** — talk only to immediate collaborators; avoid method-chaining across layers.
- **Tell Don't Ask** — tell objects what to do rather than querying their state and deciding for them.
- **CQS** (Command Query Separation) — a method either changes state or returns a value, never both.
- **Composition over inheritance** — prefer assembling behavior from small collaborators over deep class hierarchies.
- **Boy Scout Rule** — leave the code cleaner than you found it.

## Functional Programming Principles

- **Pure functions** — given the same inputs, always return the same output with no side effects.
- **Immutability** — prefer values that cannot change; transform rather than mutate.
- **Function composition** — build complex behavior by combining small, focused functions.
- **Referential transparency** — an expression can be replaced with its value without changing program behavior.
- **Avoid side effects** — isolate I/O and mutation at the edges; keep the core logic pure.
- **Higher-order functions** — functions that take or return other functions enable reuse without inheritance.

## Domain-Driven Design (DDD) Principles

Reference: *Domain-Driven Design* (Evans).

- **Ubiquitous Language** — use the same terms in code, tests, and conversation as the domain experts use.
- **Bounded Context** — an explicit boundary within which a model applies; different contexts may use different models for the same concept.
- **Entity** — an object defined by identity that persists over time (e.g. a `User` with an ID).
- **Value Object** — an object defined by its attributes, not identity; always immutable (e.g. a `Money` or `Address`).
- **Aggregate** — a cluster of objects treated as a unit for consistency; accessed only through the Aggregate Root.
- **Domain Event** — something that happened in the domain that other parts of the system may care about.
- **Repository** — abstracts persistence; the domain works with in-memory collections, not SQL.
- **Anti-Corruption Layer** — translates between your model and an external system's model to prevent leakage.

## Security Principles

- **Least privilege** — grant only the permissions needed to do the job, nothing more.
- **Defense in depth** — layer multiple independent controls; do not rely on a single safeguard.
- **Fail securely** — on error, default to the safe/closed state, not open.
- **Validate all input** — treat all data from outside the system boundary as untrusted.
- **Don't invent cryptography** — use well-audited libraries; never roll your own crypto.

## Performance

- **Measure before optimizing** — profile first; never guess where the bottleneck is.
- **Premature optimization is the root of all evil** (Knuth) — write clear code first, optimize only when necessary.
- **Amdahl's Law** — the speedup from parallelizing a task is limited by its sequential fraction.
- **Cache thoughtfully** — caching adds correctness risk; only cache when profiling justifies it.

## To be decided

<!-- Add open design questions here as the project evolves -->
- [ ] Persistence strategy
- [ ] Logging approach
- [ ] Configuration format
