# tsumugi 紡ぎ

Institutional power, influence-history, scale, and declared-banner observatory.
It maps public structures and information flow without targeting people or
issuing adjudicative scores.

This is the standalone west repository `etzhayyim/com-etzhayyim-tsumugi`.
EDN is canonical; tracked JSON is confined to `wire/` as external fixtures and
identity descriptors.

```bash
bb test
bb -m tsumugi.methods.analyze
bb -m tsumugi.methods.analyze-influence
bb -m tsumugi.methods.analyze-scale
bb -m tsumugi.methods.coverage-report
```

Code lives in `src/tsumugi/`, tests in `test/tsumugi/`, canonical data in
`data/`, schema in `schema/`, and protocol fixtures in `wire/`.
