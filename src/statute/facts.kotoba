(ns statute.facts
  "General-law compliance catalog for Antigua and Barbuda (ATG) --
  extends this repo's existing `marketentry.facts` (public-procurement
  market-entry only, narrow scope) with a second, orthogonal catalog of
  statutes a company operating in this jurisdiction must generally track
  for compliance. Mirrors cloud-itonami-iso3166-jpn/-deu/-bgr/-aze/-alb/
  -arm's `statute.facts` (ADR-2607141700, cloud-itonami-compliance-
  fact-federation).

  Every entry cites an OFFICIAL Antigua and Barbuda government-hosted
  URL -- never fabricated. Antigua and Barbuda's official consolidated-
  law portal is `laws.gov.ag` (run by the Parliamentary Counsel / Editor
  of the Official Gazette). Unlike several other small-jurisdiction law
  portals this loop has hit (Azerbaijan's e-qanun.az, Albania's
  qbz.gov.al, South Korea's law.go.kr, all JS single-page apps this
  loop's tools could not render), `laws.gov.ag` serves its PDFs directly
  at predictable `wp-content/uploads/...` paths -- but WebFetch itself
  failed on every `laws.gov.ag` (and every other `*.gov.ag`) URL with
  'unable to verify the first certificate', an incomplete TLS chain
  rather than a JS-rendering problem. curl with a standard user-agent
  succeeded on every attempt (HTTP 200), so every entry below was
  downloaded directly and its text extracted via `pdftotext -layout` and
  read -- HIGH confidence, no secondary-source fallback needed for any
  of the three entries:

  - Companies Act, 1995 (Antigua and Barbuda's own Companies Act, No. 18
    of 1995) -- confirmed via its own text: 'ENACTED by the Parliament of
    Antigua and Barbuda', assented by Governor-General James B. Carlisle,
    with section 8 titled 'Certificate of incorporation', matching
    ABIPCO's (the Antigua and Barbuda Intellectual Property and Commerce
    Office's) own description of what it issues on company registration.
    The bracketed assent date in the extracted PDF text reads '[28th
    March, 19%]' -- `pdftotext` garbled the final year digit, but the
    year is unambiguous from the Act's own repeated header ('No. 18 of
    1995', 'The Companies Act, 1995') and independently from NATLEX's
    listing of the same Act.
  - Antigua and Barbuda Labour Code, Chapter 27 of the Laws of Antigua
    and Barbuda -- confirmed via its own text, a consolidated chapter
    (`CAP. 27`) organized into Divisions (Declaratory, Administration,
    Basic Employment, Employment Health/Safety/Welfare, Women/Young
    Persons/Children Employment, Work Permits, Trade Unions, Bargaining
    Agents' Registration, Employee-Representation Questions, Industrial
    Relations). The consolidated text itself marks its original
    enactment inline as '(19th September, 1975.) 14/1975' -- Act No. 14
    of 1975 -- independently corroborated by NATLEX (ILO's legislative
    database), which separately lists 'Antigua and Barbuda Labour Code
    (No. 14 of 1975) (Cap. 27)'.
  - Data Protection Act, 2013 (No. 10 of 2013) -- confirmed via its own
    text: 'Published in the Official Gazette Vol. XXXIII No. 64 dated
    7th November, 2013', enforced by an Information Commissioner.

  A law not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of statute entries. `:statute/url` + `:statute/law-number`
  are the citation the governor requires before any compliance-fact
  proposal referencing this law can commit."
  {"ATG"
   [{:statute/id "atg.companies-act"
     :statute/title "Companies Act, 1995"
     :statute/jurisdiction "ATG"
     :statute/kind :law
     :statute/law-number "No. 18 of 1995"
     :statute/url "https://laws.gov.ag/wp-content/uploads/2018/08/a1995-18.pdf"
     :statute/url-provenance :official-laws-gov-ag
     :statute/enacted-date "1995-03-28"
     :statute/retrieved-at "2026-07-21"
     :statute/topic #{:corporate-governance :incorporation}}
    {:statute/id "atg.labour-code"
     :statute/title "Antigua and Barbuda Labour Code"
     :statute/jurisdiction "ATG"
     :statute/kind :law
     :statute/law-number "No. 14 of 1975 (consolidated as CAP. 27, Laws of Antigua and Barbuda)"
     :statute/url "https://laws.gov.ag/wp-content/uploads/2018/08/cap-27.pdf"
     :statute/url-provenance :official-laws-gov-ag
     :statute/enacted-date "1975-09-19"
     :statute/retrieved-at "2026-07-21"
     :statute/topic #{:labor :employment}}
    {:statute/id "atg.data-protection-act"
     :statute/title "Data Protection Act, 2013"
     :statute/jurisdiction "ATG"
     :statute/kind :law
     :statute/law-number "No. 10 of 2013"
     :statute/url "https://laws.gov.ag/wp-content/uploads/2019/02/a2013-10.pdf"
     :statute/url-provenance :official-laws-gov-ag
     :statute/enacted-date "2013-11-07"
     :statute/retrieved-at "2026-07-21"
     :statute/topic #{:data-protection :privacy}}]})

(defn spec-basis
  "The jurisdiction's statute vector, or nil -- nil means NO spec-basis
  for that jurisdiction yet."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report, same shape/discipline as `marketentry.facts/coverage`:
  never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-atg statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "ATG")) " ATG statutes seeded with an "
                 "official government-hosted citation. Extend "
                 "`statute.facts/catalog`, never fabricate a law-id or URL.")})))

(defn by-topic
  "Statutes for `iso3` tagged with `topic` (e.g. :labor, :data-protection)."
  [iso3 topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis iso3)))
