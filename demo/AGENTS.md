# Repository Guidelines

## Project Structure & Module Organization
This repo hosts multiple Gradle labs side by side at the root (e.g., `java-lab`, `design-pattern`, `event-lab`, `spring-mcp-ch`, `virtual-thread-spring`). Each lab keeps its own `gradlew*`, `build.gradle[.kts]`, and `settings.gradle[.kts]`. Source lives under `src/main/java|kotlin`, mirrored tests in `src/test/...`, with supporting assets in `resources/` or `docs/`. When adding a lab, create a new sibling directory so build artifacts remain in that module’s `build/` folder and never spill into the repo root.

## Build, Test, and Development Commands
Run Gradle tasks inside the target lab:
- `cd java-lab && ./gradlew clean build` — full compile plus tests for the vanilla Java samples.
- `cd spring-mcp-ch && ./gradlew bootRun` — starts the Spring Boot demos with the default dev profile.
- `cd virtual-thread-spring && ./gradlew test --tests "*VirtualThread*"` — executes only the concurrency-focused suites.
Use `./gradlew testClasses` for quick compile checks, and run `./gradlew dependencyUpdates` (when configured) before bumping libraries.

## Coding Style & Naming Conventions
Follow idiomatic JVM conventions: four-space indentation for Java, two spaces for Kotlin DSL files, braces on the same line, camelCase members, PascalCase classes, lowercase packages (e.g., `com.study.batch`). Configuration YAML/JSON belongs in `src/main/resources`, while module notes sit in `docs/`. Stick to the module’s Gradle DSL (Groovy vs Kotlin) and run IntelliJ auto-format before committing.

## Testing Guidelines
Gradle already uses JUnit 5 via `useJUnitPlatform`. Mirror production packages inside `src/test`. Name unit tests `*Test` and integration tests `*IT`, add descriptive `@DisplayName`s, and use given/when/then method names when it clarifies behavior. Always run `./gradlew test` in the edited lab before pushing, with targeted commands for concurrency or batch edge cases.

## Commit & Pull Request Guidelines
History combines bracketed course tags with type prefixes, e.g., `[실전 자바 고급 - 1] test : add scheduler case`. Keep subjects concise and imperative, and mention the touched lab (`virtual-thread-spring: refine executor sample`). Pull requests should describe motivation, list key files, link related issues or docs, and include evidence of successful builds/tests (e.g., `./gradlew test`). Attach screenshots or curl transcripts when demos or APIs change, and request review only after local checks pass.

## Security & Configuration Tips
Store configuration in `src/main/resources` and never commit secrets. Generated outputs stay under `build/`; `git clean -xfd build` is the safe reset lever. When running Spring samples, rely on the provided dev defaults rather than editing shared config files.
