(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a public-procurement market-entry
  filing -- every jurisdiction assigns its own format. This namespace
  does NOT invent one; it builds a jurisdiction-scoped sequence number
  and validates the record's required fields, the same honest,
  non-fabricating discipline `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the
  SAME ground-truth-recompute DISCIPLINE sibling actors use (verify a
  claimed monetary total against the entity's own recorded quantity x
  unit fields), reapplied to a market-entry engagement fee line.

  `required-vendor-class` / `vendor-class-insufficient?` are the SAME
  discipline applied to a genuinely Antigua-and-Barbuda-specific
  mechanism: `tendersboard.gov.ag/vendors/` (the Procurement Board's own
  published vendor-registration guidance, WebFetch-blocked by an
  incomplete TLS chain but curl-verified 2026-07-21) tiers vendor
  registration into three classes by the bid/contract value pursued --
  Class 1 below EC$50,000, Class 2 between EC$50,000 and EC$1,000,000,
  Class 3 above EC$1,000,000 -- and each higher class's documented
  requirements are a SUPERSET of the class below it (Class 2 = Class 1
  plus three references; Class 3 = Class 1 plus reference letters and
  audited financial statements), so a vendor registered at a higher
  class has necessarily also satisfied every lower class's requirements.

  This is a GENUINELY DIFFERENT check SHAPE than every prior iso3166
  sibling this repo mirrors: Bulgaria's ЗОП Art. 54(5) de-minimis is a
  PERCENTAGE-OF-TURNOVER formula, Albania's Neni 76(2)(c) carve-out is a
  FLAT STATUTORY CONSTANT, and Azerbaijan's/Armenia's flagship checks are
  plain BOOLEAN registry-membership reads. Antigua and Barbuda's vendor-
  class tiers are neither a percentage, nor a single flat constant, nor
  a boolean flag -- they are a discrete, THREE-STEP THRESHOLD
  classification over the engagement's own declared contract value, so
  the 'recompute' here derives which of three tiers applies and compares
  it against the engagement's own declared registered tier. This is
  reported honestly as a fourth distinct check shape for the family, not
  treated as a lesser version of any prior shape.

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real procurement portal. It builds the RECORD an
  operator would keep, not the act of submitting a portal registration
  itself (that is `marketentry.operation`'s `:filing/submit`, always
  human-gated -- see README Actuation)."
  (:require [clojure.string :as str]))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(def ^:private money-scale
  "Sub-minor-unit scale used when comparing two money amounts: 1/10000 of
  a unit. Coarser than double representation error by many orders of
  magnitude, finer than any real currency's minor unit (2 decimals for
  most, 3 for KWD/BHD/OMR, 0 for JPY/KRW)."
  10000)

(defn- money=
  "Exact-at-money-precision equality for two amounts.

  `==` on raw doubles is NOT the right comparison for money. With
  whole-unit fees the two agree, but as soon as an amount carries
  cents the sum `base + rate x months` is routinely not the double
  nearest the true total, and a CORRECT claim compares false: measured
  on this exact shape, 40,989 of 327,060 cent-denominated combinations
  (12.5%) were rejected while being right, against 0 of 327,060 in
  whole units.

  Rounding both sides to `money-scale` before comparing removes the
  representation error while preserving every distinction money can
  actually carry."
  [x y]
  (and (number? x) (number? y)
       (= (Math/round (* money-scale (double x)))
          (Math/round (* money-scale (double y))))))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  ;; nil when any field is not a number: an un-recomputable engagement is
  ;; un-verifiable, which is neither `correct` nor a ClassCastException
  ;; thrown out of the caller.
  (when (and (number? base-fee) (number? monthly-rate) (number? monitoring-months))
    (+ (double base-fee)
       (* (double monthly-rate) (double monitoring-months)))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (money= claimed-fee (compute-engagement-fee engagement)))

(def vendor-class-thresholds-ecd
  "tendersboard.gov.ag/vendors/ (Antigua and Barbuda Procurement Board's
  own published vendor-registration classification, curl-verified
  2026-07-21): the upper EC$ bound of Class 1 and of Class 2. Anything
  above the Class-2 upper bound requires Class 3. All amounts are
  Eastern Caribbean Dollars (EC$), the currency the Board's own guidance
  is published in."
  {1 50000.0
   2 1000000.0})

(defn required-vendor-class
  "Which tendersboard.gov.ag vendor-registration Class (1, 2 or 3)
  `contract-value` (EC$) requires, per the Board's own published tiers.
  Missing/nil/zero contract value requires only Class 1 (the baseline)."
  [contract-value]
  (let [v (double (or contract-value 0))]
    (cond
      (<= v (get vendor-class-thresholds-ecd 1)) 1
      (<= v (get vendor-class-thresholds-ecd 2)) 2
      :else 3)))

(defn vendor-class-insufficient?
  "Does `engagement`'s own declared `:vendor-class` fall SHORT of the
  Class its own declared `:contract-value` requires? Classes are
  cumulative/hierarchical (a higher Class's requirements are a documented
  superset of every lower Class's), so this is a single `<` comparison,
  never an exact-match requirement -- a vendor registered at a Class
  higher than required still satisfies the requirement."
  [{:keys [contract-value vendor-class]}]
  (< (long (or vendor-class 0)) (required-vendor-class contract-value)))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a portal registration
  package. Pure function -- does not touch any real procurement
  portal."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper-case jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a portal
  registration (always human-gated upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper-case jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
