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

* small, well-scoped libraries

The project SHOULD avoid:

* heavy frameworks
* deep transitive dependency graphs
* libraries that force architectural coupling

If a dependency is added, it MUST be justified by:

* a clear need, and
* a net reduction in code complexity.


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

## Object-Oriented Design Patterns

Reference: *Design Patterns* (Gang of Four).

### Creational
| Pattern | When to use |
|---------|-------------|
| Factory Method | decouple creation from use; let subclasses decide the concrete type |
| Abstract Factory | create families of related objects without specifying concrete classes |
| Builder | construct complex objects step-by-step; avoid telescoping constructors |
| Singleton | one instance needed globally — prefer dependency injection instead where possible |
| Prototype | clone existing objects when construction is expensive |

### Structural
| Pattern | When to use |
|---------|-------------|
| Adapter | bridge an incompatible interface to an expected one |
| Bridge | separate abstraction from implementation so both can vary |
| Composite | treat individual objects and compositions uniformly (tree structures) |
| Decorator | add behavior to an object without subclassing |
| Facade | provide a simplified interface to a complex subsystem |
| Flyweight | share fine-grained objects to reduce memory when many instances are identical |
| Proxy | control access to an object (lazy init, security, caching, remote) |

### Behavioral
| Pattern | When to use |
|---------|-------------|
| Chain of Responsibility | pass a request along a chain until something handles it |
| Command | encapsulate a request as an object; supports undo, queuing, logging |
| Interpreter | define a grammar and interpret sentences in a language |
| Iterator | sequential access to elements without exposing the underlying structure |
| Mediator | centralize communication between objects to reduce coupling |
| Memento | capture and restore object state without violating encapsulation |
| Observer | notify dependents when an object changes state |
| State | change object behavior when its internal state changes |
| Strategy | define a family of algorithms; make them interchangeable |
| Template Method | define the skeleton of an algorithm; let subclasses fill in steps |
| Visitor | add operations to an object structure without modifying it |

## Architectural Patterns

| Pattern | Summary |
|---------|---------|
| **MVC** (Model-View-Controller) | separate data (model), presentation (view), and input handling (controller) |
| **MVP** (Model-View-Presenter) | view is passive; presenter mediates between view and model |
| **MVVM** (Model-View-ViewModel) | viewmodel exposes observable state; view binds to it |
| **Layered / N-tier** | strict layers (presentation → service → domain → persistence); dependencies flow inward only |
| **Hexagonal / Ports & Adapters** | core domain has no external dependencies; adapters implement ports for I/O |
| **Pipeline** | data flows through a sequence of independent processing stages |
| **Event-Driven** | components communicate via events; decouples producers from consumers |
| **Repository** | abstract data access behind a collection-like interface |
| **CQRS** | separate read (query) and write (command) models |

## Test Patterns (xUnit)

Reference: *xUnit Test Patterns* (Meszaros).

| Pattern | Summary |
|---------|---------|
| **Four-Phase Test** | Arrange → Act → Assert → Teardown |
| **Test Double** | stand-in for a real dependency; includes stubs, mocks, fakes, spies, dummies |
| **Stub** | returns canned responses; used to control indirect inputs |
| **Mock** | verifies interactions (method calls, arguments, counts) |
| **Fake** | working implementation not suitable for production (e.g. in-memory DB) |
| **Fixture Setup** | shared setup extracted to `@BeforeEach`; keep it minimal |
| **Object Mother** | factory that creates fully-formed test objects with sensible defaults |
| **Test Data Builder** | fluent builder for test objects; set only the fields relevant to the test |
| **Parameterized Test** | run the same test logic over multiple input/output pairs |
| **Expected Exception** | assert that a specific exception is thrown under specific conditions |
| **Behavior Verification** | assert on interactions with collaborators, not just return values |
| **State Verification** | assert on the state of the system after the action |

## To be decided

<!-- Add open design questions here as the project evolves -->
- [ ] Persistence strategy
- [ ] Logging approach
- [ ] Configuration format
