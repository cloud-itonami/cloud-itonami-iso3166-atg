(ns culture.facts
  "Country-level regional-culture catalog for Antigua and Barbuda (ATG) --
  national dishes, protected products, beverages, crafts, festivals and
  heritage sites, per ADR-2607171400 addendum 2 (cloud-itonami-
  municipality-culture-catalog Wave 1, in com-junkawasaki/root). Sibling
  namespace to `marketentry.facts` / `statute.facts` (ADR-2607141700);
  city-level counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"ATG"
   [{:culture/id "atg.dish.fungee"
     :culture/name "Fungee"
     :culture/country "ATG"
     :culture/kind :dish
     :culture/summary "Caribbean cornmeal and okra dish known as fungee in the Leeward Islands, part of the national dishes of Antigua and Barbuda and of other Caribbean islands."
     :culture/url "https://en.wikipedia.org/wiki/Cou-cou"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "atg.dish.ducana"
     :culture/name "Ducana"
     :culture/country "ATG"
     :culture/kind :dish
     :culture/summary "Sweet potato dumpling or pudding from Antigua, also made in other Caribbean islands such as Saint Kitts and Nevis and Saint Vincent and the Grenadines."
     :culture/url "https://en.wikipedia.org/wiki/Ducana"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "atg.product.antigua-black-pineapple"
     :culture/name "Antigua black pineapple"
     :culture/country "ATG"
     :culture/kind :product
     :culture/summary "Pineapple variety grown on the southwestern coast of Antigua, the national fruit of Antigua and Barbuda."
     :culture/url "https://en.wikipedia.org/wiki/Antigua_black_pineapple"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "atg.festival.antigua-carnival"
     :culture/name "Antigua Carnival"
     :culture/country "ATG"
     :culture/kind :festival
     :culture/summary "Thirteen-day festival of costumes, pageants and music held annually in late July through early August in Antigua, celebrating emancipation from slavery."
     :culture/url "https://en.wikipedia.org/wiki/Antigua_Carnival"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "atg.festival.antigua-sailing-week"
     :culture/name "Antigua Sailing Week"
     :culture/country "ATG"
     :culture/kind :festival
     :culture/summary "Week-long yacht regatta held since 1967 in the waters off English Harbour, Antigua and Barbuda."
     :culture/url "https://en.wikipedia.org/wiki/Antigua_Sailing_Week"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "atg.heritage.nelsons-dockyard"
     :culture/name "Nelson's Dockyard"
     :culture/country "ATG"
     :culture/kind :heritage
     :culture/summary "Restored Georgian-era naval dockyard in English Harbour, Antigua, a UNESCO World Heritage Site as part of the Antigua Naval Dockyard and Related Archaeological Sites."
     :culture/url "https://en.wikipedia.org/wiki/Nelson%27s_Dockyard"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-atg culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "ATG"))
                 " ATG entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
