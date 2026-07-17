(ns culture.facts-test
  (:require [clojure.edn :as edn]
            [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [culture.facts :as facts]))

(deftest atg-has-culture-basis
  (let [sb (facts/spec-basis "ATG")]
    (is (= 6 (count sb)))
    (is (= (count sb) (count (set (map :culture/id sb)))))
    (is (every? #(str/starts-with? (:culture/url %) "https://") sb))
    (is (every? #(= "ATG" (:culture/country %)) sb))
    (is (every? #(nil? (:culture/municipality %)) sb))
    (is (every? #(seq (:culture/summary %)) sb))
    (is (every? #(string? (:culture/retrieved-at %)) sb))))

(deftest unknown-jurisdiction-has-no-basis
  (is (nil? (facts/spec-basis "JAM")))
  (is (nil? (facts/spec-basis "zzz"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["ATG" "JAM"])]
    (is (= 2 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["JAM"] (:missing-jurisdictions c)))))

(deftest by-kind-filters
  (is (= 2 (count (facts/by-kind "ATG" :dish))))
  (is (= ["atg.product.antigua-black-pineapple"]
         (mapv :culture/id (facts/by-kind "ATG" :product))))
  (is (empty? (facts/by-kind "ATG" :other)))
  (is (empty? (facts/by-kind "JAM" :dish))))

(deftest tx-file-matches-catalog
  (let [tx (edn/read-string (slurp "data/culture-tx.edn"))
        flat (mapcat val (sort-by key facts/catalog))]
    (is (= (vec flat) (vec tx)))))
