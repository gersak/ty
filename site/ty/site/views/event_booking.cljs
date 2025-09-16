(ns ty.site.views.event-booking)

(defn view []
  [:div.ty-elevated.p-8.rounded-lg
   [:h1.text-2xl.font-bold.ty-text.mb-6 "Event Booking Scenario"]
   [:p.ty-text-.mb-8 "This will showcase interactive calendar, date selection, and complex component orchestration."]

   [:div.ty-bg-neutral-.p-6.rounded-lg.text-center
    [:ty-icon {:name "calendar"
               :size "3xl"
               :class "ty-text-success mb-4"}]
    [:h3.text-lg.font-semibold.ty-text.mb-2 "Coming Soon"]
    [:p.ty-text-.text-sm "Calendar-driven booking interface with time slots and options."]]])
