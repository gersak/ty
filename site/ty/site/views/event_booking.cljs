(ns ty.site.views.event-booking
  (:require [ty.site.state :as state]))

(defn view []
  [:div.max-w-6xl.mx-auto.space-y-8
   ;; Hero Section
   [:div.text-center.mb-12
    [:h1.text-4xl.font-bold.ty-text.mb-4 "Event Booking Scenario"]
    [:p.text-lg.ty-text-.max-w-4xl.mx-auto.leading-relaxed
     "Discover how calendar components orchestrate with complex forms to create seamless booking experiences. This scenario showcases date selection, time slot management, service customization, and confirmation workflows in a professional event booking interface."]

    [:div.flex.flex-wrap.gap-3.justify-center.mt-6
     [:span.px-3.py-1.ty-bg-success-.ty-text-success.rounded-full.text-sm.font-medium "Calendar Integration"]
     [:span.px-3.py-1.ty-bg-primary-.ty-text-primary.rounded-full.text-sm.font-medium "Time Slot Selection"]
     [:span.px-3.py-1.ty-bg-warning-.ty-text-warning.rounded-full.text-sm.font-medium "Service Options"]
     [:span.px-3.py-1.ty-bg-info-.ty-text-info.rounded-full.text-sm.font-medium "Confirmation Flow"]]]

   ;; Main Booking Interface
   [:div.grid.grid-cols-1.lg:grid-cols-3.gap-8
    ;; Left Column - Calendar and Date Selection
    [:div.lg:col-span-2.space-y-6
     [:div.ty-elevated.p-6.rounded-xl
      [:h2.text-xl.font-semibold.ty-text.mb-4 "📅 Select Your Date"]
      [:p.ty-text-.text-sm.mb-6 "Choose from available dates. Dates in green have full availability, yellow have limited slots, and unavailable dates are disabled."]

      ;; Calendar Component
      [:div.border.ty-border.rounded-lg.p-4
       [:ty-calendar-month {:class "w-full"}]]

      ;; Quick date buttons
      [:div.flex.flex-wrap.gap-2.mt-4
       [:button.px-3.py-2.ty-bg-primary-.ty-text-primary.rounded-md.text-sm.font-medium.hover:ty-bg-primary.hover:text-white.transition-colors
        "Today"]
       [:button.px-3.py-2.ty-bg-success-.ty-text-success.rounded-md.text-sm.font-medium.hover:ty-bg-success.hover:text-white.transition-colors
        "Tomorrow"]
       [:button.px-3.py-2.ty-bg-info-.ty-text-info.rounded-md.text-sm.font-medium.hover:ty-bg-info.hover:text-white.transition-colors
        "Next Week"]]]

     ;; Time Slot Selection
     [:div.ty-elevated.p-6.rounded-xl
      [:h2.text-xl.font-semibold.ty-text.mb-4 "⏰ Available Time Slots"]
      [:p.ty-text-.text-sm.mb-6 "Select your preferred time slot. All times are shown in Pacific Time (PST)."]

      [:div.grid.grid-cols-2.md:grid-cols-4.gap-3
       ;; Morning slots
       [:div.ty--content.p-3.rounded-lg.text-center.cursor-pointer.hover:ty--elevated.transition-colors.border.ty-border.hover:ty-border-primary
        [:div.text-sm.font-medium.ty-text "9:00 AM"]
        [:div.text-xs.ty-text-success "Available"]]

       [:div.ty--content.p-3.rounded-lg.text-center.cursor-pointer.hover:ty--elevated.transition-colors.border.ty-border.hover:ty-border-primary
        [:div.text-sm.font-medium.ty-text "10:00 AM"]
        [:div.text-xs.ty-text-success "Available"]]

       [:div.ty--content.p-3.rounded-lg.text-center.cursor-pointer.hover:ty--elevated.transition-colors.border.ty-border.hover:ty-border-primary
        [:div.text-sm.font-medium.ty-text "11:00 AM"]
        [:div.text-xs.ty-text-warning "2 spots left"]]

       [:div.ty--content.p-3.rounded-lg.text-center.opacity-50.cursor-not-allowed
        [:div.text-sm.font-medium.ty-text- "12:00 PM"]
        [:div.text-xs.ty-text-danger "Booked"]]

       ;; Afternoon slots
       [:div.ty--content.p-3.rounded-lg.text-center.cursor-pointer.hover:ty--elevated.transition-colors.border.ty-border.hover:ty-border-primary
        [:div.text-sm.font-medium.ty-text "1:00 PM"]
        [:div.text-xs.ty-text-success "Available"]]

       [:div.ty--content.p-3.rounded-lg.text-center.cursor-pointer.hover:ty--elevated.transition-colors.border.ty-border.hover:ty-border-primary.ty-bg-primary-.ty-border-primary
        [:div.text-sm.font-medium.ty-text-primary "2:00 PM"]
        [:div.text-xs.ty-text-primary "✓ Selected"]]

       [:div.ty--content.p-3.rounded-lg.text-center.cursor-pointer.hover:ty--elevated.transition-colors.border.ty-border.hover:ty-border-primary
        [:div.text-sm.font-medium.ty-text "3:00 PM"]
        [:div.text-xs.ty-text-warning "1 spot left"]]

       [:div.ty--content.p-3.rounded-lg.text-center.cursor-pointer.hover:ty--elevated.transition-colors.border.ty-border.hover:ty-border-primary
        [:div.text-sm.font-medium.ty-text "4:00 PM"]
        [:div.text-xs.ty-text-success "Available"]]]]

     ;; Service Selection
     [:div.ty-elevated.p-6.rounded-xl
      [:h2.text-xl.font-semibold.ty-text.mb-4 "🎯 Services & Add-ons"]
      [:p.ty-text-.text-sm.mb-6 "Customize your booking with additional services and amenities."]

      [:div.space-y-4
       ;; Base service selection
       [:div
        [:label.block.text-sm.font-medium.ty-text.mb-2 "Event Type"]
        [:ty-dropdown {:value "meeting"
                       :placeholder "Select event type"}
         [:ty-option {:value "meeting"}
          [:div.flex.items-center.gap-3
           [:div.w-8.h-8.ty-bg-primary.rounded-full.flex.items-center.justify-center
            [:ty-icon {:name "user"
                       :size "sm"
                       :class "text-white"}]]
           [:div.flex-1
            [:div.font-medium "Business Meeting"]
            [:div.text-xs.ty-text- "Professional meeting space • $50/hour"]]]]

         [:ty-option {:value "workshop"}
          [:div.flex.items-center.gap-3
           [:div.w-8.h-8.ty-bg-success.rounded-full.flex.items-center.justify-center
            [:ty-icon {:name "star"
                       :size "sm"
                       :class "text-white"}]]
           [:div.flex-1
            [:div.font-medium "Workshop/Training"]
            [:div.text-xs.ty-text- "Large space with presentation setup • $75/hour"]]]]

         [:ty-option {:value "conference"}
          [:div.flex.items-center.gap-3
           [:div.w-8.h-8.ty-bg-warning.rounded-full.flex.items-center.justify-center
            [:ty-icon {:name "globe"
                       :size "sm"
                       :class "text-white"}]]
           [:div.flex-1
            [:div.font-medium "Conference Room"]
            [:div.text-xs.ty-text- "Executive boardroom • $100/hour"]]]]

         [:ty-option {:value "event"}
          [:div.flex.items-center.gap-3
           [:div.w-8.h-8.ty-bg-secondary.rounded-full.flex.items-center.justify-center
            [:ty-icon {:name "star"
                       :size "sm"
                       :class "text-white"}]]
           [:div.flex-1
            [:div.font-medium "Special Event"]
            [:div.text-xs.ty-text- "Full event space with catering • $200/hour"]]]]]]

       ;; Add-on services multiselect
       [:div
        [:label.block.text-sm.font-medium.ty-text.mb-2 "Additional Services"]
        [:ty-multiselect {:placeholder "Select additional services and amenities..."}
         ;; Pre-selected services
         [:ty-tag {:value "av-equipment"
                   :flavor "primary"}
          [:div.flex.items-center.gap-2
           [:div
            [:span.text-xs.w-6.h-6.ty-bg-primary+.rounded.flex.items-center.justify-center "🎥"]]
           [:span "A/V Equipment"]
           [:span.text-xs.ty-text- "+$25"]]]

         [:ty-tag {:value "catering"
                   :flavor "success"}
          [:div.flex.items-center.gap-2
           [:div
            [:span.text-xs.w-6.h-6.ty-bg-success+.rounded.flex.items-center.justify-center "🍽️"]]
           [:span "Light Catering"]
           [:span.text-xs.ty-text- "+$15/person"]]]

         ;; Available options
         [:ty-tag {:value "wifi-upgrade"}
          [:div.flex.items-center.gap-3
           [:div
            [:span.text-xs.w-6.h-6.ty-bg-primary+.rounded.flex.items-center.justify-center "📡"]]
           [:div.flex-1
            [:div.font-medium "Premium Wi-Fi"]
            [:div.text-xs.ty-text- "High-speed dedicated connection • +$10"]]]]

         [:ty-tag {:value "parking"}
          [:div.flex.items-center.gap-3
           [:div
            [:span.ty-elevated.w-6.h-6.text-xs.flex.items-center.justify-center.rounded "🚗"]]
           [:div.flex-1
            [:div.font-medium "Reserved Parking"]
            [:div.text-xs.ty-text- "Guaranteed parking spots • +$5/spot"]]]]

         [:ty-tag {:value "security"}
          [:div.flex.items-center.gap-3
           [:div
            [:span.ty-floating.text-xs.w-6.h-6.ty-bg-danger+.rounded.flex.items-center.justify-center "🛡️"]]
           [:div.flex-1
            [:div.font-medium "Security Service"]
            [:div.text-xs.ty-text- "Professional security staff • +$50/hour"]]]]

         [:ty-tag {:value "recording"}
          [:div.flex.items-center.gap-3
           [:div
            [:span.text-xs.w-6.h-6.ty-bg-secondary+.rounded.flex.items-center.justify-center "📹"]]
           [:div.flex-1
            [:div.font-medium "Video Recording"]
            [:div.text-xs.ty-text- "Professional recording setup • +$75"]]]]

         [:ty-tag {:value "translation"}
          [:div.flex.items-center.gap-3
           [:div
            [:span.text-xs.w-6.h-6.ty-bg-secondary-.rounded.flex.items-center.justify-center "🌐"]]
           [:div.flex-1
            [:div.font-medium "Live Translation"]
            [:div.text-xs.ty-text- "Multi-language interpretation • +$100/language"]]]]]]]]]

    ;; Right Column - Booking Details & Summary
    [:div.space-y-6
     ;; Booking Details Form
     [:div.ty-elevated.p-6.rounded-xl
      [:h2.text-xl.font-semibold.ty-text.mb-4 "📝 Booking Details"]

      [:div.space-y-4
       ;; Duration
       [:div
        [:label.block.text-sm.font-medium.ty-text.mb-2 "Duration"]
        [:ty-dropdown {:value "60"
                       :placeholder "Select duration"}
         [:ty-option {:value "30"}
          [:div.flex.items-center.justify-between.w-full
           [:span "30 minutes"]
           [:span.text-xs.ty-text- "Quick meeting"]]]
         [:ty-option {:value "60"}
          [:div.flex.items-center.justify-between.w-full
           [:span "1 hour"]
           [:span.text-xs.ty-text- "Standard"]]]
         [:ty-option {:value "120"}
          [:div.flex.items-center.justify-between.w-full
           [:span "2 hours"]
           [:span.text-xs.ty-text- "Extended session"]]]
         [:ty-option {:value "240"}
          [:div.flex.items-center.justify-between.w-full
           [:span "4 hours"]
           [:span.text-xs.ty-text- "Half day"]]]
         [:ty-option {:value "480"}
          [:div.flex.items-center.justify-between.w-full
           [:span "8 hours"]
           [:span.text-xs.ty-text- "Full day"]]]]]

       ;; Attendee count
       [:div
        [:ty-input {:type "number"
                    :label "Number of Attendees"
                    :value "8"
                    :min "1"
                    :max "50"
                    :placeholder "Enter attendee count"}]]

       ;; Contact details
       [:div
        [:ty-input {:type "text"
                    :label "Contact Name"
                    :value "Sarah Wilson"
                    :placeholder "Primary contact name"
                    :required true}]]

       [:div
        [:ty-input {:type "email"
                    :label "Contact Email"
                    :value "sarah.wilson@company.com"
                    :placeholder "your@email.com"
                    :required true}]]

       ;; Special requests
       [:div
        [:ty-input {:type "textarea"
                    :label "Special Requests"
                    :placeholder "Any special requirements or requests for your booking..."
                    :rows "3"}]]]]

     ;; Booking Summary
     [:div.ty-elevated.p-6.rounded-xl
      [:h2.text-xl.font-semibold.ty-text.mb-4 "💰 Booking Summary"]

      [:div.space-y-3
       ;; Selected items
       [:div.flex.justify-between.items-center
        [:span.text-sm.ty-text "Business Meeting (2:00 PM - 3:00 PM)"]
        [:span.text-sm.font-medium.ty-text "$50.00"]]

       [:div.flex.justify-between.items-center
        [:span.text-sm.ty-text "A/V Equipment"]
        [:span.text-sm.font-medium.ty-text "$25.00"]]

       [:div.flex.justify-between.items-center
        [:span.text-sm.ty-text "Light Catering (8 people)"]
        [:span.text-sm.font-medium.ty-text "$120.00"]]

       ;; Divider
       [:div.border-t.ty-border.my-3]

       ;; Totals
       [:div.flex.justify-between.items-center
        [:span.text-sm.ty-text- "Subtotal"]
        [:span.text-sm.ty-text- "$195.00"]]

       [:div.flex.justify-between.items-center
        [:span.text-sm.ty-text- "Tax (8.75%)"]
        [:span.text-sm.ty-text- "$17.06"]]

       [:div.flex.justify-between.items-center.pt-2.border-t.ty-border
        [:span.font-semibold.ty-text "Total"]
        [:span.font-semibold.text-lg.ty-text-primary "$212.06"]]]

      ;; Booking button
      [:ty-button {:flavor "primary"
                   :class "w-full mt-6"
                   :on {:click #(swap! state/state assoc-in [:event-booking :confirmation-modal-open] true)}}
       [:ty-icon {:name "check"
                  :class "mr-2"}]
       "Confirm Booking"]

      ;; Additional info
      [:div.mt-4.p-3.ty-bg-info-.rounded-lg
       [:div.flex.items-start.gap-2
        [:ty-icon {:name "info"
                   :size "sm"
                   :class "ty-text-info mt-0.5"}]
        [:div.text-xs.ty-text-info
         [:p.font-medium.mb-1 "Booking Policy"]
         [:p "Free cancellation up to 24 hours before your event. Full refund available for cancellations made 48 hours in advance."]]]]]]]

   ;; Feature Showcase
   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-4.gap-6.mt-12
    ;; Calendar Integration
    [:div.ty-elevated.p-6.rounded-lg
     [:div.w-12.h-12.ty-bg-success.rounded-lg.flex.items-center.justify-center.mb-4
      [:ty-icon {:name "calendar"
                 :size "lg"
                 :class "text-white"}]]
     [:h3.font-semibold.ty-text.mb-2 "Calendar-Driven Interface"]
     [:p.text-sm.ty-text- "Interactive calendar component with availability indicators and date restrictions for seamless selection."]]

    ;; Time Slot Management
    [:div.ty-elevated.p-6.rounded-lg
     [:div.w-12.h-12.ty-bg-primary.rounded-lg.flex.items-center.justify-center.mb-4
      [:ty-icon {:name "clock"
                 :size "lg"
                 :class "text-white"}]]
     [:h3.font-semibold.ty-text.mb-2 "Smart Time Slots"]
     [:p.text-sm.ty-text- "Visual time slot selection with real-time availability and clear status indicators for optimal UX."]]

    ;; Service Customization
    [:div.ty-elevated.p-6.rounded-lg
     [:div.w-12.h-12.ty-bg-warning.rounded-lg.flex.items-center.justify-center.mb-4
      [:ty-icon {:name "settings"
                 :size "lg"
                 :class "text-white"}]]
     [:h3.font-semibold.ty-text.mb-2 "Service Configuration"]
     [:p.text-sm.ty-text- "Rich dropdown and multiselect components for customizing services with pricing and visual indicators."]]

    ;; Booking Flow
    [:div.ty-elevated.p-6.rounded-lg
     [:div.w-12.h-12.ty-bg-info.rounded-lg.flex.items-center.justify-center.mb-4
      [:ty-icon {:name "check-circle"
                 :size "lg"
                 :class "text-white"}]]
     [:h3.font-semibold.ty-text.mb-2 "Confirmation Flow"]
     [:p.text-sm.ty-text- "Professional booking summary with dynamic pricing and modal confirmation for completed transactions."]]]

   ;; Booking Confirmation Modal
   [:ty-modal {:open (get-in @state/state [:event-booking :confirmation-modal-open] false)
               :on {:ty-modal-close #(swap! state/state assoc-in [:event-booking :confirmation-modal-open] false)}}
    [:div.p-8.max-w-2xl.ty-elevated.rounded-lg.shadow-xl
     ;; Success header
     [:div.text-center.mb-8
      [:div.w-16.h-16.ty-bg-success.rounded-full.flex.items-center.justify-center.mx-auto.mb-4
       [:ty-icon {:name "check"
                  :size "xl"
                  :class "text-white"}]]
      [:h3.text-2xl.font-bold.ty-text.mb-2 "Booking Confirmed!"]
      [:p.ty-text- "Your event has been successfully scheduled. Confirmation details have been sent to your email."]]

     ;; Booking details
     [:div.ty-content.p-6.rounded-lg.mb-6
      [:h4.font-semibold.ty-text.mb-4 "Booking Details"]

      [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.text-sm
       [:div
        [:div.ty-text-.mb-1 "Event Type"]
        [:div.font-medium.ty-text "Business Meeting"]]
       [:div
        [:div.ty-text-.mb-1 "Date & Time"]
        [:div.font-medium.ty-text "Today, 2:00 PM - 3:00 PM"]]
       [:div
        [:div.ty-text-.mb-1 "Duration"]
        [:div.font-medium.ty-text "1 hour"]]
       [:div
        [:div.ty-text-.mb-1 "Attendees"]
        [:div.font-medium.ty-text "8 people"]]
       [:div
        [:div.ty-text-.mb-1 "Contact"]
        [:div.font-medium.ty-text "Sarah Wilson"]]
       [:div
        [:div.ty-text-.mb-1 "Total Cost"]
        [:div.font-medium.ty-text-primary.text-lg "$212.06"]]]

      ;; Services
      [:div.mt-4
       [:div.ty-text-.mb-2 "Selected Services"]
       [:div.flex.flex-wrap.gap-2
        [:span.px-2.py-1.ty-bg-primary-.ty-text-primary.rounded.text-xs.font-medium "A/V Equipment"]
        [:span.px-2.py-1.ty-bg-success-.ty-text-success.rounded.text-xs.font-medium "Light Catering"]]]]

     ;; Booking reference
     [:div.p-4.ty-bg-info-.border.ty-border-info.rounded-lg.mb-6
      [:div.flex.items-center.gap-3
       [:ty-icon {:name "info"
                  :class "ty-text-info"}]
       [:div.flex-1
        [:div.font-medium.ty-text-info "Booking Reference"]
        [:div.text-sm.ty-text-info.font-mono "BK-2024-001234"]]]]

     ;; Action buttons
     [:div.flex.gap-3.justify-end
      [:ty-button {:flavor "neutral"
                   :on {:click #(swap! state/state assoc-in [:event-booking :confirmation-modal-open] false)}}
       "Close"]
      [:ty-button {:flavor "secondary"}
       [:ty-icon {:name "download"
                  :class "mr-2"}]
       "Download Receipt"]
      [:ty-button {:flavor "primary"}
       [:ty-icon {:name "calendar"
                  :class "mr-2"}]
       "Add to Calendar"]]]]])
