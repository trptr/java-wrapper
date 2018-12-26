(ns trptr.java-wrapper.util

  "General utility fns and macros used for creating Clojure wrappers of Java classes."

  (:require [clojure.string :as str]))

(def split-camel
  "Turns someMethodName (a string or symbol, etc) into (\"some\", \"method\", \"name\").
  C.f. [[strs->symbol]]."
  (comp (partial map str/lower-case)
        #(str/split % #"(?=[A-Z])")
        name))

(def strs->symbol
  "Turns (\"some\", \"method\", \"name\") into some-method-name (a symbol).
  See [[split-camel]]."
  (comp symbol
        (partial str/join "-")))

(def dashed-symbol
  "Turns someMethodName (a string or symbol, etc) into some-method-name (a symbol)."
  (comp strs->symbol split-camel))

(defmacro doseq-m
  "Calls macro on each element of coll."
  [macro coll]
  (doseq [x coll]
    (eval `(~macro ~x))))

(defmacro if-call
  "Calls `method` of `obj` if `arg` is non nil. Returns `obj`.
  A special one-method version of clojure.core/doto."
  [obj method arg]
  `(let [gx# ~obj]
     (when ~arg (. gx# ~method ~arg))
     gx#))
