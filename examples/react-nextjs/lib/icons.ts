/**
 * Icon Registry Initialization
 * 
 * This demonstrates the recommended pattern for using Ty icons:
 * 1. Import only the icons you need from @gersak/ty (tree-shaking!)
 * 2. Register them using the global window.ty.icons.register API
 * 3. Use them throughout your app with <ty-icon name="..." />
 * 
 * Benefits:
 * - Single icon registry (shared with web components)
 * - Perfect tree-shaking (only bundle icons you import)
 * - No duplicate dependencies
 * - TypeScript autocomplete for icon names
 */

// Import all icons used throughout the example app
import {
  // Basic icons
  check,
  heart,
  star,
  home,
  settings,
  user,
  users,
  mail,
  bell,
  searchIcon,
  menuIcon,
  x,
  info,
  calendar,
  moon,
  sun,

  // Navigation icons
  chevronRight,
  chevronLeft,
  chevronDown,
  chevronUp,
  arrowRight,
  externalLink,

  // Action icons
  plus,
  edit,
  save,
  sendIcon,
  download,
  upload,
  trash2,
  refreshCw,
  refreshCcw,
  play,

  // Status icons
  checkCircle,
  xCircle,
  alertCircle,
  alertTriangle,
  helpCircle,

  // Feature icons
  activity,
  book,
  clock,
  code,
  dollarSign,
  filterIcon,
  globe,
  layers,
  layout,
  mapPin,
  monitor,
  palette,
  phone,
  rocket,
  shield,
  square,
  terminal,
  trendingUp,
  zap,
  copy,
  packageIcon,
} from '@gersak/ty/icons/lucide'

/**
 * Register icons at app startup
 * Uses the global window.ty.icons.register API from the loaded script
 */
export function initializeIcons() {
  if (typeof window === 'undefined' || !window.ty) {
    console.warn('⚠️ window.ty not found. Make sure <script src="/ty/index.js"> is loaded first.')
    return
  }

  // Register all icons used in the example app
  window.ty.icons.register({
    // Basic icons
    check,
    heart,
    star,
    home,
    settings,
    user,
    users,
    mail,
    bell,
    'search': searchIcon,
    x,
    info,
    calendar,
    moon,
    sun,
    'package': packageIcon,
    'copy': copy,
    'menu': menuIcon,

    // Navigation icons
    'chevron-right': chevronRight,
    'chevron-left': chevronLeft,
    'chevron-down': chevronDown,
    'chevron-up': chevronUp,
    'arrow-right': arrowRight,
    'external-link': externalLink,

    // Action icons
    plus,
    edit,
    save,
    'send': sendIcon,
    download,
    upload,
    'trash-2': trash2,
    'refresh-cw': refreshCw,
    'refresh-ccw': refreshCcw,
    play,

    // Status icons
    'check-circle': checkCircle,
    'x-circle': xCircle,
    'alert-circle': alertCircle,
    'alert-triangle': alertTriangle,
    'help-circle': helpCircle,

    // Feature icons
    activity,
    book,
    clock,
    code,
    'dollar-sign': dollarSign,
    'filter': filterIcon,
    globe,
    layers,
    layout,
    'map-pin': mapPin,
    monitor,
    palette,
    phone,
    rocket,
    shield,
    square,
    terminal,
    'trending-up': trendingUp,
    zap,
  })

  // Verify registration
  console.log('📊 Total icons registered:', window.ty.icons.list().length)
}

/**
 * TypeScript Declaration
 * Extend the Window interface for TypeScript support
 */
declare global {
  interface Window {
    ty: {
      icons: {
        register: (icons: Record<string, string>) => void
        get: (name: string) => string | undefined
        has: (name: string) => boolean
        list: () => string[]
      }
      version: string
    }
  }
}
