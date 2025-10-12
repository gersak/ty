/**
 * Locale Resolution Utilities
 * 
 * Provides a cascade system for resolving locale/language preferences:
 * 1. Component's explicit `locale` attribute (highest priority)
 * 2. Closest ancestor's `lang` attribute
 * 3. Document root's `lang` attribute
 * 4. Browser's language preference
 * 5. 'en-US' fallback (lowest priority)
 * 
 * This allows developers to set locale at any level:
 * - Per-component: <ty-input locale="fr-FR">
 * - Per-section: <div lang="de-DE"><ty-input></ty-input></div>
 * - Per-page: <html lang="es-ES">
 * - Browser default: Uses navigator.language
 * 
 * @example
 * ```typescript
 * // In a component
 * class TyInput extends HTMLElement {
 *   get locale(): string {
 *     return getEffectiveLocale(this, this.getAttribute('locale'));
 *   }
 * }
 * ```
 */

/**
 * Get the effective locale for an element using the resolution cascade.
 * 
 * Resolution order:
 * 1. Explicit locale attribute on the element
 * 2. Closest ancestor element with a `lang` attribute
 * 3. Document root element's `lang` attribute
 * 4. Browser's navigator.language
 * 5. Fallback to 'en-US'
 * 
 * @param element - The element to get locale for
 * @param explicitLocale - Optional explicit locale value (from component's locale attribute)
 * @returns The resolved locale string (e.g., 'en-US', 'fr-FR')
 * 
 * @example
 * ```html
 * <html lang="en-US">
 *   <div lang="fr-FR">
 *     <ty-input></ty-input>              <!-- Uses 'fr-FR' -->
 *     <ty-input locale="de-DE"></ty-input>  <!-- Uses 'de-DE' -->
 *   </div>
 *   <ty-input></ty-input>                <!-- Uses 'en-US' -->
 * </html>
 * ```
 */
export function getEffectiveLocale(
  element: HTMLElement,
  explicitLocale?: string | null
): string {
  // 1. Explicit locale attribute takes highest priority
  if (explicitLocale) {
    return explicitLocale;
  }

  // 2. Check closest ancestor with lang attribute
  // This allows per-section locale overrides
  const langElement = element.closest('[lang]');
  if (langElement) {
    const lang = langElement.getAttribute('lang');
    if (lang) return lang;
  }

  // 3. Check document root (html element)
  if (document.documentElement.lang) {
    return document.documentElement.lang;
  }

  // 4. Use browser's language preference
  if (navigator.language) {
    return navigator.language;
  }

  // 5. Ultimate fallback
  return 'en-US';
}

/**
 * Normalize a locale string to BCP 47 format.
 * Handles common variations and ensures consistency.
 * 
 * @param locale - The locale string to normalize
 * @returns Normalized locale string
 * 
 * @example
 * ```typescript
 * normalizeLocale('en')       // 'en'
 * normalizeLocale('en_US')    // 'en-US'
 * normalizeLocale('EN-us')    // 'en-US'
 * ```
 */
export function normalizeLocale(locale: string): string {
  if (!locale) return 'en-US';
  
  // Replace underscores with hyphens (en_US -> en-US)
  let normalized = locale.replace(/_/g, '-');
  
  // Split into parts
  const parts = normalized.split('-');
  
  if (parts.length === 0) return 'en-US';
  
  // Lowercase language code (EN -> en)
  parts[0] = parts[0].toLowerCase();
  
  // Uppercase country code if present (us -> US)
  if (parts.length > 1) {
    parts[1] = parts[1].toUpperCase();
  }
  
  return parts.join('-');
}

/**
 * Check if a locale string is valid BCP 47 format.
 * 
 * @param locale - The locale string to validate
 * @returns true if valid, false otherwise
 * 
 * @example
 * ```typescript
 * isValidLocale('en-US')    // true
 * isValidLocale('fr')       // true
 * isValidLocale('invalid!') // false
 * ```
 */
export function isValidLocale(locale: string): boolean {
  if (!locale) return false;
  
  // BCP 47 language tag pattern
  // Simplified: allows language-region format
  const pattern = /^[a-z]{2,3}(-[A-Z]{2})?$/;
  return pattern.test(locale);
}

/**
 * Get the language code from a locale (e.g., 'en' from 'en-US').
 * 
 * @param locale - The locale string
 * @returns The language code
 * 
 * @example
 * ```typescript
 * getLanguageCode('en-US')  // 'en'
 * getLanguageCode('fr-FR')  // 'fr'
 * getLanguageCode('de')     // 'de'
 * ```
 */
export function getLanguageCode(locale: string): string {
  if (!locale) return 'en';
  return locale.split('-')[0].toLowerCase();
}

/**
 * Get the region/country code from a locale (e.g., 'US' from 'en-US').
 * 
 * @param locale - The locale string
 * @returns The region code, or undefined if not present
 * 
 * @example
 * ```typescript
 * getRegionCode('en-US')  // 'US'
 * getRegionCode('fr-FR')  // 'FR'
 * getRegionCode('de')     // undefined
 * ```
 */
export function getRegionCode(locale: string): string | undefined {
  if (!locale) return undefined;
  const parts = locale.split('-');
  return parts.length > 1 ? parts[1].toUpperCase() : undefined;
}

/**
 * Create a MutationObserver to watch for lang attribute changes.
 * Useful for components that need to react to dynamic locale changes.
 * 
 * @param element - The element to observe
 * @param callback - Function to call when lang changes
 * @returns Cleanup function to disconnect the observer
 * 
 * @example
 * ```typescript
 * class TyInput extends HTMLElement {
 *   private _localeObserver?: () => void;
 * 
 *   connectedCallback() {
 *     this._localeObserver = observeLocaleChanges(this, () => {
 *       this.render(); // Re-render when locale changes
 *     });
 *   }
 * 
 *   disconnectedCallback() {
 *     this._localeObserver?.();
 *   }
 * }
 * ```
 */
export function observeLocaleChanges(
  element: HTMLElement,
  callback: (newLocale: string) => void
): () => void {
  let currentLocale = getEffectiveLocale(element);

  // Create observer for lang attribute changes on ancestors
  const observer = new MutationObserver((mutations) => {
    for (const mutation of mutations) {
      if (mutation.type === 'attributes' && mutation.attributeName === 'lang') {
        const newLocale = getEffectiveLocale(element);
        if (newLocale !== currentLocale) {
          currentLocale = newLocale;
          callback(newLocale);
        }
      }
    }
  });

  // Observe document and all ancestors
  observer.observe(document.documentElement, {
    attributes: true,
    attributeFilter: ['lang'],
    subtree: true
  });

  // Return cleanup function
  return () => observer.disconnect();
}
