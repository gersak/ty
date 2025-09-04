// Enhanced wc-shim.js with proper form lifecycle support
// A tiny helper to define Custom Elements that delegate lifecycle to passed hooks.
// Works with any bundler that supports ESM (shadow-cljs does).

function installPropAccessors(proto, propSpec) {
  if (!propSpec) return;
  Object.keys(propSpec).forEach((key) => {
    Object.defineProperty(proto, key, {
      get() {
        const p = this._props || {};
        return p[key];
      },
      set(v) {
        if (!this._props) this._props = {};
        const old = this._props[key];
        this._props[key] = v;
        if (this._hooks && this._hooks.prop) {
          this._hooks.prop(this, key, old, v);
        }
      }
    });
  });
}

function installFormControlProperties(proto, hooks) {
  // Only install form control properties if element is form-associated
  if (!hooks || !hooks.formAssociated) return;

  // Standard form control properties that need to be exposed
  const formControlProps = ['name', 'type', 'disabled', 'required'];

  formControlProps.forEach(propName => {
    Object.defineProperty(proto, propName, {
      get() {
        // First try to get from property, then from attribute
        const propValue = this._props && this._props[propName];
        if (propValue !== undefined) {
          return propValue;
        }

        // Fallback to attribute value
        const attrValue = this.getAttribute(propName);

        // Handle boolean attributes specially
        if (propName === 'disabled' || propName === 'required') {
          return attrValue !== null;
        }

        return attrValue;
      },

      set(value) {
        // Store in properties
        if (!this._props) this._props = {};
        const old = this._props[propName];
        this._props[propName] = value;

        // Sync with attribute for consistency
        if (propName === 'disabled' || propName === 'required') {
          // Boolean attributes
          if (value) {
            this.setAttribute(propName, '');
          } else {
            this.removeAttribute(propName);
          }
        } else {
          // String/value attributes
          if (value !== null && value !== undefined) {
            this.setAttribute(propName, String(value));
          } else {
            this.removeAttribute(propName);
          }
        }

        // Trigger property change hook
        if (this._hooks && this._hooks.prop) {
          this._hooks.prop(this, propName, old, value);
        }
      }
    });
  });
}

export function define(tag, hooks) {
  // hooks: { observed, formAssociated, construct(el), connected(el), disconnected(el), adopted(el, oldDoc, newDoc),
  //          attr(el, name, oldV, newV), prop(el, key, oldV, newV), 
  //          formReset(el), formDisabled(el, disabled), formStateRestore(el, state, reason) }
  class C extends HTMLElement {
    static get formAssociated() {
      return (hooks && hooks.formAssociated) || false;
    }

    constructor() {
      super();
      this._hooks = hooks || {};
      this._props = {};

      // Set up form association if supported
      if (C.formAssociated && this.attachInternals) {
        this._internals = this.attachInternals();

        // Store initial values for form reset
        this._initialFormState = {
          value: this.getAttribute('value') || '',
          disabled: this.hasAttribute('disabled'),
          // Store other initial attributes that should be restored on reset
          type: this.getAttribute('type') || '',
          placeholder: this.getAttribute('placeholder') || ''
        };
      }

      if (this._hooks.construct) this._hooks.construct(this);
    }

    connectedCallback() {
      // Ensure form properties are properly initialized when connected
      if (this.constructor.formAssociated) {
        // Update initial state if not already set (for dynamically created elements)
        if (!this._initialFormState) {
          this._initialFormState = {
            value: this.getAttribute('value') || '',
            disabled: this.hasAttribute('disabled'),
            type: this.getAttribute('type') || '',
            placeholder: this.getAttribute('placeholder') || ''
          };
        }
      }

      if (this._hooks.connected) this._hooks.connected(this);
    }

    disconnectedCallback() {
      if (this._hooks.disconnected) this._hooks.disconnected(this);
    }

    adoptedCallback(oldDoc, newDoc) {
      if (this._hooks.adopted) this._hooks.adopted(this, oldDoc, newDoc);
    }

    attributeChangedCallback(name, oldValue, newValue) {
      console.log(`Noticed attribute ${name} change [${oldValue}] -> [${newValue}]`)
      if (oldValue === newValue) return;

      // For form-associated elements, sync attribute changes to properties
      if (this.constructor.formAssociated) {
        const formControlProps = ['name', 'type', 'disabled', 'required', 'value'];
        if (formControlProps.includes(name)) {
          // Update property to match attribute (but avoid infinite loop)
          if (!this._syncingProperty) {
            this._syncingProperty = true;
            if (name === 'disabled' || name === 'required') {
              this[name] = newValue !== null;
            } else {
              this[name] = newValue;
            }
            this._syncingProperty = false;
          }
        }
      }

      console.log('Attributes hook: ', this._hooks.attr);
      if (this._hooks.attr) this._hooks.attr(this, name, oldValue, newValue);
    }

    // Form lifecycle callbacks - these are called by the browser automatically
    formResetCallback() {

      if (this._hooks && this._hooks.formReset) {
        // Component has custom reset logic
        this._hooks.formReset(this);
      } else {
        // Default reset behavior - restore to initial state
        const initialState = this._initialFormState || {};

        const currentValue = this.value;
        // Reset value
        const initialValue = initialState.value || '';
        this.value = initialValue;
        if (initialValue) {
          this.setAttribute('value', initialValue);
        } else {
          this.removeAttribute('value');
        }

        this.attributeChangedCallback('value', currentValue, initialValue);

        // Reset disabled state
        if (initialState.disabled) {
          this.setAttribute('disabled', '');
        } else {
          this.removeAttribute('disabled');
        }

        // Reset form value in ElementInternals
        if (this._internals) {
          this._internals.setFormValue(initialValue);
        }

        // Trigger re-render if component has render method
        if (typeof this.render === 'function') {
          this.render();
        }

        // Dispatch change event to notify of reset
        this.dispatchEvent(new CustomEvent('change', {
          bubbles: true,
          detail: { value: initialValue, reason: 'form-reset' }
        }));
      }
    }

    formDisabledCallback(disabled) {

      if (this._hooks && this._hooks.formDisabled) {
        // Component has custom disabled logic
        this._hooks.formDisabled(this, disabled);
      } else {
        // Default disabled behavior
        this.disabled = disabled;
        if (typeof this.render === 'function') {
          this.render();
        }
      }
    }

    formStateRestoreCallback(state, reason) {

      if (this._hooks && this._hooks.formStateRestore) {
        // Component has custom restore logic
        this._hooks.formStateRestore(this, state, reason);
      } else {
        // Default restore behavior
        if (state && typeof state === 'string') {
          this.value = state;
          if (this._internals) {
            this._internals.setFormValue(state);
          }
          if (typeof this.render === 'function') {
            this.render();
          }
        }
      }
    }

    static get observedAttributes() {
      return (hooks && hooks.observed) || [];
    }
  }

  // Install regular property accessors
  installPropAccessors(C.prototype, hooks && hooks.props);

  // Install form control properties for form-associated elements
  installFormControlProperties(C.prototype, hooks);

  customElements.define(tag, C);
  return C;
}

export function setProps(el, obj) {
  // Batch set: triggers prop hook per key
  if (!el._props) el._props = {};
  const hooks = el._hooks || {};
  Object.keys(obj || {}).forEach((k) => {
    const oldV = el._props[k];
    const newV = obj[k];
    el._props[k] = newV;
    if (hooks.prop) hooks.prop(el, k, oldV, newV);
  });
}

export function ensureShadow(el, mode = "open") {
  return el.shadowRoot || el.attachShadow({ mode });
}

export function setShadowHTML(el, html) {
  const root = ensureShadow(el, "open");
  root.innerHTML = html || "";
  return root;
}
