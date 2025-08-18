(ns ty.components.checkbox
  (:require [clojure.string :as str]
            [ty.css :refer [ensure-styles!]]
            [ty.icons :as icons]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load checkbox styles from checkbox.css
(defstyles checkbox-styles)

(defn checkbox-attributes
  "Read all checkbox attributes directly from element"
  [^js el]
  {:checked (wcs/parse-bool-attr el "checked")
   :indeterminate (wcs/parse-bool-attr el "indeterminate")
   :disabled (wcs/parse-bool-attr el "disabled")
   :flavor (wcs/attr el "flavor")
   :size (wcs/attr el "size")
   :label (wcs/attr el "label")
   :class (wcs/attr el "class")})

(defn build-class-list
  "Build class list from attributes"
  [{:keys [checked indeterminate disabled flavor size class]}]
  (str/trim
    (str (or flavor "neutral")
         " "
         (or size "md")
         " "
         (cond
           indeterminate "indeterminate"
           checked "checked"
           :else "unchecked")
         (when disabled " disabled")
         (when class (str " " class)))))

(defn get-icon-name
  "Get the appropriate icon based on state"
  [{:keys [checked indeterminate]}]
  (cond
    indeterminate "minus"
    checked "check"
    :else nil))

(defn render! [^js el]
  (let [{:keys [disabled label checked indeterminate]
         :as attrs} (checkbox-attributes el)
        root (wcs/ensure-shadow el)
        ;; Check if we already have wrapper element
        wrapper-el (.querySelector root ".checkbox-wrapper")]

    ;; Ensure styles are loaded
    (ensure-styles! root checkbox-styles "ty-checkbox")

    ;; Create or update wrapper element
    (if wrapper-el
      ;; Update existing wrapper
      (let [box-el (.querySelector wrapper-el ".checkbox-box")
            icon-slot (.querySelector box-el "slot[name='icon']")
            label-el (.querySelector wrapper-el ".checkbox-label")]
        ;; Update classes
        (set! (.-className wrapper-el) "checkbox-wrapper")
        (set! (.-className box-el) (str "checkbox-box " (build-class-list attrs)))

        ;; Update disabled state
        (if disabled
          (.setAttribute wrapper-el "aria-disabled" "true")
          (.removeAttribute wrapper-el "aria-disabled"))

        ;; Update label if exists
        (when label-el
          (set! (.-textContent label-el) (or label ""))))

      ;; Create new structure
      (let [wrapper (js/document.createElement "div")
            box (js/document.createElement "div")
            icon-slot (js/document.createElement "slot")
            label-slot (js/document.createElement "slot")]

        ;; Set up wrapper
        (set! (.-className wrapper) "checkbox-wrapper")
        (.setAttribute wrapper "role" "checkbox")
        (.setAttribute wrapper "tabindex" (if disabled "-1" "0"))
        (.setAttribute wrapper "aria-checked" (str (or checked false)))
        (when disabled
          (.setAttribute wrapper "aria-disabled" "true"))

        ;; Set up checkbox box
        (set! (.-className box) (str "checkbox-box " (build-class-list attrs)))

        ;; Set up icon slot
        (set! (.-name icon-slot) "icon")
        (.appendChild box icon-slot)

        ;; Add label if provided
        (when label
          (let [label-el (js/document.createElement "span")]
            (set! (.-className label-el) "checkbox-label")
            (set! (.-textContent label-el) label)
            (.appendChild wrapper label-el)))

        ;; Otherwise add default slot for custom content
        (when-not label
          (.appendChild wrapper label-slot))

        ;; Build structure
        (.appendChild wrapper box)

        ;; Add click handler
        (.addEventListener wrapper "click"
                           (fn [e]
                             (when-not disabled
                               (.stopPropagation e)
                               ;; Toggle checked state
                               (let [new-checked (not checked)]
                                 ;; Update attribute
                                 (if new-checked
                                   (.setAttribute el "checked" "")
                                   (.removeAttribute el "checked"))
                                 ;; Clear indeterminate when user clicks
                                 (.removeAttribute el "indeterminate")
                                 ;; Dispatch change event
                                 (.dispatchEvent el (js/CustomEvent. "change"
                                                                     #js {:bubbles true
                                                                          :composed true
                                                                          :detail {:checked new-checked}}))))))

        ;; Add keyboard handler
        (.addEventListener wrapper "keydown"
                           (fn [e]
                             (when (and (not disabled)
                                        (contains? #{" " "Enter"} (.-key e)))
                               (.preventDefault e)
                               (.click wrapper))))

        (.appendChild root wrapper)))

    ;; Always update the icon based on current state
    (let [icon-name (get-icon-name attrs)]
      (if icon-name
        ;; Show icon
        (let [icon-el (or (.querySelector el "ty-icon[slot='icon']")
                          (js/document.createElement "ty-icon"))]
          (when-not (.querySelector el "ty-icon[slot='icon']")
            (.setAttribute icon-el "slot" "icon")
            (.appendChild el icon-el))
          (.setAttribute icon-el "name" icon-name))
        ;; Remove icon if unchecked
        (when-let [icon-el (.querySelector el "ty-icon[slot='icon']")]
          (.remove icon-el))))

    el))

(wcs/define! "ty-checkbox"
  {:observed [:checked :indeterminate :disabled :flavor :size :label :class]
   :connected render!
   :attr (fn [^js el attr-name old new]
           ;; Handle property reflection for boolean attributes
           (case attr-name
             "checked" (set! (.-checked el) (wcs/parse-bool-attr el "checked"))
             "indeterminate" (set! (.-indeterminate el) (wcs/parse-bool-attr el "indeterminate"))
             "disabled" (set! (.-disabled el) (wcs/parse-bool-attr el "disabled"))
             nil)
           ;; Always re-render on attribute change
           (render! el))
   :prop (fn [^js el prop-name old new]
           ;; Reflect properties to attributes
           (case prop-name
             "checked" (if new
                         (.setAttribute el "checked" "")
                         (.removeAttribute el "checked"))
             "indeterminate" (if new
                               (.setAttribute el "indeterminate" "")
                               (.removeAttribute el "indeterminate"))
             "disabled" (if new
                          (.setAttribute el "disabled" "")
                          (.removeAttribute el "disabled"))
             nil))})

;; -----------------------------
;; Hot Reload Hooks
;; -----------------------------

(defn ^:dev/before-load stop []
  (when goog.DEBUG
    (js/console.log "[ty-checkbox] Preparing for hot reload...")))

(defn ^:dev/after-load start []
  (when goog.DEBUG
    (js/console.log "[ty-checkbox] Hot reload complete!")))
