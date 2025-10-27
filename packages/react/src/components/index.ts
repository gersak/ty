// ===================================================================
// TY REACT WRAPPER EXPORTS
// ===================================================================
// This file provides two export styles for maximum developer flexibility:
// 1. Ty-prefixed exports (TyButton, TyInput) - explicit and backward compatible
// 2. Short name exports (Button, Input) - clean and familiar to React developers
//
// Choose the style that fits your team's preferences!

// ===================================================================
// TY-PREFIXED EXPORTS (Explicit Style - Backward Compatible)
// ===================================================================

export { TyButton } from './TyButton';
export type { TyButtonProps } from './TyButton';

export { TyTag } from './TyTag';
export type { TyTagProps } from './TyTag';

export { TyInput } from './TyInput';
export type { TyInputProps, TyInputEventDetail } from './TyInput';

export { TyTextarea } from './TyTextarea';
export type { TyTextareaProps, TyTextareaEventDetail } from './TyTextarea';

export { TyDropdown } from './TyDropdown';
export type { TyDropdownProps, TyDropdownEventDetail } from './TyDropdown';

export { TyOption } from './TyOption';
export type { TyOptionProps } from './TyOption';

export { TyIcon } from './TyIcon';
export type { TyIconProps } from './TyIcon';

export { TyModal } from './TyModal';
export type { TyModalProps, TyModalEventDetail, TyModalRef } from './TyModal';

export { TyTooltip } from './TyTooltip';
export type { TyTooltipProps } from './TyTooltip';

export { TyMultiselect } from './TyMultiselect';
export type { TyMultiselectProps, TyMultiselectEventDetail } from './TyMultiselect';

export { TyCalendar } from './TyCalendar';
export type { TyCalendarProps, TyCalendarChangeEventDetail, TyCalendarNavigateEventDetail } from './TyCalendar';

export { TyDatePicker } from './TyDatePicker';
export type { TyDatePickerProps, TyDatePickerEventDetail } from './TyDatePicker';

export { TyPopup } from './TyPopup';
export type { TyPopupProps, TyPopupElement, TyPopupOpenEvent, TyPopupCloseEvent } from './TyPopup';

export { TyCheckbox } from './TyCheckbox';
export type { TyCheckboxProps, TyCheckboxEventDetail } from './TyCheckbox';

export { TyCopy } from './TyCopy';
export type { TyCopyProps } from './TyCopy';

export { TyTabs } from './TyTabs';
export type { TyTabsProps, TabChangeDetail } from './TyTabs';

export { TyTab } from './TyTab';
export type { TyTabProps } from './TyTab';

export { TyCalendarMonth } from './TyCalendarMonth';
export type { TyCalendarMonthProps, DayClickDetail } from './TyCalendarMonth';

export { TyCalendarNavigation } from './TyCalendarNavigation';
export type { TyCalendarNavigationProps, NavigationChangeDetail } from './TyCalendarNavigation';

// ===================================================================
// SHORT NAME EXPORTS (Clean Style - Developer Choice)
// ===================================================================

export { TyButton as Button } from './TyButton';
export { TyTag as Tag } from './TyTag';
export { TyInput as Input } from './TyInput';
export { TyTextarea as Textarea } from './TyTextarea';
export { TyDropdown as Dropdown } from './TyDropdown';
export { TyOption as Option } from './TyOption';
export { TyIcon as Icon } from './TyIcon';
export { TyModal as Modal } from './TyModal';
export { TyTooltip as Tooltip } from './TyTooltip';
export { TyMultiselect as Multiselect } from './TyMultiselect';
export { TyCalendar as Calendar } from './TyCalendar';
export { TyDatePicker as DatePicker } from './TyDatePicker';
export { TyPopup as Popup } from './TyPopup';
export { TyCheckbox as Checkbox } from './TyCheckbox';
export { TyCopy as Copy } from './TyCopy';
export { TyTabs as Tabs } from './TyTabs';
export { TyTab as Tab } from './TyTab';
export { TyCalendarMonth as CalendarMonth } from './TyCalendarMonth';
export { TyCalendarNavigation as CalendarNavigation } from './TyCalendarNavigation';

// ===================================================================
// TYPE ALIASES (Both Styles Supported)
// ===================================================================

// Button types
export type { TyButtonProps as ButtonProps } from './TyButton';

// Tag types
export type { TyTagProps as TagProps } from './TyTag';

// Input types
export type { TyInputProps as InputProps, TyInputEventDetail as InputEventDetail } from './TyInput';

// Textarea types
export type { TyTextareaProps as TextareaProps, TyTextareaEventDetail as TextareaEventDetail } from './TyTextarea';

// Dropdown types
export type { TyDropdownProps as DropdownProps, TyDropdownEventDetail as DropdownEventDetail, OptionData } from './TyDropdown';

// Option types
export type { TyOptionProps as OptionProps } from './TyOption';

// Icon types
export type { TyIconProps as IconProps } from './TyIcon';

// Modal types
export type { TyModalProps as ModalProps, TyModalEventDetail as ModalEventDetail, TyModalRef as ModalRef } from './TyModal';

// Tooltip types
export type { TyTooltipProps as TooltipProps } from './TyTooltip';

// Multiselect types
export type { TyMultiselectProps as MultiselectProps, TyMultiselectEventDetail as MultiselectEventDetail } from './TyMultiselect';

// Calendar types
export type { TyCalendarProps as CalendarProps, TyCalendarChangeEventDetail as CalendarChangeEventDetail, TyCalendarNavigateEventDetail as CalendarNavigateEventDetail } from './TyCalendar';

// DatePicker types
export type { TyDatePickerProps as DatePickerProps, TyDatePickerEventDetail as DatePickerEventDetail } from './TyDatePicker';

// Popup types
export type { TyPopupProps as PopupProps, TyPopupElement as PopupElement, TyPopupOpenEvent as PopupOpenEvent, TyPopupCloseEvent as PopupCloseEvent } from './TyPopup';

// Checkbox types
export type { TyCheckboxProps as CheckboxProps, TyCheckboxEventDetail as CheckboxEventDetail } from './TyCheckbox';

// Copy types
export type { TyCopyProps as CopyProps } from './TyCopy';

// Tabs types
export type { TyTabsProps as TabsProps } from './TyTabs';

// Tab types
export type { TyTabProps as TabProps } from './TyTab';

// CalendarMonth types
export type { TyCalendarMonthProps as CalendarMonthProps } from './TyCalendarMonth';

// CalendarNavigation types
export type { TyCalendarNavigationProps as CalendarNavigationProps } from './TyCalendarNavigation';

// ===================================================================
// USAGE EXAMPLES
// ===================================================================

/*

// STYLE 1: Ty-prefixed (Explicit and backward compatible)
import { TyButton, TyInput, TyModal } from 'ty-react';
import type { TyButtonProps, TyInputProps } from 'ty-react';

function MyComponent() {
  return (
    <TyModal>
      <TyInput placeholder="Enter text..." />
      <TyButton>Submit</TyButton>
    </TyModal>
  );
}

// STYLE 2: Short names (Clean and familiar)
import { Button, Input, Modal } from 'ty-react';
import type { ButtonProps, InputProps } from 'ty-react';

function MyComponent() {
  return (
    <Modal>
      <Input placeholder="Enter text..." />
      <Button>Submit</Button>
    </Modal>
  );
}

// STYLE 3: Mixed (Team preferences)
import { TyModal, Input, Button } from 'ty-react';
import type { TyModalProps, InputProps } from 'ty-react';

function MyComponent() {
  return (
    <TyModal>
      <Input placeholder="Enter text..." />
      <Button>Submit</Button>
    </TyModal>
  );
}

*/