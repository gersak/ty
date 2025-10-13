// Ty TypeScript Components
// Modern, zero-dependency web components

export { TyButton } from './components/button.js'
export { TyIcon, IconRegistry } from './components/icon.js'
export { TyTag } from './components/tag.js'
export { TyOption } from './components/option.js'
export { TyInput } from './components/input.js'
export { TyCheckbox } from './components/checkbox.js'
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
// Export icon sets for tree-shakeable imports
export * as lucideIcons from './icons/lucide.js'
export * as heroiconsOutline from './icons/heroicons/outline.js'
export * as heroiconsSolid from './icons/heroicons/solid.js'
export * as heroiconsMini from './icons/heroicons/mini.js'
export * as heroiconsMicro from './icons/heroicons/micro.js'
export * as materialFilled from './icons/material/filled.js'
export * as materialOutlined from './icons/material/outlined.js'
export * as materialRound from './icons/material/round.js'
export * as materialSharp from './icons/material/sharp.js'
export * as materialTwoTone from './icons/material/two-tone.js'
export * as fontawesomeSolid from './icons/fontawesome/solid.js'
export * as fontawesomeRegular from './icons/fontawesome/regular.js'
export * as fontawesomeBrands from './icons/fontawesome/brands.js'

// Version
export const VERSION = '0.2.0'

// Global API
// Expose window.ty for script tag usage
import { registerIcons, getIcon, hasIcon, getIconNames } from './utils/icon-registry.js'

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

if (typeof window !== 'undefined') {
  window.ty = {
    icons: {
      register: (icons: Record<string, string>) => {
        registerIcons(icons)
        console.log(`✅ Registered ${Object.keys(icons).length} icons`)
      },
      get: (name: string) => getIcon(name),
      has: (name: string) => hasIcon(name),
      list: () => getIconNames()
    },
    version: VERSION
  }
}
