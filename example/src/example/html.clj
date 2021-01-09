(ns example.html
  (:require
   [jkxyz.tailwind :refer [tw]]))

(defn some-html []
  [:div {:class (tw :container {:mt 1})}
   [:p {:class (tw {:p 2})}
    "Some text"]])
