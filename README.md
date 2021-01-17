# Tailwind for Clojure/Script

A convenience macro for writing Tailwind class names in Clojure/Script projects,
and purging unused classes for production builds.

``` clojure
(require '[tailwind.core :refer [tw]])

(tw :container {:mt 1} {:lg {:mx 2}}) ;;=> ["container" "mt-1" "lg:mx-2"]
(tw :flex :justify-center {:hover [:text-blue-500]}) ;;=> ["flex" "justify-center" "hover:text-blue-500"]
(tw {:mx -1}) ;;=> ["-mx-1"]
```

```
$ clj -X tailwind.cli/used-classes :cljs-nses '[example.main]' :output-to used-classes.txt
```

See the `example` directory for suggested usage.
