/**
 * Ty Component Common Types
 */

/** Semantic color flavors used throughout Ty components */
export type Flavor = 
  | 'primary' 
  | 'secondary' 
  | 'success' 
  | 'danger' 
  | 'warning' 
  | 'neutral'

/** Component size variants */
export type Size = 'xs' | 'sm' | 'md' | 'lg' | 'xl'

/** Input types supported by ty-input */
export type InputType = 
  | 'text' 
  | 'password' 
  | 'email' 
  | 'url' 
  | 'tel'
  | 'date' 
  | 'time' 
  | 'datetime-local'
  | 'number'          // NEW: Basic number with optional precision
  | 'currency'        // NEW: Currency formatting
  | 'percent'         // NEW: Percentage formatting
  | 'compact'         // NEW: Compact notation (1.2K, 3.4M)
  | 'checkbox'

/** Base properties shared by all Ty components */
export interface TyBaseElement extends HTMLElement {
  /** Semantic color flavor */
  flavor?: Flavor
}

/** Button component interface */
export interface TyButtonElement extends TyBaseElement {
  /** Button size */
  size?: Size
  /** Disabled state */
  disabled?: boolean
  /** Pill shape (fully rounded) */
  pill?: boolean
  /** Outlined appearance */
  outlined?: boolean
  /** Filled appearance */
  filled?: boolean
  /** Accent appearance (default) */
  accent?: boolean
  /** Plain appearance (no background) */
  plain?: boolean
  /** Button type for forms */
  type?: 'button' | 'submit' | 'reset'
}

/** CSS style content */
/** Icon size variants */
export type IconSize = 
  | 'xs' | 'sm' | 'md' | 'lg' | 'xl' | '2xl'  // Relative sizes
  | '12' | '14' | '16' | '18' | '20' | '24' | '32' | '48' | '64' | '80' | '96'  // Fixed pixel sizes

/** Icon animation tempo */
export type IconTempo = 'slow' | 'fast'

/** Icon component interface */
export interface TyIconElement extends HTMLElement {
  /** Icon name to display */
  name?: string
  /** Icon size */
  size?: IconSize
  /** Spin animation */
  spin?: boolean
  /** Pulse animation */
  pulse?: boolean
  /** Animation tempo */
  tempo?: IconTempo
}

/** Tag component custom event detail */
export interface TyTagEventDetail {
  /** Target tag element */
  target: HTMLElement
}

/** Tag component interface */
export interface TyTagElement extends TyBaseElement {
  /** Tag value */
  value?: string
  /** Tag size */
  size?: Size
  /** Selected state */
  selected?: boolean
  /** Pill shape (fully rounded) - default true */
  pill?: boolean
  /** Clickable state */
  clickable?: boolean
  /** Dismissible state */
  dismissible?: boolean
  /** Disabled state */
  disabled?: boolean
}

/** Option component interface - omit 'hidden' from HTMLElement to avoid conflict */
export interface TyOptionElement extends Omit<HTMLElement, 'hidden'> {
  /** Option value */
  value?: string
  /** Selected state */
  selected?: boolean
  /** Disabled state */
  disabled?: boolean
  /** Highlighted state (keyboard navigation) */
  highlighted?: boolean
  /** Hidden state - overrides HTMLElement's hidden */
  hidden?: boolean
}

/** Input component interface */
export interface TyInputElement extends TyBaseElement {
  /** Input type */
  type?: InputType
  /** Input value */
  value?: string
  /** Input name (for forms) */
  name?: string
  /** Placeholder text */
  placeholder?: string
  /** Label text */
  label?: string
  /** Disabled state */
  disabled?: boolean
  /** Required field */
  required?: boolean
  /** Error message */
  error?: string
  /** Input size */
  size?: Size
  /** Associated form */
  readonly form?: HTMLFormElement | null
  
  // Numeric formatting properties (NEW)
  /** Currency code for currency type (e.g., 'USD', 'EUR', 'HRK') */
  currency?: string
  /** Locale for formatting (e.g., 'en-US', 'hr-HR') */
  locale?: string
  /** Number of decimal places */
  precision?: number | string
}

/** CSS style content */
export interface StyleContent {
  /** CSS text content */
  css: string
  /** Optional style ID for caching */
  id?: string
}
