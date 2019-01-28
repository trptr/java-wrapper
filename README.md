# java-wrapper

Clojure wrappers for various Java classes.

[![Clojars Project](https://img.shields.io/clojars/v/trptr/java-wrapper.svg)](https://clojars.org/trptr/java-wrapper)

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

The latter is idiomatic Clojure, so can be used in idiomatic Clojure ways comfortably, e.g.
the map can be `merge`d with other maps, mappings can be `assoc`ed, `dissoc`ed or selected, etc.

Moreover, in simpler cases a keyword (or a string...) can substitute a complete object,
which is then created on the fly, behind the scenes, as needed:

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

The latest version is 0.2.3, containing wrappers for 1 class: `java.util.Locale`.

Coming soon: `java.text.NumberFormat`.

Bug reports and new wrapper requests [are welcome](https://github.com/trptr/java-wrapper/issues/new/choose).


## Usage

### Via `deps.edn`

#### Using a git url

Include a dependency on this repo in your `deps.edn`. For example:

```clojure
{:deps {org.clojure/clojure {:mvn/version "1.10.0"}
        trptr/java-wrapper  {:git/url "https://github.com/trptr/java-wrapper.git"
                             :sha     "1b0da6aceee551e925174c7e72a8fa41dc5e57a0"}}}
```

#### Using mvn version

Include a dependency on the mvn version in your `deps.edn`. For example:

```clojure
{:deps {org.clojure/clojure {:mvn/version "1.10.0"}
        trptr/java-wrapper  {:mvn/version "0.2.3"}}}
```
### Via Leiningen

Include a dependency on the mvn version in your `project.clj`. For example:

```clojure
:dependencies [[org.clojure/clojure "1.10.0"]
               [trptr/java-wrapper "0.2.3"]]
```

### Others

See https://clojars.org/trptr/java-wrapper.

### Inside your `ns`

Require the proper trptr namespace(s). For example:

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

## Copyright and License

Copyright 2018, 2019 trptr

trptr/java-wrapper is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

If you need other licensing, let me know!
