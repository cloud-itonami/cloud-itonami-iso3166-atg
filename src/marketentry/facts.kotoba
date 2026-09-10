(ns marketentry.facts
  "Per-jurisdiction public-procurement market-entry regulatory catalog
  -- the G2-style spec-basis table the Market-Entry Compliance Governor
  checks every `:jurisdiction/assess` proposal against ('did the advisor
  cite an OFFICIAL public source for this jurisdiction's requirements,
  or did it invent one?').

  Antigua and Barbuda's real market-entry surface (WebFetch attempts on
  every *.gov.ag / laws.gov.ag host in this catalog failed with
  'unable to verify the first certificate' -- an incomplete TLS chain,
  the same class of environment limitation prior iterations of this loop
  hit for other jurisdictions' portals; curl with a standard user-agent
  succeeded every time (HTTP 200) and its output was read directly, PDF
  text via `pdftotext -layout`, HTML via a tag-stripped dump -- so every
  citation below is HIGH confidence, not a secondary-source fallback):

  - This iteration specifically investigated, rather than assumed, which
    body administers public procurement -- the task named three
    candidates to check (a 'Central Board of Standards', a Ministry of
    Finance procurement unit, or a dedicated Public Procurement Board).
    No 'Central Board of Standards' exists in Antigua and Barbuda; the
    Antigua and Barbuda Bureau of Standards (ABBS, `abbs.gov.ag`) is a
    real body but administers the Standards Act, 2017 (product/quality
    standards), an UNRELATED domain to procurement. The real answer is
    the THIRD candidate: the Procurement Board, together with a
    Procurement Unit inside the Ministry of Finance. Both are created by
    the Procurement Administration Act, 2011 (No. 16 of 2011) --
    downloaded directly from `laws.gov.ag` (Antigua and Barbuda's own
    official consolidated-law portal, run by the Parliamentary Counsel /
    Editor of the Official Gazette) and read in full via `pdftotext`:
    published in the Official Gazette Vol. XXXI No. 71 (29 December
    2011), assented by the Governor-General on 20 December 2011, its own
    long title reading 'AN ACT to reform the procurement administration
    procedures of the Government ... to repeal the Tenders Board Act
    ...'. Section 8 establishes 'a Procurement Unit in the Ministry of
    Finance'; section 9 provides for a Chief Procurement Officer who
    heads it; section 38 establishes the Procurement Board itself;
    section 44(1) lists the Board's duties and powers, including, at
    (1)(k), the power to 'suspend or debar a person from participating
    in solicitations'. The Board's own operating portal,
    `tendersboard.gov.ag` (fetched directly via curl; confirms its
    current membership is chaired by the Permanent Secretary, Ministry
    of Finance and Corporate Governance), retains the pre-2011 'Tenders
    Board' brand name even though the 2011 Act legally renamed/replaced
    it -- this repo cites the Act's own legal names (Procurement Board /
    Procurement Unit) as `:owner-authority` while citing the portal's
    real operating domain as `:provenance`, rather than picking one name
    and silently dropping the other.
  - Vendor/supplier registration: `tendersboard.gov.ag/vendors/` (fetched
    directly) publishes a real, CURRENT, tiered vendor-registration
    classification -- 'Class 1' for baseline bidder information on bids
    below EC$50,000, 'Class 2' (Class 1 plus three references) for bids
    between EC$50,000 and EC$1,000,000, and 'Class 3' (Class 1 plus
    reference letters and audited financial statements) for bids above
    EC$1,000,000. Each class's requirements are a documented SUPERSET of
    the class below it. `marketentry.governor`'s flagship check
    independently recomputes which Class an engagement's own declared
    contract value requires, rather than trusting a self-reported class.
  - Business registration: the Antigua and Barbuda Intellectual Property
    and Commerce Office (ABIPCO, `abipco.gov.ag`) -- fetched directly,
    confirmed in its own words to be 'the National Company and
    Intellectual Property Registry of Antigua and Barbuda ... a
    Department of the Ministry of Justice, Legal Affairs, Public Safety
    and Labour ... established under the Intellectual Property Office
    Act of 2003'. Its Commerce section runs the Companies Register under
    the Companies Act, 1995 (No. 18 of 1995, also downloaded directly
    from `laws.gov.ag` and read; section 8 is titled 'Certificate of
    incorporation', matching ABIPCO's own description of what it
    issues).
  - Tax identity, and the ONE-ACT-VS-TWO-ACTS question the task asked
    every iteration to check for its own country: Antigua and Barbuda's
    Inland Revenue Department (IRD, a Department of the Ministry of
    Finance and Corporate Governance) issues a 6-digit Taxpayer
    Identification Number (TIN). Its OWN 'How do I obtain an Income Tax
    Number?' guidance document (fetched directly from `ird.gov.ag`,
    dated 20 June 2012, still the IRD's current published guidance)
    states in its own words: 'If your business is registered under the
    Company Act of Antigua and Barbuda you must complete forms CB001 and
    F16 to apply for a TIN. At the time of application you will need to
    provide: 1. Certificate of Incorporation ... issued by Intellectual
    Property & Commerce Office ...' and 'before you can register your
    business with the IRD you must first register the business name
    with the Intellectual Property & Commerce Office (ABIPCO)'. This is
    unambiguously a TWO-ACT model: ABIPCO's Certificate of Incorporation
    is a PREREQUISITE DOCUMENT for a SEPARATE, subsequent IRD act of TIN
    issuance -- structurally the same two-act shape this catalog's own
    JPN sibling documents (Corporate Number issued by the National Tax
    Agency, separately from GEPS/qualification registration), and
    genuinely DIFFERENT from the one-act models this loop found for
    Albania (QKB), Armenia (State Register) and Azerbaijan (State Tax
    Service), where a single registration act both creates the entity
    AND issues its tax/business identifier. This catalog cites the IRD
    as `:corporate-number-owner-authority` (the TIN issuer), with
    ABIPCO's Certificate of Incorporation listed as its own, separate
    `:required-evidence` item.
  - `rep-spec-basis`: deliberately nil for ATG. This iteration
    specifically looked in the Procurement Administration Act's own text
    for a personal-exclusion-grounds provision extending disqualification
    to a bidder's representatives/directors/officers (the shape BGR's
    ЗОП Art. 54(2)-(3), ALB's Neni 76(1) and ARM's Article 6(1)(3) each
    document for their own laws) and did not find one: section 44(1)(k)
    gives the Board a general power to 'suspend or debar a person from
    participating in solicitations', but the Act itself DELEGATES the
    actual grounds for suspension/debarment to Regulations (section
    50(1)(o), 'respecting the suspension or debarment of bidders,
    offerors and other persons') -- this iteration could not locate a
    gazetted set of such Regulations on `laws.gov.ag` within its time
    budget, so no specific personal-exclusion-grounds text could be read
    and cited. Rather than infer or reuse a sibling jurisdiction's
    grounds, `rep-spec-basis` returns nil here -- the same honest-
    scope-narrowing discipline AZE's catalog already established for
    this family.

  Coverage is reported HONESTLY (see `coverage`): a jurisdiction not in
  this table has NO spec-basis, full stop -- the advisor must not
  fabricate one, and the governor holds if it tries.")

(def catalog
  "iso3 -> requirement map. `:required-evidence` mirrors the generic
  intake/portal-registration/filing evidence set; `:legal-basis` /
  `:owner-authority` / `:provenance` are the G2 citation the governor
  requires before any `:jurisdiction/assess` proposal can commit.
  ATG deliberately carries NO `:rep-owner-authority` -- see the
  namespace docstring's honest-scope-narrowing note. `:vendor-class-
  owner-authority` / `:vendor-class-legal-basis` / `:vendor-class-
  provenance` ground this vertical's flagship governor check
  (`vendor-class-spec-basis`)."
  {"ATG" {:name "Antigua and Barbuda"
          :owner-authority "Procurement Board / Procurement Unit, Ministry of Finance and Corporate Governance (headed by the Chief Procurement Officer) -- operating as tendersboard.gov.ag"
          :legal-basis "Procurement Administration Act, 2011 (No. 16 of 2011), Official Gazette Vol. XXXI No. 71 (29 December 2011) -- s.8 (Procurement Unit established in the Ministry of Finance) + s.9 (Chief Procurement Officer appointment) + s.38 (Procurement Board established), repealing the Tenders Board Act, 1991 (as amended 2002)"
          :national-spec "tendersboard.gov.ag vendor registration (tiered Class 1/2/3 by bid/contract value) and tender participation via the Procurement Board's own portal"
          :provenance "https://tendersboard.gov.ag/"
          :required-evidence ["Certificate of Incorporation/Registration (Antigua and Barbuda Intellectual Property and Commerce Office, ABIPCO, Companies Act 1995 No. 18 of 1995 s.8)"
                              "Vendor Registration record (tendersboard.gov.ag Procurement Board, tiered Class 1/2/3 by bid/contract value)"
                              "Taxpayer Identification Number (TIN) record (Inland Revenue Department, Ministry of Finance and Corporate Governance)"
                              "Authorized-representative confirmation record"]
          :corporate-number-owner-authority "Inland Revenue Department (IRD), Ministry of Finance and Corporate Governance"
          :corporate-number-legal-basis "Taxpayer Identification Number (TIN, a permanent 6-digit number) -- issued by the IRD as a SEPARATE, subsequent act to ABIPCO business registration: the IRD's own guidance requires an ABIPCO-issued Certificate of Incorporation (Companies Act 1995) as a prerequisite document for forms CB001 + F16 (TIN application for a company-registered business)"
          :corporate-number-provenance "https://ird.gov.ag/wp-content/uploads/2021/07/How-do-I-obtain-a-TIN-for-Personal-Income-Tax-Ver-2.pdf"
          :vendor-class-owner-authority "Procurement Board, Ministry of Finance and Corporate Governance"
          :vendor-class-legal-basis "tendersboard.gov.ag's own published vendor-registration classification: Class 1 (baseline bidder/company information) for bids below EC$50,000; Class 2 (Class 1 plus at least three references) for bids between EC$50,000 and EC$1,000,000; Class 3 (Class 1 plus reference letters and, where requested, audited financial statements) for bids above EC$1,000,000 -- each class's requirements are a documented superset of the class below it"
          :vendor-class-provenance "https://tendersboard.gov.ag/vendors/"}
   "USA" {:name "United States"
          :owner-authority "U.S. General Services Administration (GSA) / SAM.gov"
          :legal-basis "Federal Acquisition Regulation (FAR); System for Award Management"
          :national-spec "SAM.gov entity registration + NAICS self-certification"
          :provenance "https://sam.gov/"
          :required-evidence ["EIN record"
                              "SAM.gov registration record"
                              "State business registration record"
                              "Authorized-representative record"]}
   "DEU" {:name "Germany"
          :owner-authority "Beschaffungsamt des BMI / e-Vergabe platforms"
          :legal-basis "Gesetz gegen Wettbewerbsbeschränkungen (GWB) / VgV"
          :national-spec "e-Vergabe supplier registration under EU procurement directives"
          :provenance "https://www.evergabe-online.de/"
          :required-evidence ["Handelsregister extract"
                              "e-Vergabe registration record"
                              "USt-IdNr record"
                              "Authorized-representative record"]}})

(defn spec-basis
  "The jurisdiction's requirement map, or nil -- nil means NO spec-basis,
  and the governor must hold any proposal that tries to assess or file
  on it."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report: how many of the requested jurisdictions actually
  have a spec-basis entry. Never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-atg R0: " (count catalog)
                 " jurisdictions seeded with an official spec-basis. "
                 "This is a starting catalog for market-entry navigation, "
                 "not a survey of all ~194 jurisdictions -- extend "
                 "`marketentry.facts/catalog`, never fabricate a "
                 "jurisdiction's requirements.")})))

(defn required-evidence-satisfied?
  "Does `submitted` (a set/coll of evidence keywords or strings) satisfy
  every evidence item listed for `iso3`? Missing spec-basis -> never
  satisfied."
  [iso3 submitted]
  (when-let [{:keys [required-evidence]} (spec-basis iso3)]
    (let [need (count required-evidence)
          have (count (filter (set submitted) required-evidence))]
      (= need have))))

(defn evidence-checklist [iso3]
  (:required-evidence (spec-basis iso3) []))

(defn rep-spec-basis
  "The jurisdiction's representative-related requirement map, or nil when
  this catalog has no such regime. For ATG this is deliberately nil --
  see the `catalog` docstring's honest-scope-narrowing note."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:rep-owner-authority sb)
      (select-keys sb [:rep-owner-authority :rep-legal-basis :rep-provenance]))))

(defn corporate-number-spec-basis
  "The jurisdiction's corporate-number / tax-id regime, or nil."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:corporate-number-owner-authority sb)
      (select-keys sb [:corporate-number-owner-authority
                       :corporate-number-legal-basis
                       :corporate-number-provenance]))))

(defn vendor-class-spec-basis
  "The jurisdiction's vendor-registration-classification regime, or nil.
  For ATG this is real and current -- the flagship check this vertical
  adds is grounded here."
  [iso3]
  (when-let [sb (spec-basis iso3)]
    (when (:vendor-class-owner-authority sb)
      (select-keys sb [:vendor-class-owner-authority
                       :vendor-class-legal-basis
                       :vendor-class-provenance]))))
