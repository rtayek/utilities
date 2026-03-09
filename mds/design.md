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

## Domain-Driven Design (DDD)

Reference: *Domain-Driven Design* (Evans).

- **Ubiquitous Language** — use the same terms in code, tests, and conversation as the domain experts use.
- **Bounded Context** — a explicit boundary within which a model applies; different contexts may use different models for the same concept.
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
| **Clean Architecture** | concentric rings: domain → use cases → interface adapters → frameworks; the Dependency Rule: source code dependencies point inward only |
| **Pipeline** | data flows through a sequence of independent processing stages |
| **Event-Driven** | components communicate via events; decouples producers from consumers |
| **Event Sourcing** | store state as an immutable sequence of events; current state derived by replaying them |
| **Repository** | abstract data access behind a collection-like interface |
| **CQRS** | separate read (query) and write (command) models |
| **Saga** | manage distributed transactions as a sequence of local transactions with compensating actions on failure |
| **Outbox** | write events to a local outbox table in the same transaction as domain changes; relay them to the message broker asynchronously |
| **Strangler Fig** | incrementally replace a legacy system by routing traffic to new components until the old system can be removed |

## Enterprise Application Patterns

Reference: *Patterns of Enterprise Application Architecture* (Fowler).

| Pattern | Summary |
|---------|---------|
| **Domain Model** | object model of the domain with both data and behavior |
| **Transaction Script** | organizes business logic as a single procedure per use case; simple but does not scale to complex domains |
| **Service Layer** | defines the application's boundary; coordinates domain objects and infrastructure |
| **Data Mapper** | transfers data between objects and the database while keeping them independent |
| **Active Record** | object wraps a row in a table; combines data access and domain logic |
| **Unit of Work** | tracks objects changed during a transaction and coordinates writing them out |
| **Identity Map** | ensures each object is loaded only once per request; acts as a cache by identity |
| **Lazy Load** | defer loading related data until it is actually needed |
| **Gateway** | wraps access to an external system or resource behind an object |
| **Registry** | a well-known object that other objects use to find common objects or services |
| **Null Object** | provide a default object with do-nothing behavior to avoid null checks |
| **Special Case** | a subclass that provides special behavior for particular cases (e.g. `MissingCustomer`) |
| **Money** | represent a monetary value as an amount + currency together to avoid precision bugs |
| **Plugin** | link classes at runtime based on configuration rather than compile-time coupling |
| **Separated Interface** | define an interface in a different package from its implementation to break dependencies |
| **Layer Supertype** | a type that acts as the supertype for all types in its layer |

## Enterprise Integration Patterns

Reference: *Enterprise Integration Patterns* (Hohpe & Woolf).

| Pattern | Summary |
|---------|---------|
| **Message Channel** | connects sender and receiver; sender writes to channel, receiver reads from it |
| **Message** | atomic unit of data passed between components via a channel |
| **Pipes and Filters** | chain processing steps (filters) connected by channels (pipes) |
| **Message Router** | routes a message to a channel based on its content or metadata |
| **Message Translator** | converts a message from one format to another between systems |
| **Publish-Subscribe Channel** | broadcast a message to all interested receivers |
| **Dead Letter Channel** | route messages that cannot be delivered or processed to a holding area |
| **Request-Reply** | send a request and wait for a correlated reply on a separate reply channel |
| **Correlation Identifier** | attach an ID to a request so the reply can be matched back to it |
| **Scatter-Gather** | broadcast a request to multiple recipients and aggregate their replies |
| **Aggregator** | collect and combine related messages into a single message |
| **Splitter** | break a single message containing multiple items into individual messages |

## Stability and Resilience Patterns

Reference: *Release It!* (Nygard).

| Pattern | Summary |
|---------|---------|
| **Circuit Breaker** | detect repeated failures and stop calling a failing service until it recovers |
| **Bulkhead** | isolate failures by partitioning resources; prevent one slow consumer from exhausting all threads |
| **Timeout** | never wait forever; set a deadline on every external call |
| **Retry** | retry transient failures with back-off; distinguish transient from permanent errors |
| **Fail Fast** | validate preconditions early and refuse work you cannot complete rather than failing deep and late |
| **Steady State** | keep the system in balance; purge log files, rotate data, prevent accumulation of junk |
| **Let It Crash** | allow a failed component to die cleanly and restart from a known good state |
| **Handshaking** | let a server signal readiness to accept work; do not push more than it can handle |

## Code Smells

Reference: *Refactoring* (Fowler).

| Smell | Description |
|-------|-------------|
| **Long Method** | method does too much; break it up |
| **Large Class** | class knows or does too much; split it |
| **Feature Envy** | method uses another class's data more than its own; move it |
| **Data Clumps** | groups of data that always appear together should become their own object |
| **Primitive Obsession** | using primitives where a small value object would be clearer |
| **Switch Statements** | repeated conditionals on type; replace with polymorphism |
| **Parallel Inheritance Hierarchies** | adding a subclass in one hierarchy forces one in another; merge them |
| **Lazy Class** | a class that doesn't do enough to justify its existence |
| **Speculative Generality** | code written for a future that never came; remove it |
| **Temporary Field** | field only set in some circumstances; clarify or extract |
| **Message Chains** | `a.getB().getC().getD()` — violates Law of Demeter |
| **Middle Man** | a class that only delegates; remove or inline it |
| **Shotgun Surgery** | one change requires edits in many classes; consolidate |
| **Divergent Change** | one class changes for many different reasons; split it (SRP) |
| **Data Class** | class with only fields and getters/setters; give it behavior |
| **Refused Bequest** | subclass ignores inherited methods; reconsider the hierarchy |
| **Comments** | a comment that explains confusing code; refactor the code instead |

## Pattern Literature

Key references for further reading:

| Book | Authors | Focus |
|------|---------|-------|
| *Design Patterns* | Gang of Four | OO patterns (creational, structural, behavioral) |
| *Patterns of Enterprise Application Architecture* | Fowler | Data access, domain logic, web presentation |
| *Enterprise Integration Patterns* | Hohpe & Woolf | Messaging and integration |
| *Domain-Driven Design* | Evans | Modeling complex domains |
| *Release It!* | Nygard | Stability and resilience in production |
| *xUnit Test Patterns* | Meszaros | Test design and structure |
| *Refactoring* | Fowler | Code smells and refactoring techniques |
| *Pattern-Oriented Software Architecture* (POSA) | Buschmann et al. | Architectural and concurrency patterns |
| *Pattern Languages of Program Design* (PLoPD) | various | Community-contributed pattern languages across domains |
| *Clean Architecture* | Martin | Dependency management and architecture boundaries |

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
