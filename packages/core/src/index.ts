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

// Version
export const VERSION = '0.2.0'
