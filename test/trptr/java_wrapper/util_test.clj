(ns trptr.java-wrapper.util-test
  (:require [clojure.test :refer :all]
            [trptr.java-wrapper.util :refer :all]))

(deftest split-java-names
  (doseq [x ['someMethodName "someMethodName"
             :someMethodName "SomeMethodName"]]
    (is (= ["some" "method" "name"] (split-camel x)))))

(deftest make-clj-names
  (is (= 'some-method-name (strs->symbol ["some" "method" "name"]))))

(deftest java->clj
  (= 'some-method-name (dashed-symbol 'someMethodName)))

(defmacro defnv
  "Same as defn, just all the arguments must be in 1 coll."
  [v]
  `(defn ~@v))

(deftest doseq-macro
  (doseq-m defnv [[fn-a [i] (inc i)]
                  [fn-b [& s] (apply str s)]])
  (is (and (fn? fn-a)
           (= 2 (fn-a 1))
           (fn? fn-b)
           (= "abcde" (fn-b "ab" "c" "de")))))

(deftest conditional-method-call
  (let [nf-base  (java.text.NumberFormat/getInstance)
        max-base (.getMaximumFractionDigits nf-base)
        nf-nil   (if-call nf-base setMaximumFractionDigits nil)
        max-nil  (.getMaximumFractionDigits nf-nil)
        nf-inc   (if-call nf-base setMaximumFractionDigits (inc max-base))
        max-inc  (.getMaximumFractionDigits nf-inc)]
    (is (and (= nf-base nf-nil nf-inc)
             (= max-base max-nil (dec max-inc))))))
