# URL Status Checker

A concurrent URL health-checker built around Data-Oriented Programming (DOP), functional-style pipelines, and structured concurrency on modern Java.

The design follows one core principle: **data stays as data**. Domain values (`ScanRequest`, `Outcome`, `ScanResult`) are immutable records and sealed types with no behavior attached; all logic lives in small, composable functions that transform one immutable value into the next.

## How it works(WIP)

## Tech

- Java (structured concurrency / `StructuredTaskScope`, virtual threads, sealed interfaces, records, pattern matching)
- `java.net.http.HttpClient` for HTTPS requests, with browser-like headers to avoid trivial bot-detection false positives

## Status

Actively under development, built incrementally phase by phase:

- [x] Phase 1 — Core data types (`ScanRequest`, `Outcome`, `ScanResult`)
- [x] Phase 2 — Execute stage (HTTPS request per URL)
- [x] Phase 3 — Concurrent execution via `StructuredTaskScope` + custom `Joiner`
- [x] Phase 4 — Aggregate stage (stats over `ExecutionResult`)
- [x] Phase 5 — Sinks (JSON, CSV export, UI output)
- [ ] Phase 6 — Input sources (manual list, JSON file, UI)

## Running

```
./gradlew run
```

> Requires `--enable-preview` for structured concurrency — make sure your `build.gradle` compiler/run tasks pass that flag.
