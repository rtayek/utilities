# Coding Style

Applies to all languages unless a language-specific section says otherwise.

## General

- Clarity over cleverness — code is read far more than it is written.
- Small functions with a single responsibility.
- Meaningful names; no single-letter variables outside loop counters.
- Comments explain *why*, not *what* the code does.
- Delete dead code; don't comment it out.

## Naming

| Thing | Style |
|-------|-------|
| Classes / types | `PascalCase` |
| Methods / functions | `camelCase` |
| Constants | `UPPER_SNAKE_CASE` |
| Files | match the primary class/type they contain |

## Formatting

- Indent with 4 spaces (no tabs).
- Max line length: 100 characters.
- One blank line between methods; two between top-level declarations.
- Opening brace on the same line as the declaration.

## Java (25+)

- Prefer **records** for immutable value types.
- Use **switch expressions** (not switch statements) for exhaustive dispatch.
- Use `var` for local variables when the type is obvious from context.
- Return `Optional<T>` instead of `null` from public methods.
- Keep mutation methods package-private when they are only for internal use.
- No raw types; always parameterise generics.

## C / C++

- `snake_case` for functions and variables.
- `static` on all functions not part of a public API.
- Explicit `void` parameter list in C: `int foo(void)`.
- Inline assembly only where strictly necessary; isolate it in small helpers.
- No global mutable state.

## Assembly (NASM)

- One instruction per line; align operands in columns.
- Comment every non-obvious instruction.
- Label names describe purpose, not mechanism: `.halt`, `.loop`, `.done`.

## Tests

- Test names describe the scenario: `placeStone_reducesEnemyLiberties`.
- One assertion concept per test.
- Tests must be deterministic — no random data without a fixed seed.
- Cover the happy path, boundary conditions, and expected error cases.
