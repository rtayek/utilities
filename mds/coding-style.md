# Coding Style

Applies to all languages unless a language-specific section says otherwise.

## Status

This document is normative. Where it uses **MUST / MUST NOT / SHOULD / MAY**, those words are used in the RFC sense.

If code conflicts with this spec, either:

<!-- below is not quite right -->
- fix the code, or
- update this spec and *then* fix the code.

## General

- Clarity over cleverness — code is read far more than it is written.
- Small functions with a single responsibility.
- Meaningful names; no single-letter variables outside loop counters unless they are in common use in mathematics, computer science, physics or engineering.
- Comment out dead code; do not delete it.

## Comments Policy

### Default: no comments

The code is the documentation. Comments MUST be minimal.

A comment is allowed only if it adds information NOT already present in code, such as:

- non-obvious invariants
- tricky algorithm reasoning
- format/protocol explanations
- references to external specs

"Explaining what the code is doing" is not permitted.
If a comment conflicts with the code, fix the comment.

## Naming

| Thing | Style |
|-------|-------|
| Classes / types / interfaces / records | `PascalCase` |
| Methods / functions / locals / parameters | `camelCase` |
| Enum values | `camelCase` (not `ALL_CAPS`) |
| Constants (`static final`) | `camelCase` (not `ALL_CAPS`) |
| Files | match the primary class/type they contain |
| Test classes | name must end with `TestCase` |
| Test methods | describe the scenario and should start with test: `testPlaceStone_reducesEnemyLiberties` |

Names should reflect intent: `test...`, `mapper`, `applier`, `codec`, `adapter`, `plugin`, `renderer`.

## Formatting

- Max line length: 100 characters.
- Opening brace on the same line as the declaration.
- Compress vertical white space as much as possible.
- Compress horizontal white space as much as is reasonable.
- Fit code onto one line if it is reasonable.

## Class layout

Every class MUST follow this ordering:

1. constructors
2. public methods
3. package-private methods
4. private methods
5. fields (at the bottom)

Fields at the top are not allowed. The file should read top-down like an execution narrative.

## Visibility

- Default to **package-private** for classes, methods, and fields.
- Public/protected is a commitment — treat it as a deliberate external API decision.
- Fields SHOULD NOT be public unless they are final.
- Prefer immutable objects.

## Java (25+)

- Take full advantage of Java 25 features.
- Prefer **records** for immutable value types.
- Use **sealed hierarchies** for closed sets of types; avoid inheritance when modeling a tagged union — prefer a sealed interface + records.
- Use **switch expressions** (not switch statements) for exhaustive dispatch.
- Use `var` for local variables when the type is obvious from context.
- Return `Optional<T>` instead of `null` from public methods.
- Keep mutation methods package-private when they are only for internal use.
- No raw types; always parameterize generics.

## C / C++

- `camelCase` for functions and variables.
<!-- - `static` on all functions not part of a public API. --->
- Explicit `void` parameter list in C: `int foo(void)`.
- Inline assembly only where strictly necessary; isolate it in small helpers.
- No global mutable state.

## Assembly (NASM)

- One instruction per line; align operands in columns.
- Comment every non-obvious instruction.
- Label names describe purpose, not mechanism: `.halt`, `.loop`, `.done`.

## Tests

### Tests as Specification

Tests are the functional spec — behavioral documentation MUST live in tests, not in prose comments.

- Use xUnit Test Patterns liberally.
- If behavior changes, tests MUST change.
- One assertion concept per test.
- Tests must be deterministic — no random data without a fixed seed, no wall-clock time, no network, no environment-specific paths, no reliance on test order.
- Cover the happy path, boundary conditions, and expected error cases.
- If tests write files, write into an isolated temp directory and clean up afterward.

## Build

- The build tool is the source of truth for build configuration.
- IDE configuration MUST be derived from the build tool, not hand-maintained.
- The project MUST build and test from the command line in a clean environment.

## Spec violations

If you violate this spec, you MUST do one of:

- Fix the code to comply, or
- Add a short "Exception" note near the code explaining why compliance is worse, and ideally add a test that proves the exception is justified.

Recurring exceptions indicate the spec needs updating.

## Utilities

- Utilities must be dependency-light and general-purpose.
- Project-specific logic must NOT be placed in util packages.
- If a utility depends on model/parser/UI types, it is not a utility.
