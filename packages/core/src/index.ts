// Ty TypeScript Components
// Modern, zero-dependency web components

export { TyButton } from './components/button.js'
export { TyIcon, IconRegistry } from './components/icon.js'
export { TyTag } from './components/tag.js'
export { TyOption } from './components/option.js'
export { TyInput } from './components/input.js'
export { TyCheckbox } from './components/checkbox.js'
export { TyCopy } from './components/copy.js'
export { TyTextarea } from './components/textarea.js'
export { TyTooltip } from './components/tooltip.js'
export { TyPopup } from './components/popup.js'
export { TyModal } from './components/modal.js'
export { TyDropdown } from './components/dropdown.js'
export { TyMultiselect } from './components/multiselect.js'
export { TyTabs } from './components/tabs.js'
export { TyTab } from './components/tab.js'
export { TyCalendarMonth } from './components/calendar-month.js'
export { TyCalendarNavigation } from './components/calendar-navigation.js'
export { TyCalendar } from './components/calendar.js'
export { TyDatePicker } from './components/date-picker.js'

// Types
export type {
  Flavor,
  Size,
  InputType,
  IconSize,
  IconTempo,
  TyButtonElement,
  TyIconElement,
  TyTagElement,
  TyTagEventDetail,
  TyOptionElement,
  TyInputElement
} from './types/common.js'

export type { TyCheckboxElement } from './components/checkbox.js'
export type { TyCopyElement } from './components/copy.js'
export type { TyTextareaElement } from './components/textarea.js'
export type { TooltipFlavor, TooltipAttributes } from './components/tooltip.js'
export type { PopupAttributes } from './components/popup.js'
export type { ModalAttributes, ModalCloseDetail } from './components/modal.js'
export type { TabsAttributes, TabChangeDetail } from './components/tabs.js'
export type { TabAttributes } from './components/tab.js'
export type { DayContentFn, DayClickDetail, CalendarSize } from './components/calendar-month.js'
export type { NavigationChangeDetail } from './components/calendar-navigation.js'
export type { CalendarChangeDetail, CalendarNavigateDetail } from './components/calendar.js'
export type { DatePickerChangeDetail } from './components/date-picker.js'
export type { DayContext } from './utils/calendar-utils.js'

// Property capture utilities for React/Reagent compatibility
export {
  capturePreSetProperties,
  getCapturedValues,
  applyPreSetProperties,
  captureAndApplyProperties
} from './utils/property-capture.js'
export type { CapturedProperty, PropertyCaptureOptions, PropertyMap } from './utils/property-capture.js'

// Utilities
export {
  lockScroll,
  unlockScroll,
  forceUnlockAll,
  isLocked,
  isLockedBy,
  getActiveLocks,
  getLockState,
  enableDebug as enableScrollLockDebug,
  disableDebug as disableScrollLockDebug
} from './utils/scroll-lock.js'

export type {
  Placement,
  PositionResult,
  CleanupFn
} from './utils/positioning.js'

// Icons
// Note: Icon libraries moved to separate @gersak/ty-icons package
// Icon registry utility remains in core for registering custom icons

// Version (auto-generated from package.json)
import { VERSION } from './version.js'

// Global API
// Expose window.tyIcons for script tag usage
import { registerIcons, getIcon, hasIcon, getIconNames, getCacheInfo, clearIcons, getCachedIcon } from './utils/icon-registry.js'

// Export icon registry functions for advanced use
export { registerIcons, getIcon, hasIcon, getIconNames, getCacheInfo, clearIcons, getCachedIcon }

declare global {
  interface Window {
    tyVersion: string
    tyIcons: {
      register: (icons: Record<string, string>) => void
      get: (name: string) => string | undefined
      has: (name: string) => boolean
      list: () => string[]
      cacheInfo: () => Promise<{
        version: string
        cacheName: string
        available: boolean
        iconCount?: number
      }>
      clearCache: () => Promise<void>
    }
  }
}

if (typeof window !== 'undefined') {
  window.tyVersion = VERSION
  window.tyIcons = {
    register: (icons: Record<string, string>) => {
      const count = Object.keys(icons).length
      registerIcons(icons)

      // Defer logging to not block registration
      setTimeout(() => {
        console.log(`✅ Registered ${count} icons (cached for instant reload)`)
      }, 0)
    },
    get: (name: string) => getIcon(name),
    has: (name: string) => hasIcon(name),
    list: () => getIconNames(),
    cacheInfo: () => getCacheInfo(),
    clearCache: () => clearIcons()
  }
}