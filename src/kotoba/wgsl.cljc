(ns kotoba.wgsl
  "DEDUP NOTICE (2026-07-09, see CHANGELOG.md): this namespace used to carry its own copy of the
   WGSL-as-data compiler. That copy traced back to a 2026-07-02 'clj-wgsl Phase-4' split-migration
   that was abandoned mid-flight — this repo's main went empty, then got a `restore:` commit that
   brought the implementation back — but nothing ever rebuilt it from source afterward, so it never
   received the real feature/bugfix work that happened in kotoba-lang/webgpu's internal `kami.wgsl`
   (Phase 3 compute+storage support, EDN-ified kami-render shaders, a real-binary WGSL validation
   gate via naga). Diffing the two found the compiler logic itself was already byte-identical (no
   behavioural bug, unlike the sprite-gpu case) — this repo's copy was simply stale documentation
   sitting on top of otherwise-correct code, with zero organic commits of its own since the restore.

   So `kami.wgsl` (kotoba-lang/webgpu) is now canonical, and this namespace is a thin re-export of
   it — same public API, always in sync, no more silent drift risk. If you're vendoring this repo
   standalone, requiring `kotoba.wgsl` still works exactly as before; the implementation just lives
   one hop away now. See kami.wgsl's docstring for the full DSL reference (expressions/statements/
   top-level forms — unchanged)."
  (:require [kami.wgsl :as impl]))

(def expr     "See kami.wgsl/expr. Compile an EDN expression to a WGSL expression string." impl/expr)
(def stmt     "See kami.wgsl/stmt. Compile an EDN statement to a WGSL statement string." impl/stmt)
(def func     "See kami.wgsl/func. Compile a function form to a WGSL function declaration." impl/func)
(def struct*  "See kami.wgsl/struct*. name + fields → a WGSL struct declaration." impl/struct*)
(def binding* "See kami.wgsl/binding*. A @group/@binding resource var declaration." impl/binding*)
(def shader   "See kami.wgsl/shader. Assemble top-level items into one WGSL source string." impl/shader)
