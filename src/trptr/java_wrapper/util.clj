;; Copyright 2018, 2019 trptr

;; This file is part of trptr/java-wrapper.

;; trptr/java-wrapper is free software: you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; trptr/java-wrapper is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with this program.  If not, see <https://www.gnu.org/licenses/>.

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
