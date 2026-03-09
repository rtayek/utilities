# Project Templates

A collection of prototype `.md` files for use as starting templates in software projects.

## Purpose

Copy these files into a new project and customize them to fit. Each file covers a distinct concern and is designed to be self-contained.

## Files

| File | Purpose |
|------|---------|
| `agents.md` | Rules governing what AI agents may do freely, must ask about, or must never do |
| `persona.md` | AI agent identity, communication style, and tone |
| `coding-style.md` | Naming, formatting, and language-specific style rules |
| `design.md` | Architectural principles, design goals, and engineering guidelines |
| `patterns.md` | Catalog of OO, architectural, enterprise, and test patterns |

## Usage

1. Copy the relevant files into your project's root or a `mds/` subdirectory.
2. Edit each file to reflect your project's specific decisions and constraints.
3. Reference them from your `CLAUDE.md` or `agents.md` so AI agents pick them up automatically.

## Customization tips

- `design.md` — fill in the `## Structure` section and resolve the `## To be decided` items early.
- `coding-style.md` — remove language sections that don't apply.
- `persona.md` — adjust identity and tone to match the working style you want.
- `agents.md` — tighten or loosen the "must ask" list based on how much autonomy you want to grant.
