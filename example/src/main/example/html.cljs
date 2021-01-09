(ns example.html
  (:require
   [jkxyz.tailwind :refer [tw]]))

(defn some-component []
  [:div {:class (tw {:lg [:container {:mt 2}]})}
   [:p {:class (tw :bg-gray-100 :text-xl)}]])
