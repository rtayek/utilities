# Utilities Project Handoff

Date: 2026-02-03

## Overview
Plain Java Eclipse project consolidating shared utilities. No Gradle/Maven build. Source roots are `src/` (production) and `tst/` (JUnit tests). Archive material under `archive/` is not part of the build.

## Current Stats
- Java files: 33 in `src/`, 10 in `tst/`
- Production packages:
  - `com.tayek.util`
  - `com.tayek.util.concurrent`
  - `com.tayek.util.core`
  - `com.tayek.util.exec`
  - `com.tayek.util.io`
  - `com.tayek.util.log`
  - `com.tayek.util.net`
  - `com.tayek.util.range`
- Test packages:
  - `com.tayek.util`
  - `com.tayek.util.io`
  - `com.tayek` (MyTestWatcher)

## Recent Work
- Split large `Utilities` into focused helpers:
  - `Texts`, `Stacks`, `Misc` (core)
  - `FileIO`, `PropertiesIO`, `Serialization` (io)
- Removed `Utilities.java` entirely after extracting its functionality.
- Moved `IO` static helpers into dedicated classes:
  - Networking moved to `com.tayek.util.net.Net`
  - Thread helpers moved to `com.tayek.util.concurrent.Threads`
  - System properties helper moved to `com.tayek.util.core.SystemProperties`
- Logging server configuration and socket handler setup live in `com.tayek.util.log.LoggingHandler`.
- Added `PropertiesIO` resource loading helpers (`loadFromResource`, `loadFromClassLoader`, `loadFromUrl`).

## Key Architectural Notes
- Networking, logging, and threading utilities are now split into separate subpackages to avoid cross-package cycles.
- Range/missing tracking lives in `com.tayek.util.range` and uses static helpers from `Range`.
- `P.java` is a small demo/harness for resource property loading; it now delegates to `PropertiesIO`.

## Build/Verification Notes
- `javac -Xlint:all -d bin` on `src` completes without warnings.
- Tests are run in Eclipse (JUnit on classpath). CLI test compilation not set up here.

## Suggested Next Steps (optional)
- Decide whether to keep or remove the demo `P.java` class.
- Consider splitting `Net` into smaller classes (hosts/constants vs sockets/discovery) if desired.
- Optional: scan `archive/` for old references to removed packages/classes.
