# Repository Guidelines

## Project Structure & Module Organization
This repo hosts several lab-sized Gradle projects—`java-lab`, `design-pattern`, `event-lab`, `spring-mcp-ch`, `virtual-thread-spring`, and others—living side by side at the root. Each lab keeps its own `gradlew*`, `build.gradle[.kts]`, and `settings.gradle[.kts]`. Source resides in `src/main/java|kotlin`, mirrored tests in `src/test/...`, with module assets under `resources/` or `docs/`. Create new experiments as sibling directories so build outputs remain inside each lab’s `build/` folder and do not leak to the root.

## Build, Test, and Development Commands
Run Gradle tasks from inside the module:
- `cd java-lab && ./gradlew clean build` — compiles vanilla Java samples and executes their tests.
- `cd spring-mcp-ch && ./gradlew bootRun` — launches the Spring Boot demos with dev-friendly defaults.
- `cd virtual-thread-spring && ./gradlew test --tests "*VirtualThread*"` — runs targeted concurrency cases.
Use `./gradlew testClasses` for quick compile checks and `./gradlew dependencyUpdates` (where defined) before raising library versions. Generated artifacts stay under `build/`; `git clean -xfd build` is safe when a reset is needed.

## Coding Style & Naming Conventions
Stick to idiomatic JVM style: four-space indentation for Java, two spaces for Kotlin DSL files, braces on the same line, camelCase members, PascalCase classes. Packages should remain lowercase (`com.study.batch`). Keep configuration YAML/JSON in `src/main/resources` and keep module-level notes inside each `docs/`. Gradle files must match the module’s DSL (Groovy vs Kotlin) and should not be intermixed. Run IntelliJ auto-formatting before committing to keep diffs tidy.

## Testing Guidelines
Gradle already calls `useJUnitPlatform`, so write JUnit 5 tests that mirror the production package path. Name files `*Test` for unit cases and `*IT` for integration. Favor descriptive `@DisplayName` strings and given/when/then method names. Always run `./gradlew test` before pushing, and capture concurrency or batch edge cases with regression tests so experiments remain reproducible.

## Commit & Pull Request Guidelines
Recent history shows bracketed course tags (`[실전 자바 고급 - 1] …`) alongside `<type> : message` prefixes (`chore : …`, `test : …`). Continue using those forms so reviewers can scan by topic. Write concise, imperative subjects and mention the lab touched (`virtual-thread-spring: refine executor sample`). Pull requests should state the motivation, list key files, link related issues or notes, and include test evidence (e.g., `./gradlew test`). Attach screenshots or curl transcripts when APIs change, and request review only after local checks pass.
