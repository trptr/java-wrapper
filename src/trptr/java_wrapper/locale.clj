(ns trptr.java-wrapper.locale

  "Clojure wrapper for Java Locale.

  Note: wherever a Java method requires a Locale object, not only such an object,
  but also a \"locale specification\" is accepted.
  In such cases the Java method (called by the Clojure wrapper) will get
  the Locale object \"specified\" by the \"specification\". This means, that
  (unless we pass a Locale object already to the Clojure wrapper), the needed Locale object
  is created \"on the fly\", \"behind the scenes\", inside the Clojure wrapper.

  Below are the possibilities for such \"**locale specifications**\" and their interpretations.

  * **`nil`**  
  In this case the Locale object, \"specified\" by this \"specification\"
  is the one returned by Java's `Locale.getDefault()`.

  * a **Java Locale object**  
  In this case the resulting Locale object is the same as the \"specification\".

  * a **map** with keys corresponding to the Locale.Builder class' setXXX instance methods,
  i.e. `:language`, `:region`, `:script`, `:variant`, etc.  
  The resulting Locale object is the one
  returned by a corresponding [[build]] call. See examples at [[build]].

  * a **keyword** or **string**  
  In this case the \"specification\" is splitted at \"-\" and \"_\" characters;  
  the resulting Locale object is the one returned by
  [[make-locale]] applied on the result of the splitting.

  For more info see [[locale]]."

  (:require [clojure.string :as str]
            [trptr.java-wrapper.util :as jwu])
  (:import  [java.util Locale Locale$Category Locale$Builder #_Locale$IsoCountryCode]))

;; some ideas are from:
;; https://github.com/ptaoussanis/tower/blob/master/src/taoensso/tower.cljx

(def country-code "Turns its argument into a \"normalized\" country code string." (comp str/upper-case name))
(def language-code "Turns its argument into a \"normalized\" language code string." (comp str/lower-case name))

;;; Locale.Builder
(defn build
  "Wrapper of Java's Locale.Builder/build.

  Notes:  
  * sets `locale` first (if any), then the others.
    So the properties of `locale` might be overwritten in the result.  
  * `language` and `region` can be strings or keywords and are case insensitive.  
  * use empty strings for the removal of settings.

  Examples:
  ```clojure
  (get-display-country (build {:region \"029\"})) ;; → Caribbean
  ```
  ```clojure
  (get-display-script {:script \"Arab\"}) ;; → Arabic
  ```
  ```clojure
  (build {:language-tag \"de-POSIX-x-URP-lvariant-Abc-Def\"})
  ;; → #object[java.util.Locale 0x68230234 \"de__POSIX_Abc_Def_#x-urp\"]
  ```

  See also [[to-language-tag]]!"

  [{:keys [#_extension language language-tag locale region script variant]}]
  (-> (cond-> (Locale$Builder.)
        ;;extension
        locale       (.setLocale locale)
        language     (.setLanguage (language-code language))
        language-tag (.setLanguageTag language-tag)
        region       (.setRegion (country-code region))
        script       (.setScript script)
        variant      (.setVariant variant))
      (.build)))

(defn make-locale
  "Wrapper of java.util.Locale's constructor: creates a Java Locale object from:  
  * a language code OR keyword (\"en\" or :en) and  
  * an optional country code OR keyword (\"DE\" or :de) and  
  * an optional vendor-specific variant code.  
  Case insensitive."
  ([lang]                 (Locale. (language-code lang)))
  ([lang country]         (Locale. (language-code lang) (country-code country)))
  ([lang country variant] (Locale. (language-code lang) (country-code country) variant)))

;;; Locale.Category
;;(def locale-category-values #(java.util.Locale$Category/values)) ;; makes sense?? -- use 'categories' instead
(def categories
  "Java Locale.Category constants in a map with keys`:locale.category/display` and `:locale.category/format`."
  #:locale.category{:display (Locale$Category/DISPLAY)
                    :format  (Locale$Category/FORMAT)})

(defn get-default
  "Direct wrapper of the Java method.  
  `locale-category` is a value from the map [[categories]]."
  ([] (Locale/getDefault))
  ([locale-category] (Locale/getDefault locale-category)))

(def split-locale
  "Accepts a string or a keyword and returns a vector of strings created from the argument's parts.  
  The argument's parts must be separated by either a dash (\"-\") or an underscore (\"_\").  
  Keywords' namespaces are ignored."
  #(str/split (name %) #"[-_]"))

(defn locale
  "The main Locale creator API: returns a Java Locale object if called:  
  * without arguments;  
  * with 1 \"locale specification\" (see the docstring of this ns, above);  
  * a series of proper string or keyword subtags.

  Returns the defualt locale in case of no arguments or if its single argument is nil.

  Returns its single argument untouched if it is already a Locale object.

  If it gets 2 or more arguments, they are passed untouched to [[make-locale]].

  If it gets a single map, it is passed untouched to [[build]].

  Otherwise it turns the got string or keyword (you choose!) into a
  series of strings (by [[split-locale]]) and then passes the result to [[make-locale]].

  Valid `spec` examples:
  `\"fr-ar\"`, `\"EN_CA-variaNt\"`, `:ar--vari2`, `:-ng`, `:ru_CN`, `{:variant \"varIAnt\"}`."
  ([] (get-default))
  
  ([spec] (cond
            (instance? Locale spec) spec
            (map? spec)             (build spec)
            (nil? spec)             (get-default)
            :else                   (apply make-locale (split-locale spec))))

  ([lang & others] (apply make-locale lang others)))

(def available-locales
  "Wrapper of the Java method 'getAvailableLocales', as a set.  
  Can be used to check e.g. if a freshly made Locale object is 'valid' on the system:  
  `(available-locales (locale :-an))` is usually `nil`, while there is a good chance that
  e.g. `(available-locales (locale :en))` is truethy..."
  (set (Locale/getAvailableLocales)))

(def low-kw "Turns its argument into a keyword of lowercase letters." (comp keyword str/lower-case name))

(def get-iso-countries "Direct wrapper of the Java method. Consider using [[iso-countries]] instead." #(Locale/getISOCountries))
(def get-iso-languages "Direct wrapper of the Java method. Consider using [[iso-languages]] instead." #(Locale/getISOLanguages))

(def iso-countries "All Java ISO country codes as Clojure keywords, in a set."   (set (map low-kw (get-iso-countries))))
(def iso-languages "All Java ISO language codes as Clojure keywords, in a set. " (set (map low-kw (get-iso-languages))))

;;(def iso-country? iso-countries)
;;(def iso-language? iso-languages)

(def iso-country-kw  "Turns its argument into an ISO country code keyword OR `nil`."  (comp iso-countries low-kw))
(def iso-language-kw "Turns its argument into an ISO language code keyword OR `nil`." (comp iso-languages low-kw))

;;; other static methods
(def for-language-tag
  "Direct wrapper of the Java method. See also [[to-language-tag]]!  
  Example: `(for-language-tag \"fr-FR\")` → `#object[java.util.Locale 0x163fd507 \"fr_FR\"]`."
  #(Locale/forLanguageTag %))

(defmacro ^:private defn-locale-spec-wrapper
  "Same as `defn`, but prepends a ~fix text to the docstring."
  [fn-name docstring & definition]
  (let [java-method-name (jwu/camel-symbol fn-name)]
    `(defn ~fn-name
       ~(str "Wrapper of the Java method `" java-method-name
             "`, but accepts \"locale specifications\" as well "
             " (not only Locale objects).  "
             "See the docstring of this `ns` (above), for the description of \"locale specifications\"."
             docstring)
       ~@definition)))

(defn-locale-spec-wrapper set-default nil
  ([new-locale] (Locale/setDefault (locale new-locale)))
  ([locale-category new-locale] (Locale/setDefault locale-category (locale new-locale))))

;;; other instance methods
(def to-language-tag
  "Wrapper of the Java method, but accepts \"locale specifications\" as well.  

  Can be used to 'serialize' a Locale object as a string, that can later be used to recreate the object:
  `(build {:language-tag (to-language-tag <some-locale>)})` should result `<some-locale>`.

  See also Java doc for Compatibility of Locale: \"toLanguageTag cannot represent the state of locales
  whose language, country, or variant do not conform to BCP 47.

  [...] it is recommended that clients migrate away from constructing non-conforming locales
  and use the forLanguageTag and Locale.Builder APIs instead.

  Clients desiring a string representation of the complete locale can then always rely on toLanguageTag for this purpose.\""
  #(.toLanguageTag (locale %)))

(defmacro ^:private defn-on-locale
  "A specialized version of `defn`, used to create wrappers of the simplest instance methods of Locale. "
  [java-method-name]
  (let [clj-fn-name (jwu/dashed-symbol java-method-name)]
    `(defn-locale-spec-wrapper ~clj-fn-name nil [loc#] (. (locale loc#) ~java-method-name))))

(jwu/doseq-m defn-on-locale
             [getLanguage getCountry getVariant getScript
              hasExtensions stripExtensions])

(defmacro ^:private defn-get-display-property
  "A specialized version of `defn`, used for the creation of wrappers for instance methods
  which accept optional extra locale parameter."
  [java-method-name]
  (let [clj-fn-name (jwu/dashed-symbol java-method-name)]
    `(defn-locale-spec-wrapper ~clj-fn-name nil
       ([loc#] (. (locale loc#) ~java-method-name))
       ([loc# in-locale#] (. (locale loc#) ~java-method-name (locale in-locale#))))))

(jwu/doseq-m defn-get-display-property
             [getDisplayLanguage getDisplayCountry getDisplayVariant getDisplayScript getDisplayName])

(defn-locale-spec-wrapper get-iso-3-language nil [loc] (.getISO3Language (locale loc)))

(defn-locale-spec-wrapper get-iso-3-country 
  "  \nNote: if the argument is a string or a keyword, then 
  the country comes from the second subtag.
  So use e.g. `:-an` or \"-an\" instead of `:an` or \"an\"."
  [loc]
  (.getISO3Country (locale loc)))

;;; constants

(def unicode-locale-extension "The Java constant." Locale/UNICODE_LOCALE_EXTENSION)

(def predef-locales
  "The constants from the Java class' static fields in a map
  with keys `:locale/english`, `:locale/uk`, etc.
  Not all Java constants are wrapped yet."
  #:locale{:canada        Locale/CANADA
           :canada-french Locale/CANADA_FRENCH
           :china         Locale/CHINA
           :chinese       Locale/CHINESE
           :english       Locale/ENGLISH
           :uk            Locale/UK
           :us            Locale/US
           :france        Locale/FRANCE
           :french        Locale/FRENCH
           :german        Locale/GERMAN
           :germany       Locale/GERMANY
           :root          Locale/ROOT})
