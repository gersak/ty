// wc-shim.js
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

export function define(tag, hooks) {
  // hooks: { observed, construct(el), connected(el), disconnected(el), adopted(el, oldDoc, newDoc),
  //          attr(el, name, oldV, newV), prop(el, key, oldV, newV) }
  class C extends HTMLElement {
    constructor() {
      super();
      this._hooks = hooks || {};
      this._props = {};
      if (this._hooks.construct) this._hooks.construct(this);
    }
    connectedCallback() {
      if (this._hooks.connected) this._hooks.connected(this);
    }
    disconnectedCallback() {
      if (this._hooks.disconnected) this._hooks.disconnected(this);
    }
    adoptedCallback(oldDoc, newDoc) {
      if (this._hooks.adopted) this._hooks.adopted(this, oldDoc, newDoc);
    }
    attributeChangedCallback(name, oldValue, newValue) {
      if (oldValue === newValue) return;
      if (this._hooks.attr) this._hooks.attr(this, name, oldValue, newValue);
    }
    static get observedAttributes() {
      return (hooks && hooks.observed) || [];
    }
  }
  // Optional: install property accessors for direct JS/CLJS property usage
  installPropAccessors(C.prototype, hooks && hooks.props);
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
