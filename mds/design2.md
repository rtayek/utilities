# Design

Core design principles for this project.

## Principles

- **Simple over clever** — prefer the straightforward solution even if a fancier one exists.
- **Immutability by default** — make things mutable only when there is a clear reason.
- **Small surfaces** — keep public APIs minimal; it is easier to add than to remove.
- **Fail loudly** — validate inputs at boundaries; throw descriptive exceptions early.
- **Separation of concerns** — data structures, business logic, and I/O live in separate layers.

## Structure

```
src/
  model/       — pure data types and domain logic (no I/O)
  service/     — operations on the model
  io/          — all input/output (files, network, console)
  util/        — stateless helpers shared across layers
test/
  model/
  service/
```

## Dependencies

- Dependencies flow inward: `io` → `service` → `model`.
- `model` has **no** external dependencies.
- Prefer the standard library; add third-party libraries only when the benefit is clear.

## Error handling

- Use exceptions (or Result types) for conditions the caller must handle.
- Use assertions for invariants that should never be violated.
- Never swallow exceptions silently.

## Concurrency

- Prefer immutable, shared-nothing designs.
- Document every class that is or is not thread-safe.
- Synchronise at the highest level that makes sense, not inside every method.

## Versioning

- Public APIs are stable once released; breaking changes require a major version bump.
- Internal APIs may change freely.

## To be decided

<!-- Add open design questions here as the project evolves -->
- [ ] Persistence strategy
- [ ] Logging approach
- [ ] Configuration format
