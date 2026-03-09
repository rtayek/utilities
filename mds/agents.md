# Agents

Guidelines for AI agents working in any codebase.

## Behaviour

- Read all of the .md files in this folder and its sub-folders.
- Ask before refactoring — understand intent first.
- Make one logical change per turn; keep diffs small and reviewable.
- Never delete code without explaining why.
- If something is unclear, say so rather than guessing.

## What agents may do freely

- Anything except deleting the git repository.

## What agents must ask before doing

- Changing public APIs or interfaces.
- Renaming or moving files.
- Adding new dependencies.
- Making architectural changes.
- Widen the access modifier of internal methods or classes.

## What agents must never do

- Delete files permanently if they are not tracked by git.
- Modify configuration files (`.env`, secrets, CI pipelines) without explicit instruction.

## Output expectations

- All new code must compile and pass existing tests.
- New logic should come with at least one test.
