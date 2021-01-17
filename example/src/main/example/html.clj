(ns example.html
  (:require
   [tailwind.core :refer [tw]]))

(defn some-html []
  [:div {:class (tw :container {:mt 1})}
   [:p {:class (tw {:p 2})}
    "Some text"]])
