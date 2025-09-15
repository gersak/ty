(ns hello.components
  "Wrapper components for Ty Web Components with proper event handling"
  (:require [reagent.core :as r]))

(defn ty-date-picker
  "Reagent wrapper for ty-date-picker that handles custom events properly"
  [props]
  (let [element-ref (atom nil)
        {:keys [on-change] :as props} props
        filtered-props (dissoc props :on-change)]
    
    (r/create-class
      {:component-did-mount
       (fn [this]
         (when (and @element-ref on-change)
           (.addEventListener @element-ref "change"
                              (fn [^js event]
                                (let [detail (.-detail event)
                                      value (.-value detail)
                                      formatted (.-formatted detail)
                                      source (.-source detail)]
                                  (on-change {:value value
                                              :formatted formatted
                                              :source source
                                              :original-event event}))))))
       
       :component-will-unmount
       (fn [this]
         (when @element-ref
           ;; Remove event listener on unmount
           (.removeEventListener @element-ref "change" 
                                 (.-tyChangeListener @element-ref))))
       
       :reagent-render
       (fn [props]
         [:ty-date-picker
          (merge filtered-props
                 {:ref (fn [el] 
                         (reset! element-ref el))})])})))

(defn ty-dropdown
  "Reagent wrapper for ty-dropdown that handles custom events"
  [props]
  (let [element-ref (atom nil)
        {:keys [on-change] :as props} props
        filtered-props (dissoc props :on-change)]
    
    (r/create-class
      {:component-did-mount
       (fn [this]
         (when (and @element-ref on-change)
           (.addEventListener @element-ref "change"
                              (fn [^js event]
                                (let [detail (.-detail event)
                                      option (.-option detail)]
                                  (on-change {:option option
                                              :value (.-value option)
                                              :text (.-textContent option)
                                              :original-event event}))))))
       
       :component-will-unmount  
       (fn [this]
         (when @element-ref
           (.removeEventListener @element-ref "change"
                                 (.-tyChangeListener @element-ref))))
       
       :reagent-render
       (fn [props & children]
         (into [:ty-dropdown
                (merge filtered-props
                       {:ref (fn [el] 
                               (reset! element-ref el))})]
               children))})))

(defn ty-multiselect
  "Reagent wrapper for ty-multiselect that handles custom events"
  [props]
  (let [element-ref (atom nil)
        {:keys [on-change] :as props} props
        filtered-props (dissoc props :on-change)]
    
    (r/create-class
      {:component-did-mount
       (fn [this]
         (when (and @element-ref on-change)
           (.addEventListener @element-ref "change"
                              (fn [^js event]
                                (let [detail (.-detail event)
                                      values (.-values detail)
                                      action (.-action detail)
                                      item (.-item detail)]
                                  (on-change {:values (js->clj values)
                                              :action action  
                                              :item item
                                              :original-event event}))))))
       
       :component-will-unmount
       (fn [this]
         (when @element-ref
           (.removeEventListener @element-ref "change"
                                 (.-tyChangeListener @element-ref))))
       
       :reagent-render
       (fn [props & children]
         (into [:ty-multiselect
                (merge filtered-props
                       {:ref (fn [el]
                               (reset! element-ref el))})]
               children))})))
