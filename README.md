# cloud-itonami-iso3166-atg

Open ISO 3166 Blueprint for **ATG**: Antigua and Barbuda --
**`:implemented`**.

This repository designs **and implements** a forkable OSS business for
an independent public-sector market-entry consultant: an already-
incorporated operator (e.g. a `cloud-itonami-cofog-{code}`,
`cloud-itonami-isco-{code}`, `cloud-itonami-unspsc-{segment}` or
`cloud-itonami-{ISIC}` blueprint fork) gets a Compliance Advisor +
independent **Market-Entry Compliance Governor** to navigate public-
procurement registration, local business/tax registration, and
regulatory-compliance rules in Antigua and Barbuda, so the operator can
win and service a government contract without hiring a full in-house
compliance department.

## Official surface (curl-verified 2026-07-21 -- WebFetch failed on every `*.gov.ag` host with an incomplete TLS chain, curl with a standard user-agent succeeded on every attempt)

- Procurement: `tendersboard.gov.ag`, operated by the Procurement Board
  and Procurement Unit (Ministry of Finance and Corporate Governance),
  established by the Procurement Administration Act, 2011 (No. 16 of
  2011), which repealed the Tenders Board Act, 1991 (as amended 2002).
  Vendor registration is tiered into Class 1/2/3 by bid/contract value
  (`tendersboard.gov.ag/vendors/`).
- Business registration: the Antigua and Barbuda Intellectual Property
  and Commerce Office (ABIPCO, `abipco.gov.ag`), a Department of the
  Ministry of Justice, Legal Affairs, Public Safety and Labour,
  established under the Intellectual Property Office Act of 2003 --
  issues a Certificate of Incorporation under the Companies Act, 1995
  (No. 18 of 1995).
- Tax: the Inland Revenue Department (IRD, `ird.gov.ag`), a Department
  of the Ministry of Finance and Corporate Governance, issues the
  Taxpayer Identification Number (TIN) as a SEPARATE, subsequent act to
  ABIPCO business registration (an ABIPCO Certificate of Incorporation
  is a prerequisite document on the IRD's own TIN application form).

## Implementation (R0)

| Piece | Location |
|---|---|
| Actor namespaces | `src/marketentry/*` |
| Governor | `:market-entry-compliance-governor` |
| Ops | `:engagement/intake` · `:jurisdiction/assess` · `:filing/draft` · `:filing/submit` |
| Flagship HARD check | `vendor-class-insufficient` (tendersboard.gov.ag's tiered Class 1/2/3 vendor-registration classification, independently recomputed against the engagement's own declared contract value -- see `docs/adr/0001-architecture.md`) |
| Compliance catalog | `src/statute/facts.kotoba` -- Companies Act 1995, Labour Code (CAP. 27), Data Protection Act 2013 |
| Tests | `clojure -M:dev:test` |
| Demo | `clojure -M:dev:run` |
| Architecture ADR | [`docs/adr/0001-architecture.md`](docs/adr/0001-architecture.md) |

`:filing/submit` is never in any phase's `:auto` set -- human sign-off
is structural, not a rollout milestone.

## No robotics premise -- digital/data service exemption

Market-entry and procurement-compliance navigation is a pure data/software
service with no physical-domain work (portal registration, document
checklists, regulatory-change monitoring) -- the same exemption class as
`cloud-itonami-6310` (HR SaaS replacement) and `cloud-itonami-gtin-*`.
`blueprint.edn` sets `:itonami.blueprint/robotics false` and
`:required-technologies` lists only real capabilities (`:identity`,
`:forms`, `:dmn`, `:bpmn`, `:audit-ledger`), no `:robotics`.

## Core Contract

```text
operator intake + prior filing history
        |
        v
Compliance Advisor -> Market-Entry Compliance Governor -> filing draft, or human sign-off
        |
        v
gated portal registration / filing submission + audit ledger
```

No automated proposal can submit a portal registration or filing the
governor refuses, suppress a compliance record, or claim a legal/tax
conclusion the governor has not cleared. `:filing/submit` is never in any
phase's `:auto` set -- it always requires human sign-off.

## What this is NOT

- **Not the government of Antigua and Barbuda.** This blueprint is an
  independent operator the government contracts with or that bids into
  its procurement -- never the government itself, and never an official
  channel.
- **Not legal or tax advice.** Every regulatory claim must cite the
  official source and route final filings to Antigua-and-Barbuda-
  licensed counsel or a registered agent where the law requires licensed
  representation.

## Capability layer

Required capabilities (`blueprint.edn`):

- :identity
- :forms
- :dmn
- :bpmn
- :audit-ledger

See [`docs/business-model.md`](docs/business-model.md) and
[`docs/operator-guide.md`](docs/operator-guide.md).

## License

AGPL-3.0-or-later.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Antigua and Barbuda:

- `src/culture/facts.kotoba` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
