from chatgtp
---

# Project Standards Spec

## Status

This document is normative. Where it uses **MUST / MUST NOT / SHOULD / MAY**, those words are used in the RFC sense.

If code conflicts with this spec, either:

* fix the code, or
* update this spec and *then* fix the code.

---

# 1. Goals

The project MUST optimize for:

* **Correctness** (verified by tests)
* **Readability** (code reads like prose)
* **Determinism** (tests and builds behave the same everywhere)
* **Low coupling** and **clear boundaries**
* **Long-term maintainability** over short-term cleverness

---

# 2. Architecture

## 2.1 Layering

The codebase MUST be structured so that dependencies flow inward:

* **Domain/model** MUST NOT depend on UI or CLI code.
* Parsing/serialization layers MUST NOT leak UI concerns.
* UI layers MAY depend on model/parsers, but not vice-versa.

If a concept needs to cross a boundary, it MUST cross via:

* DTOs/snapshots, or
* carefully defined interfaces.

## 2.2 “Clean break” refactors

When improving architecture, the preferred tactic is a clean break:

* move code to its correct location
* rename packages cleanly
* remove dead compatibility layers unless there is an explicit, tested reason to keep them

“Temporary” shims are only allowed if they are:

* time-boxed, and
* tracked by a test and/or TODO that makes removal explicit.

---

# 3. Naming Rules

These are not suggestions.

## 3.1 Classes, interfaces, records

MUST use **UpperCamelCase**.

## 3.2 Methods, locals, parameters

MUST use **lowerCamelCase**.

## 3.3 Enums

### Enum type names

MUST use **UpperCamelCase**.

### Enum values

MUST use **lowerCamelCase** (not `ALL_CAPS`, not `PascalCase`).

Example:

```java
enum MoveType { pass, placement, resignation }
```

## 3.4 Constants (`static final`)

MUST use **camelCase** (not ALL_CAPS).

Example:

```java
static final int maxBoardSize = 19;
```

Rationale: constants are still variables in code; screaming them doesn’t add clarity.

---

# 4. Visibility and API Surface

## 4.1 Default visibility

Classes, methods, and fields MUST default to **package-private** unless one of the following is true:

* they are part of a deliberate external API
* they are required by a framework/tool (rare; justify it)
* they exist to support tests in another package (prefer moving tests)

Public/protected MUST be treated as a commitment.

## 4.2 Encapsulation

* Fields SHOULD NOT be public.
* State exposure MUST be intentional and minimal.
* Mutability MUST be constrained: prefer immutable objects and defensive copying at boundaries.

---

# 5. Class Layout Standard

Every class MUST follow this ordering:

1. constructors
2. public methods
3. package-private methods
4. private methods
5. fields (at the bottom)

Fields at the top are not allowed in this project. If you want to see state first, use your IDE outline. The file should read top-down like an execution narrative.

---

# 6. Comments Policy

## 6.1 Default: no comments

Comments MUST be minimal. Code is the documentation.

A comment is allowed only if it adds information NOT already present in code, such as:

* non-obvious invariants
* tricky algorithm reasoning
* format/protocol explanations
* references to external specs

“Explaining what the code is doing” is not permitted.

## 6.2 Tests as documentation

Behavioral documentation MUST live in tests, not in prose comments.

---

# 7. Testing Spec

## 7.1 Tests are the spec

Tests MUST define behavior. If behavior changes, tests MUST change.

## 7.2 Determinism

Tests MUST be deterministic:

* no dependence on wall-clock time (unless controlled)
* no reliance on network
* no reliance on environment-specific paths
* no reliance on test order

## 7.3 Scope

Tests SHOULD verify externally visible behavior and contracts, not implementation details.

## 7.4 Filesystem usage

If tests write files, they MUST:

* write into an isolated temp directory
* clean up (or use temp facilities that auto-clean)
* avoid writing into the repo tree unless explicitly required

---

# 8. Utilities Policy

Reusable utilities MUST live under:

* `tayek.util.*`

Rules:

* utilities MUST be dependency-light
* utilities MUST be general-purpose
* project-specific logic MUST NOT be placed in util packages

If a “utility” depends on model/parser/UI types, it is not a utility.

---

# 9. Dependencies

The project SHOULD prefer:

* the JDK
* small, well-scoped libraries

The project SHOULD avoid:

* heavy frameworks
* deep transitive dependency graphs
* libraries that force architectural coupling

If a dependency is added, it MUST be justified by:

* a clear need, and
* a net reduction in code complexity.

---

# 10. Build and Tooling Requirements

## 10.1 Gradle is canonical

Gradle MUST be the source of truth for build configuration.

IDE configuration MUST be derived from Gradle, not hand-maintained.

## 10.2 Command line build

The project MUST build and test from the command line in a clean environment.

---

# 11. Refactoring Rules

Refactors MUST preserve behavior unless intentionally changing behavior (which requires updating tests).

Refactor process MUST follow:

1. ensure tests exist for current behavior
2. refactor structure/names/packages
3. remove duplication
4. run tests continuously
5. only then consider micro-optimizations

If tests break during refactor, that is a stop-the-line event.

---

# 12. Exceptions

If you violate this spec, you MUST do one of:

* fix the code to comply, or
* add a short “Exception” note near the code explaining why compliance is worse, and ideally add a test that proves the exception is justified.

Exceptions MUST be rare; recurring exceptions indicate the spec needs updating.

---

If you want the “even stricter” version, the next step is to add **enforcement hooks**:

* a Checkstyle/Spotless rule set that enforces enum/constant naming and member order
* a JUnit guard test that fails if class fields appear before methods (yes, it can be done)
* Gradle tasks wired into `check`

Tell me whether you want enforcement via **Spotless**, **Checkstyle**, or “whatever is least annoying in Eclipse,” and I’ll give you a concrete setup.
