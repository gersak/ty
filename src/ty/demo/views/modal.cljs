(ns ty.demo.views.modal
  (:require [ty.demo.state :as state]))

(defn modal-view []
  [:div.max-w-6xl.mx-auto
   [:div.mb-8
    [:h1.text-3xl.font-bold.text-gray-900.dark:text-white.mb-2
     "Modal Component"]
    [:p.text-lg.text-gray-600.dark:text-gray-400
     "A modal component that wraps the native HTML dialog element."]]

   ;; Basic test
   [:div.demo-section
    [:h2.demo-title "Basic Modal Test"]
    [:ty-button {:on {:click #(swap! state/state assoc :modal-basic-open true)}}
     "Open Modal"]

    ;; Simple modal
    [:ty-modal {:open (get @state/state :modal-basic-open false)
                :on {:ty-modal-close #(swap! state/state assoc :modal-basic-open false)}}
     [:div.p-6
      [:h3 "Test Modal"]
      [:p "This is a test modal."]
      [:ty-button {:on {:click #(swap! state/state assoc :modal-basic-open false)}}
       "Close"]]]]])
