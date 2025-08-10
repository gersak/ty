(ns ty.styles.tokens
  (:require [clojure.string :as str]))

(def semantic-states
  [:positive :negative :exception :important :unique :neutral])

(def emphasis-modifiers
  [:p3 :p2 :p1 nil :m1 :m2 :m3])  ; nil = base

(defn css-var
  "Get CSS variable with optional fallback
   (css-var :color-positive) => \"var(--ty-color-positive)\"
   (css-var :color-positive \"#10b981\") => \"var(--ty-color-positive, #10b981)\""
  [var-name & [fallback]]
  (let [css-var-name (str "--ty-" (name var-name))]
    (if fallback
      (str "var(" css-var-name ", " fallback ")")
      (str "var(" css-var-name ")"))))

(defn semantic-var
  "Get semantic CSS variable
   (semantic-var :color :positive) => \"var(--ty-color-positive)\"
   (semantic-var :color :positive :p1) => \"var(--ty-color-positive-p1)\""
  [property state & [modifier]]
  (let [var-parts (cond-> [(name property) (name state)]
                    modifier (conj (name modifier)))]
    (css-var (keyword (str/join "-" var-parts)))))

(defn spacing
  "Get spacing variable
   (spacing 4) => \"var(--ty-spacing-4)\"
   (spacing \"1_5\") => \"var(--ty-spacing-1_5)\""
  [size]
  (css-var (keyword (str "spacing-" (name size)))))

(defn radius
  "Get border radius variable
   (radius :md) => \"var(--ty-radius-md)\""
  [size]
  (css-var (keyword (str "radius-" (name size)))))

(defn shadow
  "Get shadow variable
   (shadow :lg) => \"var(--ty-shadow-lg)\""
  [size]
  (css-var (keyword (str "shadow-" (name size)))))

(defn font-size
  "Get font size variable
   (font-size :sm) => \"var(--ty-font-size-sm)\""
  [size]
  (css-var (keyword (str "font-size-" (name size)))))

(defn font-weight
  "Get font weight variable
   (font-weight :semibold) => \"var(--ty-font-weight-semibold)\""
  [weight]
  (css-var (keyword (str "font-weight-" (name weight)))))

(defn line-height
  "Get line height variable
   (line-height :normal) => \"var(--ty-line-height-normal)\""
  [height]
  (css-var (keyword (str "line-height-" (name height)))))

(defn transition
  "Get transition variable
   (transition :all) => \"var(--ty-transition-all)\""
  [type]
  (css-var (keyword (str "transition-" (name type)))))
