# Business Model: Independent Public-Sector Market-Entry & Procurement Compliance Service — Antigua and Barbuda

## Classification

- Repository: `cloud-itonami-iso3166-atg`
- ISO 3166: `ATG` (Antigua and Barbuda)
- Activity: public-procurement market-entry and ongoing regulatory-
  compliance navigation for an already-incorporated operator

## Customer

- an already-incorporated `cloud-itonami-cofog-{code}` /
  `cloud-itonami-isco-{code}` / `cloud-itonami-unspsc-{segment}` /
  `cloud-itonami-{ISIC}` operator wanting to bid on an Antigua and
  Barbuda public contract
- a foreign SME or civic-tech vendor entering the public sector in
  Antigua and Barbuda for the first time
- a `cloud-itonami-M6910` client that has just completed incorporation
  and now needs public-sector market access

## Offer

- registration walkthrough for `tendersboard.gov.ag` (the Procurement
  Board's portal, mandatory under the Procurement Administration Act,
  2011, No. 16 of 2011), including which vendor-registration Class
  (1/2/3) a given bid value requires
- business/tax registration checklist: Certificate of Incorporation
  from the Antigua and Barbuda Intellectual Property and Commerce Office
  (ABIPCO, Companies Act 1995), followed by Taxpayer Identification
  Number (TIN) registration with the Inland Revenue Department -- a
  separate, subsequent act
- vendor-class-sufficiency screening: independent verification that an
  operator's registered vendor Class actually meets what its own
  declared bid/contract value requires, before any filing submission
- ongoing regulatory-change monitoring subscription
- compliance-audit export package for the client's own records

## Revenue

- per-engagement market-entry fee (one-time registration + checklist
  completion)
- recurring regulatory-change monitoring subscription
- compliance-audit export package

## Trust Controls

- any actual portal registration or filing submission requires
  Market-Entry Compliance Governor clearance and always escalates to
  human sign-off (`:filing/submit` is never automated at any phase)
- a false or fabricated regulatory-requirement claim is a HARD hold that
  cannot be overridden by human approval alone -- it must be corrected
  against a cited official source first
- a registered vendor Class that falls short of what the engagement's
  own declared contract value requires (tendersboard.gov.ag's Class
  1/2/3 tiers) is a HARD hold on `:filing/submit`, independently
  recomputed rather than trusted from a self-reported Class
- this service does **not** provide legal or tax advice; characterization
  and filing on the client's behalf beyond checklist/draft assistance
  routes to Antigua-and-Barbuda-licensed counsel or a registered agent

## Boundary with adjacent actors (read before forking)

- **`cloud-itonami-M6910`**: helps a client BECOME a legal entity
  (incorporation, ISIC 6910) -- a prior, different regulatory phase
  (company law). This blueprint assumes incorporation is already done and
  handles public-procurement market entry (a different regulatory domain).
- **`cloud-itonami-cofog-{code}`**: a jurisdiction-agnostic operator
  template for ONE public function. This blueprint is the orthogonal
  jurisdiction-specific axis -- the two compose (fork a COFOG-function
  blueprint AND this one to operate in Antigua and Barbuda).
