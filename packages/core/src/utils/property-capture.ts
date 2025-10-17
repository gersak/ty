/**
 * Property Capture Utility
 * 
 * Solves the problem of frameworks (React, Reagent, etc.) setting properties
 * on custom elements BEFORE connectedCallback runs.
 * 
 * PROBLEM:
 * 1. Framework creates element: document.createElement('ty-dropdown')
 * 2. Framework sets properties: element.value = "foo"
 * 3. Constructor runs and defines getters/setters
 * 4. BUT properties were already set, creating instance properties
 * 5. Instance properties shadow the class getters/setters
 * 6. connectedCallback runs - too late!
 * 
 * SOLUTION:
 * In connectedCallback, capture any pre-set properties and apply them properly.
 * 
 * @example
 * ```typescript
 * connectedCallback(): void {
 *   const preSetProps = capturePreSetProperties(this, [
 *     'value', 'clearable', 'disabled'
 *   ])
 *   
 *   if (preSetProps.value !== undefined) {
 *     this._value = preSetProps.value
 *   }
 * }
 * ```
 */

/**
 * Captured property data
 */
export interface CapturedProperty<T = any> {
  name: string
  value: T
  hadProperty: boolean
}

/**
 * Options for property capture
 */
export interface PropertyCaptureOptions {
  /**
   * Whether to delete instance properties after capture (default: true)
   * This ensures the class getter/setter will work going forward
   */
  cleanup?: boolean
}

/**
 * Capture properties that were set before connectedCallback
 * 
 * @param element - The custom element instance
 * @param propertyNames - Array of property names to check
 * @param options - Capture options
 * @returns Record of captured properties with metadata
 * 
 * @example
 * ```typescript
 * const captured = capturePreSetProperties(this, ['value', 'disabled'])
 * // { 
 * //   value: { name: 'value', value: 'foo', hadProperty: true },
 * //   disabled: { name: 'disabled', value: undefined, hadProperty: false }
 * // }
 * ```
 */
export function capturePreSetProperties(
  element: HTMLElement,
  propertyNames: string[],
  options: PropertyCaptureOptions = { cleanup: true }
): Record<string, CapturedProperty> {
  const captured: Record<string, CapturedProperty> = {}

  for (const name of propertyNames) {
    const descriptor = Object.getOwnPropertyDescriptor(element, name)

    captured[name] = {
      name,
      value: descriptor?.value,
      hadProperty: descriptor !== undefined && descriptor.value !== undefined
    }

    // Clean up instance property so class getter/setter works
    if (options.cleanup && captured[name].hadProperty) {
      delete (element as any)[name]
    }
  }

  return captured
}

/**
 * Simpler API - just get the values that were pre-set
 * 
 * @param element - The custom element instance
 * @param propertyNames - Array of property names to check
 * @param options - Capture options
 * @returns Record of property names to values (only includes properties that were set)
 * 
 * @example
 * ```typescript
 * const values = getCapturedValues(this, ['value', 'disabled'])
 * if (values.value !== undefined) {
 *   this._value = values.value
 * }
 * ```
 */
export function getCapturedValues(
  element: HTMLElement,
  propertyNames: string[],
  options?: PropertyCaptureOptions
): Record<string, any> {
  const captured = capturePreSetProperties(element, propertyNames, options)
  const values: Record<string, any> = {}

  for (const [name, data] of Object.entries(captured)) {
    if (data.hadProperty) {
      values[name] = data.value
    }
  }

  return values
}

/**
 * Mapping of public property names to private field names
 */
export type PropertyMap = Record<string, string>

/**
 * Automatically apply pre-set properties to private fields
 * 
 * @param element - The custom element instance
 * @param propertyNames - Array of property names to check
 * @param propertyMap - Optional mapping of public names to private field names
 *                      Default: adds underscore prefix (e.g., 'value' -> '_value')
 * @param options - Capture options
 * 
 * @example
 * ```typescript
 * // Simple usage (uses _propertyName convention)
 * applyPreSetProperties(this, ['value', 'disabled'])
 * // Sets this._value and this._disabled
 * 
 * // Custom mapping
 * applyPreSetProperties(this, ['value'], { value: 'internalValue' })
 * // Sets this.internalValue
 * ```
 */
export function applyPreSetProperties(
  element: any,
  propertyNames: string[],
  propertyMap?: PropertyMap,
  options?: PropertyCaptureOptions
): void {
  const captured = capturePreSetProperties(element, propertyNames, options)

  for (const [name, data] of Object.entries(captured)) {
    if (data.hadProperty) {
      // Determine the private field name
      const privateName = propertyMap?.[name] || `_${name}`
      element[privateName] = data.value
    }
  }
}

/**
 * Helper for components with standard _propertyName pattern
 * 
 * @param element - The custom element instance
 * @param propertyNames - Array of property names to check
 * @param transformer - Optional function to transform values before setting
 * @param options - Capture options
 * 
 * @example
 * ```typescript
 * connectedCallback(): void {
 *   // Basic usage
 *   captureAndApplyProperties(this, ['value', 'disabled', 'required'])
 *   
 *   // With transformation
 *   captureAndApplyProperties(this, ['value'], (name, value) => {
 *     if (name === 'value') return this.parseValue(value)
 *     return value
 *   })
 * }
 * ```
 */
export function captureAndApplyProperties(
  element: any,
  propertyNames: string[],
  transformer?: (name: string, value: any) => any,
  options?: PropertyCaptureOptions
): void {
  const captured = capturePreSetProperties(element, propertyNames, options)

  for (const [name, data] of Object.entries(captured)) {
    if (data.hadProperty) {
      const privateName = `_${name}`
      const value = transformer ? transformer(name, data.value) : data.value
      element[privateName] = value
    }
  }
}
