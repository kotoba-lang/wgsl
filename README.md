# kotoba-lang/wgsl

Kotoba runtime package for `kotoba.wgsl`.

## Status: thin re-export (2026-07-09)

`src/kotoba/wgsl.cljc` no longer carries its own implementation. It re-exports
[`kami.wgsl`](https://github.com/kotoba-lang/webgpu/blob/main/src/kami/wgsl.cljc)
from `kotoba-lang/webgpu`, which is the copy that receives real feature and bug
fix work (Phase 3 compute+storage support, EDN-ified kami-render shaders, a
real-binary WGSL validation gate via naga). This repo's old standalone copy had
zero organic commits since a 2026-07-02 restore — it just carried stale
documentation on top of otherwise-correct (byte-identical) compiler logic. See
CHANGELOG.md for the full rationale.

Requiring `kotoba.wgsl` still works exactly as before — same public API
(`expr`, `stmt`, `func`, `struct*`, `binding*`, `shader`), same behaviour — it
just delegates to the canonical implementation one hop away instead of
carrying a copy that can drift again.

## Test

```sh
clojure -M:test
```
