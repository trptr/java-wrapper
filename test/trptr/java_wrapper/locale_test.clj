(ns trptr.java-wrapper.locale-test
  (:require [clojure.test :refer :all]
            [clojure.set]
            [trptr.java-wrapper.locale :refer :all])
  (:import  [java.util Locale Locale$Category Locale$Builder]))

(deftest basics

  (doseq [x ['AL 'al 'Al 'aL
             "AL" "al" "Al" "aL"
             :AL :al :Al :aL]]
    (is (= "AL" (country-code x))))

  (doseq [x ['AL 'al 'Al 'aL
             "AL" "al" "Al" "aL"
             :AL :al :Al :aL]]
    (is (= "al" (language-code x))))

  (is (= (java.util.Locale$Category/DISPLAY)
         (:locale.category/display categories)))

  (is (available-locales (locale)))
  (is (clojure.set/subset? #{:lk :so :my :ly} iso-countries))
  (is (not (clojure.set/subset? #{:kl} iso-countries)))

  (is (= :es (iso-language-kw "es")))
  (is (nil? (iso-language-kw "sb")))

  (is (= Locale/GERMANY (:locale/germany predef-locales)))

  (is (= "fr" (get-language :fr)))
  (is (not (has-extensions :fr))))

(deftest creating-locales+properties

  (let [loc-af (Locale. "af")]
    (is (= loc-af (Locale. "AF")
           (make-locale "af") (make-locale :AF)
           (locale "aF") (locale :Af)
           (build {:language :af}) (build {:language :AF})))
    (is (= "Afrikaans" (get-display-language loc-af :en))))

  (let [loc-cs-sk-1var (Locale. "cs" "SK" "1var")]
    (is (= loc-cs-sk-1var
           (make-locale :cs "sk" "1var")
           (locale "cS-Sk-1var")
           (build {:language :cs
                   :region   :sk
                   :variant  "1var"})))
    (is (= "tchèque" (get-display-language loc-cs-sk-1var :fr)))
    (is (= "چیک" (get-display-language loc-cs-sk-1var :ur)))
    (is (= "Slovakia" (get-display-country loc-cs-sk-1var :en)))
    (is (= "سلوفاكيا" (get-display-country loc-cs-sk-1var :ar)))
    (is (= "1var" (get-display-variant loc-cs-sk-1var :zh)))
    (is (= "tcheco (Eslováquia, 1var)" (get-display-name loc-cs-sk-1var :pt)))
    (is (= "SVK" (get-iso-3-country loc-cs-sk-1var))))

  (is (not= (locale :cs-sk-1var)
            (locale :cs-sk-1Var)))

  (let [uk-cy (locale {:language :uk
                       :script   "Cyrl"})]
    (is (= "Ukrainisch" (get-display-language uk-cy :de)))
    (is (= "кириллица" (get-display-script uk-cy :ru)))
    (is (= "ukr" (get-iso-3-language uk-cy)))))

(deftest language-tag-handling
  (let [loc (locale :fr-ca)]
    (is (= loc
           (for-language-tag "fr-CA")
           (build {:language-tag (to-language-tag loc)})))))
