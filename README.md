# java-wrapper

Clojure wrappers for various Java classes.

## Goal

Provide APIs for Java classes that make their usage comfortable within Clojure.

For example, compare the usage of `java.util.Locale$Builder.build` in the standard way:

```clojure
(-> (Locale$Builder.)
    (.setLanguage "ru")
    (.setScript   "Cyrl")
    (.build))
```
with the equivalent code using `trptr.java-wrapper.locale/build`:

```clojure
(build {:language "ru"
        :script   "Cyrl"})
```

In simpler cases a keyword (or a string...) can substitute a complete object, which is then created on the fly,
behind the scenes, as needed:

```clojure
(get-display-name :de-ch :en)
```

(using `get-display-name` from the `ns` `trptr.java-wrapper.locale`) is equivalent to

```clojure
(.getDisplayName (Locale. "de" "CH") (Locale. "en"))
```

## Documentation

* this readme
* [API and namepace docs](http://trptr.github.io/java-wrapper/index.html)
* [tests](test/trptr/java_wrapper): simple usage examples


## Status

The latest version is 0.2.1, containing wrappers for 1 class: `java.util.Locale`.

Coming soon: `java.text.NumberFormat`.


## Usage via `deps.edn`

Include a dependency on this repo in your `deps.edn`. For example:

```clojure
{:deps {org.clojure/clojure {:mvn/version "1.10.0"}
        trptr/java-wrapper  {:git/url "https://github.com/trptr/java-wrapper.git"
                             :sha     "722d2cca910880d66debacdd4ef724aea37156d7"}}}
```
Then require the proper namespace as needed. For example:

```clojure
(ns my-namespace
    (:require [trptr.java-wrapper.locale :as jwl]))

(defn -main
  [& _]
  (->> jwl/available-locales
       (map jwl/get-display-name)
       sort
       (clojure.string/join "\n")
       println))
```

## License

GPL-3.0 -- if you need other licensing, let me know!
