# tsumugi 紡ぎ

Institutional power, influence-history, scale, and declared-banner observatory.
It maps public structures and information flow without targeting people or
issuing adjudicative scores.

This is the standalone west repository `etzhayyim/com-etzhayyim-tsumugi`.
EDN is canonical; tracked JSON is confined to `wire/` as external fixtures and
identity descriptors.

```bash
kbb -M:test
kbb -m tsumugi.methods.analyze
kbb -m tsumugi.methods.analyze-influence
kbb -m tsumugi.methods.analyze-scale
kbb -m tsumugi.methods.coverage-report
```

Code lives in `src/tsumugi/`, tests in `test/tsumugi/`, canonical data in
`data/`, schema in `schema/`, and protocol fixtures in `wire/`.
