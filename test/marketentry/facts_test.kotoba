(ns marketentry.facts-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.facts :as facts]))

(deftest atg-has-spec-basis
  (let [sb (facts/spec-basis "ATG")]
    (is (some? sb))
    (is (string? (:provenance sb)))
    (is (seq (:required-evidence sb)))
    (is (some? (facts/corporate-number-spec-basis "ATG")))
    (is (some? (facts/vendor-class-spec-basis "ATG")))))

(deftest atg-rep-spec-basis-is-honestly-absent
  (testing "no verified personal-exclusion-grounds/resident-representative mandate for ATG public-procurement participation -- deliberately not claimed"
    (is (nil? (facts/rep-spec-basis "ATG")))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest required-evidence-satisfied
  (let [sb (facts/spec-basis "ATG")
        all (:required-evidence sb)]
    (is (true? (facts/required-evidence-satisfied? "ATG" all)))
    (is (not (facts/required-evidence-satisfied? "ATG" (take 1 all))))
    (is (nil? (facts/required-evidence-satisfied? "ATL" all)))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["ATG" "USA" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 2 (:covered c)))
    (is (= ["ATL"] (:missing-jurisdictions c)))))
