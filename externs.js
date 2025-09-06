/**
 * @fileoverview Externs for ty web components
 * Prevents Closure Compiler from renaming these properties in advanced compilation mode
 */

/**
 * @typedef {{
 *   observed: (!Array<string>|undefined),
 *   props: (!Object|undefined),
 *   formAssociated: (boolean|undefined),
 *   construct: (function(!Element)|undefined),
 *   connected: (function(!Element)|undefined),
 *   disconnected: (function(!Element)|undefined),
 *   adopted: (function(!Element, !Document, !Document)|undefined),
 *   attr: (function(!Element, string, *, *)|undefined),
 *   prop: (function(!Element, string, *, *)|undefined),
 *   formReset: (function(!Element)|undefined),
 *   formDisabled: (function(!Element, boolean)|undefined),
 *   formStateRestore: (function(!Element, *, string)|undefined)
 * }}
 */
var TyHooks;

/**
 * Ty component hooks object
 * @type {TyHooks}
 */
var hooks;

// Core hook properties that must be preserved
hooks.observed;
hooks.props;
hooks.formAssociated;

// Lifecycle hooks
hooks.construct;
hooks.connected;
hooks.disconnected;
hooks.adopted;

// Change hooks  
hooks.attr;
hooks.prop;

// Form lifecycle hooks
hooks.formReset;
hooks.formDisabled;
hooks.formStateRestore;

/**
 * Web Component internal properties that should be preserved
 * @constructor
 */
function TyElement() { }

// Internal state properties used by ty components
TyElement.prototype._hooks;
TyElement.prototype._props;
TyElement.prototype._internals;
TyElement.prototype._initialFormState;
TyElement.prototype._syncingProperty;

// Component-specific state properties
TyElement.prototype.tyInputState;
TyElement.prototype.tyComponentState;
TyElement.prototype.tyMultiselectState;
TyElement.prototype.tyDropdownState;
TyElement.prototype.tyModalState;
TyElement.prototype.tyInitialAttrsRead;

// Batch queue properties
TyElement.prototype.tyAttrBatch;
TyElement.prototype.tyUnifiedBatch;

// Custom properties for ty components
TyElement.prototype.value;
TyElement.prototype.shadowValue;
TyElement.prototype.formattedValue;
TyElement.prototype.selected;

/**
 * HTML Dialog Element and Modal API
 * @constructor
 * @extends {HTMLElement}
 */
var HTMLDialogElement = function() { };

/**
 * Shows the dialog as a modal
 * @return {undefined}
 */
HTMLDialogElement.prototype.showModal = function() { };

/**
 * Shows the dialog non-modally
 * @return {undefined}
 */
HTMLDialogElement.prototype.show = function() { };

/**
 * Closes the dialog
 * @param {string=} returnValue
 * @return {undefined}
 */
HTMLDialogElement.prototype.close = function(returnValue) { };

/**
 * Whether the dialog is open
 * @type {boolean}
 */
HTMLDialogElement.prototype.open;

/**
 * The return value of the dialog
 * @type {string}
 */
HTMLDialogElement.prototype.returnValue;

/**
 * Event fired when dialog closes
 * @type {Function}
 */
HTMLDialogElement.prototype.onclose;

/**
 * Properties that should be preserved on regular JavaScript objects
 */
var obj = {};

// Form state properties
obj.value;
obj.disabled;
obj.required;
obj.type;
obj.name;
obj.placeholder;

// Component state properties  
obj.open;
obj.placement;
obj.offset;
obj.flip;
obj.size;
obj.flavor;
obj.error;
obj.label;

// Batch queue properties
obj.changes;
obj.scheduled;
obj.attrChanges;
obj.propChanges;

// Component state objects
obj.shadowValue;
obj.lastExternalValue;
obj.isFocused;
obj.isOpen;
obj.search;
obj.highlightedIndex;
obj.filteredOptions;
