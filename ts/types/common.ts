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

/** CSS style content */
export interface StyleContent {
  /** CSS text content */
  css: string
  /** Optional style ID for caching */
  id?: string
}
