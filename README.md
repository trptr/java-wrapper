# java-wrapper

Clojure wrappers for various Java classes.

## Documentation

* this readme
* [API Docs](http://trptr.github.io/java-wrapper/index.html)
* [tests](test/trptr/java_wrapper): simple usage examples

## Usage via `deps.edn`

Include a dependency on this repo in your `deps.edn`. For example:

```clojure
{:deps {org.clojure/clojure {:mvn/version "1.10.0"}
        trptr/java-wrapper  {:git/url "https://github.com/trptr/java-wrapper.git"
                             :sha     "e42d0a34f48c331f2870d4d9e7f689e5e44805e2"}}}
```

Then require the proper namespace as needed. For example:

```clojure
(ns my-namespace
    (:require [trptr.java-wrapper.util :as jwu]))

(defn -main
  [& args]
  (println (jwu/split-camel "someMethodName")))
```

## License

GPL-3.0 -- if you need other licensing, let me know!
