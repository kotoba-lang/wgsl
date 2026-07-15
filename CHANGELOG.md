# Changelog

## Unreleased

- Remove the erroneous dependency on the WebGPU executor. `kami.wgsl` lives in
  this package and depends only on `kotoba.expr`, avoiding a renderer/shader
  dependency cycle.

## Unreleased — 2026-07-09

### Changed: `kotoba.wgsl` is now a thin re-export of `kami.wgsl` (kotoba-lang/webgpu)

**Why.** This repo and `kotoba-lang/webgpu`'s internal `src/kami/wgsl.cljc` both trace back to the
same abandoned "clj-wgsl Phase-4" split-migration (2026-07-02) that left both repos' `main` empty,
followed by independent `restore:` commits that recovered old content into each repo but never
reconciled the two copies against each other afterward.

Unlike the earlier `kotoba.sprite-gpu` dedup, diffing the two `wgsl` copies (normalizing
`kotoba.*` → `kami.*`) did **not** turn up a behavioural bug — the compiler logic (expression/
statement/function/struct/binding compilation, kebab→snake idents, f32 literal coercion, vecN/matN
typed constructors) is byte-identical between the two. What differed was development history:

- `kotoba-lang/webgpu`'s `kami.wgsl` is where the DSL actually originated (2026-06-24) and where
  every real feature landed afterward: DSL adoption for the live fragment shader, the entire lit
  shader as data (struct/bindings/shadow/vertex/fragment), the shared `kotoba.expr`/`expr.core`
  split (ADR-2607051500), Phase 3 compute+storage support, EDN-ifying `kami-render` shaders, and a
  real-binary WGSL validation gate via `naga` (wgpu's own WGSL front-end) wired into
  `bb gen-glsl`/`bb gen-wgsl`.
- This repo's copy (`src/kotoba/wgsl.cljc`, restore commit `0779e52`) already contained that mature
  implementation as a snapshot at restore time, but has had **zero commits since** beyond CI/lint
  housekeeping (`ci: add clj test workflow`, `ci: fix broken sibling checkout`, `ci: add clj-kondo
  lint gate`) — confirming no independent development ever happened here.

Consumer evidence points the same way: this repo is not orphaned (`kotoba-lang/sky`,
`kotoba-lang/render-shaders`, and `kotoba-lang/shaders` all depended on it as of this writing), but
none of them had ever needed anything this repo's copy didn't already share byte-for-byte with
`kami.wgsl`. Those three real consumers are being repointed to depend on `kotoba-lang/webgpu`
directly (and require `kami.wgsl`) in the same consolidation pass, matching how `scene2d`/`webgl`
were repointed off `kotoba-lang/sprite-gpu` directly to `kotoba-lang/webgpu` in the prior dedup.

**What changed.**
- `deps.edn`: dropped the direct `kotoba-lang/expr` dep, added
  `io.github.kotoba-lang/webgpu {:local/root "../webgpu"}` (pulls in `org-w3-webgpu` + `expr`
  transitively).
- `.github/workflows/ci.yml`: sibling checkout is now `webgpu` (+ its own transitive
  `org-w3-webgpu`/`expr` local/root deps) instead of a direct `expr` checkout.
- `src/kotoba/wgsl.cljc`: replaced the duplicated implementation with a thin re-export of
  `kami.wgsl` — same public API (`expr`, `stmt`, `func`, `struct*`, `binding*`, `shader`), so
  anything still requiring `kotoba.wgsl` keeps working unmodified, but now it can never drift from
  the canonical copy again.
- `test/wgsl_test.clj`: unchanged — it exercises the public API through `kotoba.wgsl`, so it now
  incidentally verifies the re-export delegates correctly too.

This repo itself is **not** being archived or deleted — that's a separate decision, out of scope
here. It remains usable standalone; it just no longer maintains a second copy of the logic.
