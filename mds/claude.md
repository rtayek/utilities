# CLAUDE.md

Instructions for Claude when working in this project.

## Project

This is a utilities project that has a collection of prototype `.md` files in mds/ for use as starting templates in software projects. Each file covers a different concern (coding style, design principles, agent behavior, etc.) and is meant to be copied and customized per project.

## Build

```bash
# No build step — this project is documentation only.
```

## Test

```bash
# Review each .md file for completeness and consistency.
```

## Run

```bash
# No run step — open the .md files directly in any markdown viewer or editor.
```

## Key files

| File | Purpose |
|------|---------|
| `claude.md` | Instructions and context for Claude when working in this project |
| `agents.md` | Rules governing what AI agents may do freely, must ask about, or must never do |
| `persona.md` | AI agent identity, communication style, and tone |
| `coding-style.md` | Naming, formatting, and language-specific style rules (Java 25, C/C++, NASM) |
| `design.md` | Architectural principles, design goals, and engineering guidelines |
| `patterns.md` | Catalog of OO, architectural, enterprise, and test patterns |
| `README.md` | Project overview and how to use these templates |

## Conventions

- Follow `coding-style.md` for any code examples embedded in the templates.
- Follow `design.md` for all architectural decisions.
- Follow `agents.md` for rules on what requires approval.
- Follow `persona.md` for communication style.

## Off-limits

- `.env` and any secrets files
- Generated files (regenerate them via the build command instead)
