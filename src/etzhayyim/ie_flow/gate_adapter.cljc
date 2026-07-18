(ns etzhayyim.ie-flow.gate-adapter
  "ie-flow.gate-adapter — the shared boilerplate every 'gate / observatory' actor reuses to
  embed the information-energy flow (ADR-2606212200). The roster's verdict-gate + observatory
  actors (kafun / ugachi / busshi / …) all share the SAME shape: an assessment produces ROWS,
  each row routes to a VERDICT/ROUTE, and the actor RECTIFIES a scattered-risk VOLUME into a
  realised-order VALUE. Rather than fork that plumbing into each actor, this is ONE helper —
  the shared-lib-not-forks principle, applied to the adapters themselves.

  An actor supplies only its DOMAIN model (a config map); the helper builds the ie-flow events,
  folds them through the SHARED metrics, and records them to the per-actor ledger. PURE except
  for record-flow! (the embed write). Stdlib + ie-flow.{metrics,embed} only.

  config:
    :actor       actor id string (the ledger key)
    :id-prefix   event-id prefix (e.g. \"ugachi-\")
    :source-kind source node kind (e.g. \"project\"/\"commodity\"/\"stand\") → source = \"<kind>:<id>\"
    :rows        the assessed rows (seq of string-keyed maps)
    :id-key      row id key            (default \"id\")
    :route-key   row verdict/route key (default \"verdict\")
    :volume-fn   row → the scattered-risk magnitude (the flow the actor rectifies)
    :value-fn    row → the realised-order magnitude (volume rectified onto the route)
    :cost        per-row assessment cost (default 2.0; cheap — the actor only assesses)
  Every event: {:id :actor :source :target :type :volume :value :cost :risk 0.0 :agent? true}."
  (:require [etzhayyim.ie-flow.metrics :as iem]
            #?(:clj [clojure.java.io :as io])))

(def default-value-scale 100.0)
(def default-assess-cost 2.0)

(defn flow-events
  "Build ie-flow EVENT maps from an actor's config (see ns doc). Pure."
  [{:keys [actor id-prefix source-kind rows id-key route-key volume-fn value-fn cost]
    :or {id-key "id" route-key "verdict" cost default-assess-cost}}]
  (mapv
   (fn [r]
     (let [route (get r route-key)]
       {:id (str id-prefix (get r id-key))
        :actor actor
        :source (str source-kind ":" (get r id-key))
        :target (str "route:" (name route))
        :type (name route)
        :volume (double (volume-fn r))
        :value (double (value-fn r))
        :cost (double cost)
        :risk 0.0
        :agent? true}))
   rows))

(defn flow-state
  "Fold the actor's events through the SHARED ie-flow metrics → the order calculus
  (net-gain / order-index / agent-efficiency / parasitic?). Pure."
  [config]
  (iem/flow-state (flow-events config)))

#?(:clj
   (defn record-flow!
     "Record measured events to this actor's standalone local EDN ledger. Deterministic
     (caller supplies tx-id + as-of), no-server-key, gitignored. Returns
     {:flow-log :events :order-index}."
     ([config] (record-flow! config {}))
     ([config {:keys [tx-id as-of]}]
      (let [evs (flow-events config)
            actor (:actor config)
            path (str "data/persisted/" actor ".ie-flow.kotoba.edn")
            entry {:tx/id (or tx-id (str actor "-ie-flow"))
                   :tx/as-of (or as-of "beat") :tx/events evs}]
        (io/make-parents path)
        (spit path (str (pr-str entry) "\n") :append true)
        {:flow-log path
         :events (count evs)
         :order-index (:order-index (iem/flow-state evs))}))))
