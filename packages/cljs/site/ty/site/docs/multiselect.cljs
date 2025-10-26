(ns ty.site.docs.multiselect
  "Documentation for ty-multiselect component"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]
            [ty.site.state :as state]))

(defn multiselect-event-handler [event]
  (let [detail (.-detail event)
        values (js->clj (.-values detail) :keywordize-keys false)
        action (.-action detail)
        item (.-item detail)]
    (js/console.log "Multiselect changed:"
                    (clj->js {:values values
                              :action action
                              :item item}))))

(defn- render-hero-section []
  [:div.mb-8
   [:h1.text-3xl.font-bold.ty-text++.mb-2 "ty-multiselect"]
   [:p.text-lg.ty-text-
    "A powerful multiselect component that allows selecting multiple options with tag-based display. "
    "Built on the dropdown foundation with automatic tag management, search filtering, and semantic styling. "
    "Uses ty-tag elements to display selected items as dismissible chips."]])

(defn- render-api-reference []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2#attributes.text-2xl.font-bold.ty-text++.mb-4 "API Reference"]
   (attribute-table
    [{:name "value"
      :type "string"
      :default "null"
      :description "Comma-separated selected values (e.g., \"red,blue,green\")"}
     {:name "placeholder"
      :type "string"
      :default "null"
      :description "Placeholder text when no options are selected"}
     {:name "label"
      :type "string"
      :default "null"
      :description "Label displayed above the multiselect"}
     {:name "name"
      :type "string"
      :default "null"
      :description "Name for form submission (creates multiple entries in FormData)"}
     {:name "disabled"
      :type "boolean"
      :default "false"
      :description "Whether the multiselect is disabled"}
     {:name "readonly"
      :type "boolean"
      :default "false"
      :description "Whether the multiselect is read-only (can view but not change selections)"}
     {:name "required"
      :type "boolean"
      :default "false"
      :description "Whether the multiselect is required (shows asterisk)"}
     {:name "searchable"
      :type "boolean"
      :default "true"
      :description "Enable built-in search filtering. Use 'not-searchable' attribute to disable. When disabled, fires 'search' events for external search."}
     {:name "size"
      :type "string"
      :default "'md'"
      :description "Component size: 'sm', 'md', 'lg'"}
     {:name "flavor"
      :type "string"
      :default "'neutral'"
      :description "Semantic flavor: primary, secondary, success, danger, warning, neutral"}
     {:name "delay"
      :type "number"
      :default "0"
      :description "Debounce delay in milliseconds for search events (0-5000ms). Only applies when searchable=false."}
     {:name "clearable"
      :type "boolean"
      :default "false"
      :description "Show clear all button when items are selected"}
     {:name "selected-label"
      :type "string"
      :default "'Selected'"
      :description "Label for selected section in mobile mode"}
     {:name "available-label"
      :type "string"
      :default "'Available'"
      :description "Label for available options section in mobile mode"}
     {:name "no-selection-message"
      :type "string"
      :default "'No items selected'"
      :description "Message shown when no items are selected (mobile mode)"}
     {:name "no-options-message"
      :type "string"
      :default "'No options available'"
      :description "Message shown when no options are available (mobile mode)"}
     {:name "class"
      :type "string"
      :default "null"
      :description "Additional CSS classes"}])
   [:div.mt-6
    [:h3.text-lg.font-semibold.ty-text++.mb-2 "Events"]
    (event-table
     [{:name "change"
       :when-fired "Fired when selections change"
       :payload "{values: string[], action: 'add'|'remove'|'clear'|'set', item: string|null}"}
      {:name "search"
       :when-fired "Fired when user types in search (only when searchable=false for external search)"
       :payload "{query: string, element: HTMLElement}"}
      {:name "focus"
       :when-fired "Fired when multiselect gains focus"
       :payload "Standard FocusEvent"}
      {:name "blur"
       :when-fired "Fired when multiselect loses focus"
       :payload "Standard FocusEvent"}])]
   [:div.mt-4
    [:h3.text-lg.font-semibold.ty-text++.mb-2 "Slots"]
    [:div.ty-bg-neutral-.rounded.p-4
     [:p.text-sm.ty-text- "Content slot accepts " [:code.ty-bg-neutral.px-1.rounded "ty-tag"] " elements:"]
     [:pre.text-xs.ty-text-.mt-2
      [:code.hljs.language-html "<!-- Use ty-tag elements as options -->\n<ty-tag value=\"red\" pill size=\"sm\">Red</ty-tag>\n<ty-tag value=\"blue\" pill size=\"sm\">Blue</ty-tag>"]]]
    [:div.ty-bg-neutral-.rounded.p-4.mt-2
     [:p.text-sm.ty-text- "Selected slot (automatic): Selected tags are moved here with dismiss functionality:"]
     [:pre.text-xs.ty-text-.mt-2
      [:code.hljs.language-html "<!-- Tags automatically get these attributes when selected -->\n<ty-tag slot=\"selected\" selected dismissible>Selected Tag</ty-tag>"]]]]])

(defn- render-basic-usage-examples []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Basic Usage"]
   [:div.mb-6
    [:h3.text-lg.font-semibold.ty-text+.mb-2 "Large Dataset (100 Options, 10 Selected)"]
    [:p.ty-text-.mb-2 "Test multiselect performance with 100 available options and 10 pre-selected items. Search is enabled by default for easy filtering."]
    [:div.mb-4
     [:ty-multiselect {:placeholder "Search from 100 countries..."
                       :value "usa,canada,mexico,brazil,argentina,uk,france,germany,japan,australia"
                       :label "Select Countries"
                       :clearable true
                       :style {:min-width "320px"}
                       :on {:change multiselect-event-handler}}
      [:ty-tag {:value "usa" :pill true :size "sm"} "🇺🇸 United States"]
      [:ty-tag {:value "canada" :pill true :size "sm"} "🇨🇦 Canada"]
      [:ty-tag {:value "mexico" :pill true :size "sm"} "🇲🇽 Mexico"]
      [:ty-tag {:value "brazil" :pill true :size "sm"} "🇧🇷 Brazil"]
      [:ty-tag {:value "argentina" :pill true :size "sm"} "🇦🇷 Argentina"]
      [:ty-tag {:value "chile" :pill true :size "sm"} "🇨🇱 Chile"]
      [:ty-tag {:value "colombia" :pill true :size "sm"} "🇨🇴 Colombia"]
      [:ty-tag {:value "peru" :pill true :size "sm"} "🇵🇪 Peru"]
      [:ty-tag {:value "venezuela" :pill true :size "sm"} "🇻🇪 Venezuela"]
      [:ty-tag {:value "ecuador" :pill true :size "sm"} "🇪🇨 Ecuador"]
      [:ty-tag {:value "uk" :pill true :size "sm"} "🇬🇧 United Kingdom"]
      [:ty-tag {:value "france" :pill true :size "sm"} "🇫🇷 France"]
      [:ty-tag {:value "germany" :pill true :size "sm"} "🇩🇪 Germany"]
      [:ty-tag {:value "italy" :pill true :size "sm"} "🇮🇹 Italy"]
      [:ty-tag {:value "spain" :pill true :size "sm"} "🇪🇸 Spain"]
      [:ty-tag {:value "portugal" :pill true :size "sm"} "🇵🇹 Portugal"]
      [:ty-tag {:value "netherlands" :pill true :size "sm"} "🇳🇱 Netherlands"]
      [:ty-tag {:value "belgium" :pill true :size "sm"} "🇧🇪 Belgium"]
      [:ty-tag {:value "switzerland" :pill true :size "sm"} "🇨🇭 Switzerland"]
      [:ty-tag {:value "austria" :pill true :size "sm"} "🇦🇹 Austria"]
      [:ty-tag {:value "sweden" :pill true :size "sm"} "🇸🇪 Sweden"]
      [:ty-tag {:value "norway" :pill true :size "sm"} "🇳🇴 Norway"]
      [:ty-tag {:value "denmark" :pill true :size "sm"} "🇩🇰 Denmark"]
      [:ty-tag {:value "finland" :pill true :size "sm"} "🇫🇮 Finland"]
      [:ty-tag {:value "poland" :pill true :size "sm"} "🇵🇱 Poland"]
      [:ty-tag {:value "czech" :pill true :size "sm"} "🇨🇿 Czech Republic"]
      [:ty-tag {:value "hungary" :pill true :size "sm"} "🇭🇺 Hungary"]
      [:ty-tag {:value "greece" :pill true :size "sm"} "🇬🇷 Greece"]
      [:ty-tag {:value "turkey" :pill true :size "sm"} "🇹🇷 Turkey"]
      [:ty-tag {:value "russia" :pill true :size "sm"} "🇷🇺 Russia"]
      [:ty-tag {:value "japan" :pill true :size "sm"} "🇯🇵 Japan"]
      [:ty-tag {:value "china" :pill true :size "sm"} "🇨🇳 China"]
      [:ty-tag {:value "korea" :pill true :size "sm"} "🇰🇷 South Korea"]
      [:ty-tag {:value "india" :pill true :size "sm"} "🇮🇳 India"]
      [:ty-tag {:value "indonesia" :pill true :size "sm"} "🇮🇩 Indonesia"]
      [:ty-tag {:value "thailand" :pill true :size "sm"} "🇹🇭 Thailand"]
      [:ty-tag {:value "vietnam" :pill true :size "sm"} "🇻🇳 Vietnam"]
      [:ty-tag {:value "philippines" :pill true :size "sm"} "🇵🇭 Philippines"]
      [:ty-tag {:value "malaysia" :pill true :size "sm"} "🇲🇾 Malaysia"]
      [:ty-tag {:value "singapore" :pill true :size "sm"} "🇸🇬 Singapore"]
      [:ty-tag {:value "australia" :pill true :size "sm"} "🇦🇺 Australia"]
      [:ty-tag {:value "newzealand" :pill true :size "sm"} "🇳🇿 New Zealand"]
      [:ty-tag {:value "southafrica" :pill true :size "sm"} "🇿🇦 South Africa"]
      [:ty-tag {:value "egypt" :pill true :size "sm"} "🇪🇬 Egypt"]
      [:ty-tag {:value "nigeria" :pill true :size "sm"} "🇳🇬 Nigeria"]
      [:ty-tag {:value "kenya" :pill true :size "sm"} "🇰🇪 Kenya"]
      [:ty-tag {:value "morocco" :pill true :size "sm"} "🇲🇦 Morocco"]
      [:ty-tag {:value "algeria" :pill true :size "sm"} "🇩🇿 Algeria"]
      [:ty-tag {:value "tunisia" :pill true :size "sm"} "🇹🇳 Tunisia"]
      [:ty-tag {:value "israel" :pill true :size "sm"} "🇮🇱 Israel"]
      [:ty-tag {:value "uae" :pill true :size "sm"} "🇦🇪 UAE"]
      [:ty-tag {:value "saudi" :pill true :size "sm"} "🇸🇦 Saudi Arabia"]
      [:ty-tag {:value "qatar" :pill true :size "sm"} "🇶🇦 Qatar"]
      [:ty-tag {:value "kuwait" :pill true :size "sm"} "🇰🇼 Kuwait"]
      [:ty-tag {:value "iraq" :pill true :size "sm"} "🇮🇶 Iraq"]
      [:ty-tag {:value "iran" :pill true :size "sm"} "🇮🇷 Iran"]
      [:ty-tag {:value "pakistan" :pill true :size "sm"} "🇵🇰 Pakistan"]
      [:ty-tag {:value "bangladesh" :pill true :size "sm"} "🇧🇩 Bangladesh"]
      [:ty-tag {:value "srilanka" :pill true :size "sm"} "🇱🇰 Sri Lanka"]
      [:ty-tag {:value "myanmar" :pill true :size "sm"} "🇲🇲 Myanmar"]
      [:ty-tag {:value "cambodia" :pill true :size "sm"} "🇰🇭 Cambodia"]
      [:ty-tag {:value "laos" :pill true :size "sm"} "🇱🇦 Laos"]
      [:ty-tag {:value "mongolia" :pill true :size "sm"} "🇲🇳 Mongolia"]
      [:ty-tag {:value "nepal" :pill true :size "sm"} "🇳🇵 Nepal"]
      [:ty-tag {:value "bhutan" :pill true :size "sm"} "🇧🇹 Bhutan"]
      [:ty-tag {:value "taiwan" :pill true :size "sm"} "🇹🇼 Taiwan"]
      [:ty-tag {:value "hongkong" :pill true :size "sm"} "🇭🇰 Hong Kong"]
      [:ty-tag {:value "macao" :pill true :size "sm"} "🇲🇴 Macao"]
      [:ty-tag {:value "afghanistan" :pill true :size "sm"} "🇦🇫 Afghanistan"]
      [:ty-tag {:value "kazakhstan" :pill true :size "sm"} "🇰🇿 Kazakhstan"]
      [:ty-tag {:value "uzbekistan" :pill true :size "sm"} "🇺🇿 Uzbekistan"]
      [:ty-tag {:value "turkmenistan" :pill true :size "sm"} "🇹🇲 Turkmenistan"]
      [:ty-tag {:value "kyrgyzstan" :pill true :size "sm"} "🇰🇬 Kyrgyzstan"]
      [:ty-tag {:value "tajikistan" :pill true :size "sm"} "🇹🇯 Tajikistan"]
      [:ty-tag {:value "azerbaijan" :pill true :size "sm"} "🇦🇿 Azerbaijan"]
      [:ty-tag {:value "georgia" :pill true :size "sm"} "🇬🇪 Georgia"]
      [:ty-tag {:value "armenia" :pill true :size "sm"} "🇦🇲 Armenia"]
      [:ty-tag {:value "ukraine" :pill true :size "sm"} "🇺🇦 Ukraine"]
      [:ty-tag {:value "belarus" :pill true :size "sm"} "🇧🇾 Belarus"]
      [:ty-tag {:value "moldova" :pill true :size "sm"} "🇲🇩 Moldova"]
      [:ty-tag {:value "romania" :pill true :size "sm"} "🇷🇴 Romania"]
      [:ty-tag {:value "bulgaria" :pill true :size "sm"} "🇧🇬 Bulgaria"]
      [:ty-tag {:value "serbia" :pill true :size "sm"} "🇷🇸 Serbia"]
      [:ty-tag {:value "croatia" :pill true :size "sm"} "🇭🇷 Croatia"]
      [:ty-tag {:value "slovenia" :pill true :size "sm"} "🇸🇮 Slovenia"]
      [:ty-tag {:value "bosnia" :pill true :size "sm"} "🇧🇦 Bosnia"]
      [:ty-tag {:value "macedonia" :pill true :size "sm"} "🇲🇰 Macedonia"]
      [:ty-tag {:value "albania" :pill true :size "sm"} "🇦🇱 Albania"]
      [:ty-tag {:value "montenegro" :pill true :size "sm"} "🇲🇪 Montenegro"]
      [:ty-tag {:value "kosovo" :pill true :size "sm"} "🇽🇰 Kosovo"]
      [:ty-tag {:value "lithuania" :pill true :size "sm"} "🇱🇹 Lithuania"]
      [:ty-tag {:value "latvia" :pill true :size "sm"} "🇱🇻 Latvia"]
      [:ty-tag {:value "estonia" :pill true :size "sm"} "🇪🇪 Estonia"]
      [:ty-tag {:value "iceland" :pill true :size "sm"} "🇮🇸 Iceland"]
      [:ty-tag {:value "ireland" :pill true :size "sm"} "🇮🇪 Ireland"]
      [:ty-tag {:value "luxembourg" :pill true :size "sm"} "🇱🇺 Luxembourg"]
      [:ty-tag {:value "malta" :pill true :size "sm"} "🇲🇹 Malta"]
      [:ty-tag {:value "cyprus" :pill true :size "sm"} "🇨🇾 Cyprus"]]]
    (code-block "<!-- Large dataset: 100 options, 10 pre-selected -->
<ty-multiselect 
  value=\"usa,canada,mexico,brazil,argentina,uk,france,germany,japan,australia\"
  label=\"Select Countries\"
  placeholder=\"Search from 100 countries...\"
  clearable
  style=\"min-width: 320px;\">
  <ty-tag value=\"usa\" pill size=\"sm\">🇺🇸 United States</ty-tag>
  <ty-tag value=\"canada\" pill size=\"sm\">🇨🇦 Canada</ty-tag>
  <ty-tag value=\"mexico\" pill size=\"sm\">🇲🇽 Mexico</ty-tag>
  <!-- ... 97 more countries ... -->
</ty-multiselect>

<!-- Performance Notes:
  - Built-in search filters 100 options smoothly
  - Selected tags (10) display as dismissible chips
  - Clearable button allows removing all selections at once
  - Desktop: Smart positioned dropdown
  - Mobile: Full-screen modal with sections
-->")]
   [:div.mb-6
    [:h3.text-lg.font-semibold.ty-text+.mb-2 "With Initial Values"]
    [:p.ty-text-.mb-2 "Use the " [:code.ty-bg-neutral-.px-2.py-1.rounded.text-sm "value"] " attribute with comma-separated values to pre-select options:"]
    [:div.mb-4
     [:ty-multiselect {:value #js ["javascript" "python"]
                       :label "Programming Languages"
                       :placeholder "Select languages..."
                       :style {:min-width "320px"}
                       :on {:change multiselect-event-handler}}
      [:ty-tag {:value "javascript"
                :pill true
                :size "sm"
                :flavor "warning"}
       [:div.flex.items-center.gap-1.5
        [:div.w-5.h-5.rounded.bg-yellow-400.flex.items-center.justify-center.rounded-full
         [:span.text-xs.font-bold.text-black "JS"]]
        "JavaScript"]]
      [:ty-tag {:value "python"
                :pill true
                :size "sm"
                :flavor "primary"}
       [:div.flex.items-center.gap-1.5
        [:div.w-5.h-5.rounded.bg-blue-500.flex.items-center.justify-center.rounded-full
         [:span.text-xs.font-bold.text-white "🐍"]]
        "Python"]]
      [:ty-tag {:value "java"
                :pill true
                :size "sm"
                :flavor "danger"}
       [:div.flex.items-center.gap-1.5
        [:div.w-5.h-5.rounded.bg-red-600.flex.items-center.justify-center.rounded-full
         [:span.text-xs.font-bold.text-white "☕"]]
        "Java"]]
      [:ty-tag {:value "clojure"
                :pill true
                :size "sm"
                :flavor "success"}
       [:div.flex.items-center.gap-1.5
        [:div.w-5.h-5.rounded.bg-green-600.flex.items-center.justify-center.rounded-full
         [:span.text-xs.font-bold.text-white "λ"]]
        "Clojure"]]
      [:ty-tag {:value "rust"
                :pill true
                :size "sm"
                :flavor "warning"}
       [:div.flex.items-center.gap-1.5
        [:div.w-5.h-5.rounded.bg-orange-500.flex.items-center.justify-center.rounded-full
         [:span.text-xs.font-bold.text-white "🦀"]]
        "Rust"]]
      [:ty-tag {:value "go"
                :pill true
                :size "sm"
                :flavor "info"}
       [:div.flex.items-center.gap-1.5
        [:div.w-5.h-5.rounded.bg-cyan-500.flex.items-center.justify-center.rounded-full
         [:span.text-xs.font-bold.text-white "Go"]]
        "Go"]]]]
    (code-block "<ty-multiselect value=\"javascript,python\" label=\"Programming Languages\" placeholder=\"Select languages...\">
  <ty-tag value=\"javascript\" pill size=\"sm\">JavaScript</ty-tag>
  <ty-tag value=\"python\" pill size=\"sm\">Python</ty-tag>
  <ty-tag value=\"java\" pill size=\"sm\">Java</ty-tag>
  <ty-tag value=\"clojure\" pill size=\"sm\">Clojure</ty-tag>
  <ty-tag value=\"rust\" pill size=\"sm\">Rust</ty-tag>
  <ty-tag value=\"go\" pill size=\"sm\">Go</ty-tag>
</ty-multiselect>")]])

(defn- render-semantic-flavor-examples []
  [:div
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Semantic Flavors"]
   [:p.ty-text-.mb-4 "Use semantic flavors to convey meaning. Both the dropdown border and selected tags inherit the flavor styling."]
   [:div.space-y-8
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text++.mb-4 "Success (Approved Items)"]
     [:div.mb-4
      [:ty-multiselect {:flavor "success"
                        :value "approved,verified"
                        :label "Status"
                        :placeholder "Select approved items..."
                        :style {:min-width "280px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "approved"
                 :pill true
                 :size "sm"
                 :flavor "success"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "check-circle"
                    :size "12"
                    :class "ty-text-success++"}]
         "Approved"]]
       [:ty-tag {:value "verified"
                 :pill true
                 :size "sm"
                 :flavor "success"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "shield"
                    :size "12"
                    :class "ty-text-success++"}]
         "Verified"]]
       [:ty-tag {:value "confirmed"
                 :pill true
                 :size "sm"
                 :flavor "success"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "check"
                    :size "12"
                    :class "ty-text-success++"}]
         "Confirmed"]]
       [:ty-tag {:value "validated"
                 :pill true
                 :size "sm"
                 :flavor "success"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "star"
                    :size "12"
                    :class "ty-text-success++"}]
         "Validated"]]]]
     (code-block "<ty-multiselect flavor=\"success\" value=\"approved,verified\" placeholder=\"Select approved items...\">
  <ty-tag value=\"approved\" pill size=\"sm\" flavor=\"success\">✓ Approved</ty-tag>
  <ty-tag value=\"verified\" pill size=\"sm\" flavor=\"success\">✓ Verified</ty-tag>
  <ty-tag value=\"confirmed\" pill size=\"sm\" flavor=\"success\">✓ Confirmed</ty-tag>
  <ty-tag value=\"validated\" pill size=\"sm\" flavor=\"success\">✓ Validated</ty-tag>
</ty-multiselect>")]
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text++.mb-4 "Danger (Error States)"]
     [:div.mb-4
      [:ty-multiselect {:flavor "danger"
                        :value "error,timeout"
                        :label "System Issues"
                        :placeholder "Select error types..."
                        :style {:min-width "320px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "error"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "x-circle"
                    :size "12"
                    :class "ty-text-danger++"}]
         "Critical Error"]]
       [:ty-tag {:value "failed"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "alert-triangle"
                    :size "12"
                    :class "ty-text-danger++"}]
         "Process Failed"]]
       [:ty-tag {:value "rejected"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "shield"
                    :size "12"
                    :class "ty-text-danger++"}]
         "Access Rejected"]]
       [:ty-tag {:value "invalid"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "alert-circle"
                    :size "12"
                    :class "ty-text-danger++"}]
         "Invalid Data"]]
       [:ty-tag {:value "timeout"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "clock"
                    :size "12"
                    :class "ty-text-danger++"}]
         "Timeout"]]
       [:ty-tag {:value "offline"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "wifi-off"
                    :size "12"
                    :class "ty-text-danger++"}]
         "Connection Lost"]]]]
     (code-block "<ty-multiselect flavor=\"danger\" value=\"error,failed\" placeholder=\"Select problem areas...\">
  <ty-tag value=\"error\" pill size=\"sm\" flavor=\"danger\">✗ Error</ty-tag>
  <ty-tag value=\"failed\" pill size=\"sm\" flavor=\"danger\">✗ Failed</ty-tag>
  <ty-tag value=\"rejected\" pill size=\"sm\" flavor=\"danger\">✗ Rejected</ty-tag>
  <ty-tag value=\"invalid\" pill size=\"sm\" flavor=\"danger\">✗ Invalid</ty-tag>
</ty-multiselect>")]]])

(defn- render-component-state-examples []
  [:div
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Component States"]
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:div.mb-6
     [:h3.text-xl.font-semibold.ty-text++.mb-2 "Required Field"]
     [:div.mb-4
      [:ty-multiselect {:label "Required Skills"
                        :required true
                        :placeholder "You must select at least one skill..."
                        :style {:min-width "320px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "frontend"
                 :size "sm"} "Frontend"]
       [:ty-tag {:value "backend"
                 :size "sm"} "Backend"]
       [:ty-tag {:value "fullstack"
                 :size "sm"} "Full Stack"]
       [:ty-tag {:value "devops"
                 :size "sm"} "DevOps"]
       [:ty-tag {:value "design"
                 :size "sm"} "Design"]]]
     (code-block "<ty-multiselect label=\"Required Skills\" required placeholder=\"You must select at least one skill...\">
  <ty-tag value=\"frontend\" pill size=\"sm\">Frontend</ty-tag>
  <ty-tag value=\"backend\" pill size=\"sm\">Backend</ty-tag>
  <ty-tag value=\"fullstack\" pill size=\"sm\">Full Stack</ty-tag>
  <ty-tag value=\"devops\" pill size=\"sm\">DevOps</ty-tag>
  <ty-tag value=\"design\" pill size=\"sm\">Design</ty-tag>
</ty-multiselect>")]
    [:div.mb-6
     [:h3.text-xl.font-semibold.ty-text++.mb-2 "Disabled State"]
     [:div.mb-4
      [:ty-multiselect {:value "option1,option2"
                        :disabled true
                        :label "Disabled Multiselect"
                        :placeholder "Cannot be changed"
                        :style {:min-width "280px"}}
       [:ty-tag {:value "option1"
                 :size "sm"} "Option 1"]
       [:ty-tag {:value "option2"
                 :size "sm"} "Option 2"]
       [:ty-tag {:value "option3"
                 :size "sm"} "Option 3"]]]
     (code-block "<ty-multiselect value=\"option1,option2\" disabled label=\"Disabled Multiselect\">
  <ty-tag value=\"option1\" pill size=\"sm\">Option 1</ty-tag>
  <ty-tag value=\"option2\" pill size=\"sm\">Option 2</ty-tag>
  <ty-tag value=\"option3\" pill size=\"sm\">Option 3</ty-tag>
</ty-multiselect>")]
    [:div
     [:h3.text-xl.font-semibold.ty-text++.mb-2 "With Clear All Button"]
     [:p.ty-text-.mb-2 "Enable " [:code.ty-bg-neutral-.px-2.py-1.rounded.text-sm "clearable"] " to show a clear button when items are selected:"]
     [:div.mb-4
      [:ty-multiselect {:value "feature1,feature2,feature3"
                        :clearable true
                        :label "Features (Clearable)"
                        :placeholder "Select features..."
                        :style {:min-width "320px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "feature1"
                 :size "sm"} "Feature 1"]
       [:ty-tag {:value "feature2"
                 :size "sm"} "Feature 2"]
       [:ty-tag {:value "feature3"
                 :size "sm"} "Feature 3"]
       [:ty-tag {:value "feature4"
                 :size "sm"} "Feature 4"]
       [:ty-tag {:value "feature5"
                 :size "sm"} "Feature 5"]]]
     (code-block "<ty-multiselect value=\"feature1,feature2,feature3\" clearable label=\"Features (Clearable)\">
  <ty-tag value=\"feature1\" pill size=\"sm\">Feature 1</ty-tag>
  <ty-tag value=\"feature2\" pill size=\"sm\">Feature 2</ty-tag>
  <ty-tag value=\"feature3\" pill size=\"sm\">Feature 3</ty-tag>
  <ty-tag value=\"feature4\" pill size=\"sm\">Feature 4</ty-tag>
  <ty-tag value=\"feature5\" pill size=\"sm\">Feature 5</ty-tag>
</ty-multiselect>")]]])

(defn- render-form-integration-example []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Form Integration"]
   [:p.ty-text-.mb-4
    "ty-multiselect participates in form data collection using ElementInternals. "
    "Selected values are added to FormData as multiple entries with the same name (HTML standard)."]
   [:div.mb-6
    [:h3.text-xl.font-semibold.ty-text++.mb-2 "HTMX & FormData Example"]
    [:form#multiselect-form-demo.space-y-4.p-4.border.ty-border.rounded
     [:ty-multiselect {:name "categories"
                       :label "Categories"
                       :placeholder "Select categories..."
                       :value "tech,design"
                       :style {:min-width "280px"}
                       :on {:change multiselect-event-handler}}
      [:ty-tag {:value "tech"
                :size "sm"} "Technology"]
      [:ty-tag {:value "design"
                :size "sm"} "Design"]
      [:ty-tag {:value "business"
                :size "sm"} "Business"]
      [:ty-tag {:value "marketing"
                :size "sm"} "Marketing"]
      [:ty-tag {:value "finance"
                :size "sm"} "Finance"]]
     [:ty-multiselect {:name "skills"
                       :label "Skills"
                       :placeholder "Select skills..."
                       :style {:min-width "280px"}
                       :on {:change multiselect-event-handler}}
      [:ty-tag {:value "javascript"
                :size "sm"} "JavaScript"]
      [:ty-tag {:value "python"
                :size "sm"} "Python"]
      [:ty-tag {:value "clojure"
                :size "sm"} "Clojure"]
      [:ty-tag {:value "react"
                :size "sm"} "React"]
      [:ty-tag {:value "css"
                :size "sm"} "CSS"]]
     [:ty-button {:flavor "primary"
                  :on {:click #(let [form (.getElementById js/document "multiselect-form-demo")
                                     form-data (js/FormData. form)
                                     results (.getElementById js/document "multiselect-form-results")
                                     entries (js/Array.from (.entries form-data))]
                                 (set! (.-innerHTML results)
                                       (str "<strong>FormData contents:</strong><br>"
                                            (if (> (.-length entries) 0)
                                              (.join (.map entries
                                                           (fn [[name value]]
                                                             (str name " = " value)))
                                                     "<br>")
                                              "No form data found"))))}}
      "Extract FormData"]
     [:div#multiselect-form-results.mt-4.p-3.rounded.font-mono.text-xs.ty-bg-neutral-.ty-text-
      "Click 'Extract FormData' to see form values..."]]
    (code-block "<!-- HTMX Integration -->
<form hx-post=\"/submit\" hx-target=\"#results\">
  <ty-multiselect name=\"categories\" placeholder=\"Select categories...\">
    <ty-tag value=\"tech\" pill size=\"sm\">Technology</ty-tag>
    <ty-tag value=\"design\" pill size=\"sm\">Design</ty-tag>
  </ty-multiselect>
  
  <ty-multiselect name=\"skills\" placeholder=\"Select skills...\">
    <ty-tag value=\"javascript\" pill size=\"sm\">JavaScript</ty-tag>
    <ty-tag value=\"python\" pill size=\"sm\">Python</ty-tag>
  </ty-multiselect>
  
  <button type=\"submit\">Submit</button>
</form>

<!-- Resulting FormData (URL-encoded) -->
<!-- categories=tech&categories=design&skills=javascript&skills=python -->"
                "html")]
   [:div.text-xs.ty-text--
    [:strong "Expected server payload:"] [:br]
    [:code "categories: ['tech', 'design'], skills: ['javascript', 'python']"]]])

(defn- render-tag-behavior-section []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Tag Behavior & Automatic Management"]
   [:div.mb-6
    [:h3.text-xl.font-semibold.ty-text++.mb-2 "Automatic Tag State Management"]
    [:p.ty-text-.mb-3 "ty-multiselect automatically manages tag states:"]
    [:ul.space-y-2.ty-text-.ml-4
     [:li "• " [:strong "Unselected tags"] " remain in the dropdown options"]
     [:li "• " [:strong "Selected tags"] " move to the " [:code.ty-bg-neutral-.px-1.rounded "selected"] " slot"]
     [:li "• " [:strong "Selected tags"] " automatically get " [:code.ty-bg-neutral-.px-1.rounded "dismissible=\"true\""] " attribute"]
     [:li "• " [:strong "Clicking a selected tag's dismiss button"] " moves it back to options"]
     [:li "• " [:strong "Tag flavors"] " should match the multiselect flavor for visual consistency"]]]
   [:div.mb-6
    [:h3.text-xl.font-semibold.ty-text++.mb-2 "Tag Size Recommendations"]
    [:div.flex.gap-4.items-start.mb-3
     [:div.flex.flex-col.gap-2
      [:ty-multiselect {:placeholder "Small tags (recommended)"
                        :value "small1,small2"
                        :style {:min-width "220px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "small1"
                 :size "sm"} "Small Tag"]
       [:ty-tag {:value "small2"
                 :size "sm"} "Small Tag"]
       [:ty-tag {:value "small3"
                 :size "sm"} "Small Tag"]]
      [:div.text-xs.ty-text-- "size=\"sm\" (recommended)"]]
     [:div.flex.flex-col.gap-2
      [:ty-multiselect {:placeholder "Regular tags"
                        :value "regular1,regular2"
                        :style {:min-width "220px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "regular1"} "Regular Tag"]
       [:ty-tag {:value "regular2"} "Regular Tag"]
       [:ty-tag {:value "regular3"} "Regular Tag"]]
      [:div.text-xs.ty-text-- "default size"]]]
    [:p.ty-text-.text-sm "Small tags (" [:code.ty-bg-neutral-.px-1.rounded "size=\"sm\""] ") are recommended for better space efficiency."]]
   [:div
    [:h3.text-xl.font-semibold.ty-text++.mb-2 "Pill vs. Rectangular Tags"]
    [:div.flex.gap-4.items-start.mb-3
     [:div.flex.flex-col.gap-2
      [:ty-multiselect {:placeholder "Pill tags (recommended)"
                        :value "pill1,pill2"
                        :style {:min-width "220px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "pill1"
                 :size "sm"} "Pill Tag"]
       [:ty-tag {:value "pill2"
                 :size "sm"} "Pill Tag"]
       [:ty-tag {:value "pill3"
                 :size "sm"} "Pill Tag"]]
      [:div.text-xs.ty-text-- "pill=\"true\" (recommended)"]]
     [:div.flex.flex-col.gap-2
      [:ty-multiselect {:placeholder "Rectangular tags"
                        :value "rect1,rect2"
                        :style {:min-width "220px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "rect1"
                 :size "sm"
                 :pill "false"} "Rect Tag"]
       [:ty-tag {:value "rect2"
                 :size "sm"
                 :pill "false"} "Rect Tag"]
       [:ty-tag {:value "rect3"
                 :size "sm"
                 :pill "false"} "Rect Tag"]]
      [:div.text-xs.ty-text-- "default shape"]]]
    [:p.ty-text-.text-sm "Pill tags (" [:code.ty-bg-neutral-.px-1.rounded "pill=\"true\""] ") are recommended for multiselect use cases."]]])

(defn- render-event-handling-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Event Handling"]
   [:p.ty-text-.mb-4
    "ty-multiselect dispatches detailed change events with information about what changed:"]
   [:div.mb-4
    [:ty-multiselect {:id "event-demo"
                      :placeholder "Select to see events in console..."
                      :style {:min-width "280px"}
                      :on {:change multiselect-event-handler}}
     [:ty-tag {:value "event1"
               :size "sm"} "Event Test 1"]
     [:ty-tag {:value "event2"
               :size "sm"} "Event Test 2"]
     [:ty-tag {:value "event3"
               :size "sm"} "Event Test 3"]
     [:ty-tag {:value "event4"
               :size "sm"} "Event Test 4"]]]
   (code-block "// Event payload structure
{
  values: string[],        // Current selected values array
  action: string,          // \"add\", \"remove\", \"clear\", \"set\"
  item: string | null      // The specific item that was added/removed (null for clear/set)
}

// JavaScript event handling
const handleMultiselectChange = (event) => {
  const { values, action, item } = event.detail;
  
  console.log('Selection changed:', {
    currentValues: values,     // [\"event1\", \"event2\"]
    action: action,            // \"add\" | \"remove\" | \"clear\" | \"set\"
    changedItem: item          // \"event2\" (what was added/removed)
  });
  
  // React to specific actions
  if (action === 'add') {
    console.log(`Added: ${item}`);
  } else if (action === 'remove') {
    console.log(`Removed: ${item}`);
  } else if (action === 'clear') {
    console.log('All selections cleared');
  }
};

document.getElementById('my-multiselect')
  .addEventListener('change', handleMultiselectChange);

// React/framework example
<TyMultiselect 
  onChange={handleMultiselectChange}
  placeholder=\"Select options...\">
  <ty-tag value=\"option1\" pill size=\"sm\">Option 1</ty-tag>
</TyMultiselect>" "javascript")
   [:div.mt-4.p-3.ty-bg-primary-.rounded.text-sm
    [:p.ty-text-primary++ [:strong "💡 Tip:"] " Open browser console to see detailed change events from the demo above."]]])

(defn- render-best-practices-section []
  [:div.ty-elevated.rounded-lg.p-6
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Best Practices"]
   [:div.grid.md:grid-cols-2.gap-6
    [:div
     [:h3.text-lg.font-semibold.ty-text-success++.mb-3.flex.items-center.gap-2
      [:ty-icon {:name "check-circle"
                 :size "20"}]
      "Do's"]
     [:ul.space-y-2.ty-text-
      [:li "• Use small pill tags (" [:code.ty-bg-neutral-.px-1.rounded "size=\"sm\" pill"] ") for better layout"]
      [:li "• Match tag flavors with multiselect flavor for consistency"]
      [:li "• Set appropriate min-width to prevent layout shifts"]
      [:li "• Use semantic flavors to convey meaning (success, danger, etc.)"]
      [:li "• Provide clear, descriptive labels for accessibility"]
      [:li "• Use " [:code.ty-bg-neutral-.px-1.rounded "required"] " attribute for mandatory fields"]
      [:li "• Listen to change events to react to selection changes"]]]
    [:div
     [:h3.text-lg.font-semibold.ty-text-danger++.mb-3.flex.items-center.gap-2
      [:ty-icon {:name "x-circle"
                 :size "20"}]
      "Don'ts"]
     [:ul.space-y-2.ty-text-
      [:li "• Don't mix tag sizes within the same multiselect"]
      [:li "• Don't use placeholder text as a label replacement"]
      [:li "• Don't forget to set " [:code.ty-bg-neutral-.px-1.rounded "value"] " attributes on tags"]
      [:li "• Don't make the component too narrow for selected tags"]
      [:li "• Don't manually manage tag " [:code.ty-bg-neutral-.px-1.rounded "selected"] " attributes after initialization"]
      [:li "• Don't use conflicting tag and multiselect flavors"]
      [:li "• Don't forget the " [:code.ty-bg-neutral-.px-1.rounded "name"] " attribute for forms"]]]]
   [:div.mt-6.p-4.ty-bg-primary-.rounded
    [:h3.font-semibold.ty-text-primary++.mb-2.flex.items-center.gap-2
     [:ty-icon {:name "lightbulb"
                :size "18"}]
     "Key Tips"]
    [:ul.space-y-1.text-sm.ty-text-primary
     [:li "• Tags automatically move between slots based on selection state"]
     [:li "• Search functionality is built-in - users can type to filter options"]
     [:li "• Selected tags become dismissible automatically (X button appears)"]
     [:li "• Component automatically closes dropdown when all options are selected"]
     [:li "• FormData integration works seamlessly with HTMX and standard forms"]
     [:li "• Use " [:code.ty-bg-neutral-.px-1.rounded "value"] " attribute to set initial selected values"]
     [:li "• The component handles keyboard navigation and accessibility automatically"]]]])

(defn- render-related-components-section []
  [:div.mt-8.p-4.ty-border.border.rounded-lg
   [:h3.font-semibold.ty-text+.mb-2 "Related Components"]
   [:div.flex.gap-4.text-sm
    [:a.ty-text-primary.hover:underline {:href "/docs/dropdown"} "ty-dropdown →"]
    [:a.ty-text-primary.hover:underline {:href "/docs/tag"} "ty-tag →"]
    [:a.ty-text-primary.hover:underline {:href "/docs/input"} "ty-input →"]]])

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   (render-hero-section)
   (render-api-reference)
   (render-basic-usage-examples)
   (render-semantic-flavor-examples)
   (render-component-state-examples)
   (render-form-integration-example)
   (render-tag-behavior-section)
   (render-event-handling-section)
   (render-best-practices-section)
   (render-related-components-section)])
