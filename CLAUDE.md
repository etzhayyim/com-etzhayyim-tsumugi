# com-etzhayyim-tsumugi repository rules

- This is an independent flat-path west repository.
- EDN is canonical for metadata, contracts, graphs, DNA, and state. Tracked JSON
  is an external wire format and belongs only under `wire/`.
- Keep implementation in `src/tsumugi/`, tests in `test/tsumugi/`, schema in
  `schema/`, and external fixtures/identity descriptors in `wire/`.
- Do not reintroduce Go, TinyGo, tracked wasm binaries, shell launchers, JSON-LD,
  BPMN, or former monorepo paths.
- Preserve map-not-target, person-exclusion, provenance, dry-run publication,
  Murakumo-only inference, and fission-gate invariants.
- Run `bb test` before publishing changes.
