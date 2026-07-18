(require '[babashka.process :refer [shell]])

(def namespaces
  '[tsumugi.murakumo-test tsumugi.tests.test-banner
    tsumugi.methods.test-analyze tsumugi.methods.test-analyze-influence
    tsumugi.methods.test-analyze-scale tsumugi.methods.test-autorun
    tsumugi.methods.test-coverage-report tsumugi.methods.test-coverage-scale
    tsumugi.methods.test-fission-gate tsumugi.methods.test-ie-flow
    tsumugi.methods.test-ingest tsumugi.methods.test-ingest-influence
    tsumugi.methods.test-ingest-scale tsumugi.methods.test-narrate
    tsumugi.methods.test-project-influence-posts tsumugi.methods.test-publish
    tsumugi.methods.test-publish-ipfs tsumugi.methods.test-resolve
    tsumugi.methods.test-topics])

(let [failed
      (reduce
       (fn [acc ns-sym]
         (let [form (str "(require 'clojure.test '" ns-sym ")"
                         "(let [r (clojure.test/run-tests '" ns-sym ")]"
                         "(System/exit (if (zero? (+ (:fail r) (:error r))) 0 1)))")
               result (shell {:continue true} "bb" "-e" form)]
           (if (zero? (:exit result)) acc (conj acc ns-sym))))
       [] namespaces)]
  (when (seq failed)
    (throw (ex-info "tsumugi tests failed" {:namespaces failed}))))
