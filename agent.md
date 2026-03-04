# agent.md

This file applies to the `util` project.

## Project Scope
- Plain Java Eclipse project for shared utilities.
- No Gradle/Maven build in this repo.
- Source roots:
  - `src/` production code
  - `tst/` JUnit tests
- `archive/` is historical material and is not part of the active build.

## Package Layout
- `com.tayek.util`
- `com.tayek.util.concurrent`
- `com.tayek.util.core`
- `com.tayek.util.exec`
- `com.tayek.util.io`
- `com.tayek.util.log`
- `com.tayek.util.net`
- `com.tayek.util.range`

## Build And Verification
- Compile production sources from project root:
  - `javac -Xlint:all -d bin $(find src -name "*.java")`
- Tests are expected to run in Eclipse with JUnit on classpath.

## Current Architecture Notes
- Legacy large utility classes were split into focused helpers.
- IO/network/thread/system-property concerns are separated by package:
  - Networking: `com.tayek.util.net.Net`
  - Thread helpers: `com.tayek.util.concurrent.Threads`
  - System properties: `com.tayek.util.core.SystemProperties`
- Logging server/socket handler configuration is in `com.tayek.util.log.LoggingHandler`.
- Properties resource loading helpers are in `com.tayek.util.io.PropertiesIO`.

## Change Rules
- Keep classes focused; avoid reintroducing monolithic utility classes.
- Preserve package boundaries (net/log/thread/io/core separation).
- Prefer small, reversible edits.
- Keep behavior stable unless tests or explicit requirements demand changes.
- If API behavior changes, update/add tests in `tst/`.

## Optional Cleanup Candidates
- Decide whether `P.java` remains as demo harness or should be removed.
- Consider splitting `Net` further if it grows.
- Scan `archive/` only when recovering legacy behavior.
