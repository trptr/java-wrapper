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
  "Turns someMethodName (a string or symbol, etc) into some-method-name (a symbol).  
  Opposite of [[camel-symbol]]."
  (comp strs->symbol split-camel))

(defn camel-symbol
  "Turns clojure-name (a string or symbol, etc) into clojureName (a symbol).  
  Opposite of [[dashed-symbol]]."
  [clojure-name]
  (let [[f & r] (str/split (name clojure-name) #"-")]
    (->> (map str/capitalize r)
         (apply str f)
         symbol)))

(defmacro doseq-m
  "Calls macro on each element of coll."
  [macro coll]
  `(do ~@(for [x coll]
           `(~macro ~x))))

(defmacro if-call
  "Calls `method` of `obj` if `arg` is non nil. Returns `obj`.  
  A special one-method version of clojure.core/doto."
  [obj method arg]
  `(let [gx# ~obj]
     (when ~arg (. gx# ~method ~arg))
     gx#))
