# ADR-0001: Architecture — Antigua and Barbuda market-entry compliance actor (`marketentry`)

**Status**: accepted
**Date**: 2026-07-21

## Context

`cloud-itonami-iso3166-atg` was published as a `:blueprint` (docs +
`blueprint.edn` only, then a country-level `culture.facts` catalog in a
separate Wave 1 batch) but carried ZERO `src/marketentry` or
`src/statute` content -- its `:public-sector/market-entry-compliance`
domain, declared in `blueprint.edn`, was unimplemented. This ADR closes
that gap, following the pattern established by `cloud-itonami-iso3166-jpn`
(origin) and `cloud-itonami-iso3166-bgr` / `cloud-itonami-iso3166-aze` /
`cloud-itonami-iso3166-alb` / `cloud-itonami-iso3166-arm` (the simpler,
no-`goyoukiki` shape this blueprint also uses -- `blueprint.edn`'s
`:required-technologies` does not list `:ontology`, so this fork skips
the `marketentry.goyoukiki` real-tender-fact bridge JPN carries).

## Decision

Build the full governed-actor architecture for `marketentry`, mirroring
JPN/BGR/AZE/ALB/ARM's harness verbatim (StateGraph node names, governor
hard/escalate contract, phase 0-3 rollout, `Store` protocol with
MemStore + DatomicStore parity) and researching Antigua and Barbuda's
own real market-entry rules from scratch for the country-specific
content.

- **Store**: `marketentry.store`, MemStore + DatomicStore, proven parity
  via contract test.
- **Registry**: `marketentry.registry`, pure DRAFT-certificate
  construction via `unsigned-certificate`, jurisdiction-scoped sequence
  numbering (`ATG-DFT-000000`, `ATG-SUB-000000`), plus the flagship
  tiered vendor-class recompute (see below).
- **Governor**: `:market-entry-compliance-governor` (family keyword from
  `blueprint.edn`).
- **Entity shape**: `engagement`, sequential draft -> submit on the same
  record. `high-stakes` = `#{:actuation/draft-filing
  :actuation/submit-filing}`.
- **Phase**: 0->3; `:filing/draft` and `:filing/submit` NEVER auto-
  commit at any phase.

### Which body administers procurement -- three candidates checked, not assumed

The task named three candidates to investigate: a "Central Board of
Standards", a Ministry of Finance procurement unit, or a dedicated
Public Procurement Board. WebSearch confirmed no "Central Board of
Standards" exists for Antigua and Barbuda -- the closest-sounding real
body, the Antigua and Barbuda Bureau of Standards (`abbs.gov.ag`),
administers the unrelated Standards Act, 2017 (product/quality
standards). The real answer is the third candidate, split into two
statutory bodies working together: the Procurement Administration Act,
2011 (No. 16 of 2011, downloaded directly from `laws.gov.ag` and read in
full via `pdftotext` after every WebFetch attempt on `*.gov.ag` failed
with "unable to verify the first certificate" -- curl with a standard
user-agent succeeded on every attempt) establishes a Procurement Unit
inside the Ministry of Finance (s.8) headed by a Chief Procurement
Officer (s.9), and a separate Procurement Board (s.38) with its own
duties and powers (s.44). Its own long title states it repeals the
Tenders Board Act, 1991 (as amended 2002) -- but the operating portal,
`tendersboard.gov.ag`, retains the old brand name even though the 2011
Act legally renamed the body; the portal's own "Procurement Board" page,
fetched directly, confirms the Board is chaired by the Permanent
Secretary, Ministry of Finance and Corporate Governance, consistent with
the Act's own text.

### Flagship HARD check: `vendor-class-insufficient` -- a fourth distinct check SHAPE

`tendersboard.gov.ag/vendors/`, fetched directly, publishes a real,
current vendor-registration classification tiered by bid/contract
value: Class 1 (baseline bidder/company information) for bids below
EC$50,000, Class 2 (Class 1 plus at least three references) for bids
between EC$50,000 and EC$1,000,000, and Class 3 (Class 1 plus reference
letters and, where requested, audited financial statements) for bids
above EC$1,000,000 -- each class's documented requirements are a
superset of the class below it. `marketentry.registry/required-vendor-
class` independently recomputes which of the three tiers an
engagement's own declared `:contract-value` requires, and
`vendor-class-insufficient?` HARD-holds `:filing/submit` if the
engagement's own declared `:vendor-class` falls short of it.

This is a genuinely different check SHAPE than every prior iso3166
sibling: Bulgaria's ЗОП Art. 54(5) de-minimis is a PERCENTAGE-OF-
TURNOVER formula, Albania's Neni 76(2)(c) carve-out is a FLAT STATUTORY
CONSTANT, and Azerbaijan's/Armenia's flagship checks are plain BOOLEAN
registry-membership reads. This is a discrete THREE-TIER THRESHOLD
classification recompute over the engagement's own declared numeric
field -- a fourth distinct shape, not a lesser version of any prior one.

The Procurement Administration Act itself ALSO gives the Board a power
(s.44(1)(k)) to "suspend or debar a person from participating in
solicitations" -- a candidate for a second, boolean-shaped check similar
to Azerbaijan's/Armenia's. This iteration deliberately did NOT implement
it as a second governor check: the Act delegates the actual grounds for
suspension/debarment to Regulations (s.50(1)(o)), and no gazetted
Regulations text could be located on `laws.gov.ag` within this
iteration's time budget to ground a genuine check against, unlike
Albania's own deliberate non-implementation of its Neni 78 excluded-
operators list (a real, found, but scope-disciplined omission, not a
failure to look) -- this mirrors that same discipline rather than
padding scope with an ungrounded or half-grounded second check.

### Other HARD checks (all unoverridable)

1. **spec-basis** -- never invent a jurisdiction's market-entry
   requirements (`marketentry.facts` G2 catalog: tendersboard.gov.ag,
   ABIPCO, IRD for ATG).
2. **evidence-incomplete** -- draft/submit require a full assessment
   checklist on file.
3. **vendor-class-insufficient** -- see above (FLAGSHIP).
4. **engagement-fee-mismatch** -- recompute `base-fee + monthly-rate ×
   monitoring-months` (ground-truth-recompute discipline).
5. **tin-unverified** -- conditional on `:requires-tin?` (Taxpayer
   Identification Number, issued by the Inland Revenue Department as a
   SEPARATE, subsequent act to ABIPCO business registration -- see
   `marketentry.facts` for the one-act-vs-two-act investigation).
6. **already-drafted / already-submitted** -- dedicated booleans, never
   a `:status` value.

### `rep-spec-basis`: honestly nil, like Azerbaijan's

This iteration specifically looked for a personal-exclusion-grounds
provision in the Procurement Administration Act's own text extending
disqualification to a bidder's representatives/directors/officers (the
shape BGR's ЗОП Art. 54(2)-(3), ALB's Neni 76(1) and ARM's Article
6(1)(3) each document for their own laws) and did not find one -- the
Act's own debarment power (s.44(1)(k)) is general and delegates its
grounds to Regulations this iteration could not locate/verify as
gazetted. Rather than infer or reuse a sibling jurisdiction's grounds,
`rep-spec-basis` returns nil for ATG, the same honest-scope-narrowing
discipline AZE's catalog already established for this family.

### The one-act-vs-two-acts business-registration/TIN question

The task asked every iteration to investigate, rather than assume,
whether business registration and tax-ID issuance happen in one act or
two. For Antigua and Barbuda this iteration found a clean TWO-ACT
answer, directly from the Inland Revenue Department's own guidance
document (`ird.gov.ag`, fetched directly): a company must first obtain
a Certificate of Incorporation from ABIPCO (the Antigua and Barbuda
Intellectual Property and Commerce Office, under the Companies Act
1995) BEFORE it can apply to the IRD for a Taxpayer Identification
Number (TIN) using forms CB001 + F16, with the Certificate of
Incorporation itself required as a prerequisite document on the TIN
application. This is structurally the SAME two-act shape this catalog's
own JPN sibling documents (Corporate Number issued by the National Tax
Agency, separately from GEPS/qualification registration) -- and
genuinely DIFFERENT from the one-act models this loop found for Albania
(QKB), Armenia (State Register) and Azerbaijan (State Tax Service),
where a single registration act both creates the entity and issues its
tax/business identifier.

### `statute.facts` (second, orthogonal catalog)

Three Antigua and Barbuda statutes, all confirmed by downloading the PDF
directly from `laws.gov.ag` (Antigua and Barbuda's own official
consolidated-law portal) via curl (every direct WebFetch attempt on
`*.gov.ag` failed with an incomplete-TLS-chain error, not a JS-SPA
blocker) and reading the extracted text: the Companies Act, 1995 (No. 18
of 1995), the Antigua and Barbuda Labour Code (CAP. 27, whose own text
marks its original enactment inline as Act No. 14 of 1975 -- 19
September 1975 -- independently corroborated by NATLEX), and the Data
Protection Act, 2013 (No. 10 of 2013).

## Consequences

- `src/` now genuinely exists with real, tested, curl/pdftotext-cited
  content for this blueprint's declared domain (`:public-sector/
  market-entry-compliance`) -- moves this repo's
  `manifest/itonami-fleet-audit.edn` `:prod-ready?` signal from `:stub`
  to `:active`.
- The existing `culture.facts` catalog (Wave 1, unrelated batch) is
  untouched.
- The Procurement Administration Act's own suspend/debar power
  (s.44(1)(k)) is a genuine, verified, NOT-implemented extension point
  for a future iteration -- only if that iteration can locate and read
  the gazetted Regulations that actually specify its grounds.
- Sibling country blueprints can continue forking JPN/BGR/AZE/ALB/ARM/ATG
  and swapping in their own genuinely-researched `marketentry.facts` /
  `statute.facts` content and whichever flagship check their own law
  actually supports -- this ADR is itself further evidence that the
  flagship check should be chosen from real, currency-checked research,
  not copied by rote, and that a shape need not be boolean just because
  two prior siblings' shapes happened to be.
