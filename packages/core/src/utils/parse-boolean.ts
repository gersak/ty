/**
 * Parse a value as a boolean following web standards
 * 
 * This utility helps web components handle boolean attributes consistently:
 * - HTML5 boolean attributes: presence = true, absence = false, empty string = true
 * - Framework boolean values: true/false/"true"/"false"
 * - Numeric boolean values: 1/0/"1"/"0"
 * 
 * @param value - The attribute value to parse
 * @returns boolean - The parsed boolean value
 * 
 * @example
 * parseBoolean(null)       // false - attribute not present
 * parseBoolean('')         // true  - HTML boolean attribute (present but empty)
 * parseBoolean('true')     // true
 * parseBoolean('false')    // false
 * parseBoolean('TRUE')     // true  - case insensitive
 * parseBoolean('1')        // true
 * parseBoolean('0')        // false
 * parseBoolean('yes')      // true  - any other non-empty string
 * parseBoolean('anything') // true  - truthy by default unless explicitly false
 */
export function parseBoolean(value: string | null): boolean {
  // No attribute or null = false (HTML standard)
  if (value === null) return false
  
  // Empty string = true (HTML boolean attribute pattern: <input checked>)
  if (value === '') return true
  
  // Normalize to lowercase for case-insensitive comparison
  const normalized = value.toLowerCase().trim()
  
  // Explicit false values
  if (normalized === 'false' || normalized === '0') return false
  
  // Everything else is truthy (follows HTML/JavaScript conventions)
  return true
}

/**
 * Check if a value looks like a boolean string
 * Used to determine if an attribute should be treated as a boolean
 * 
 * @param value - The value to check
 * @returns boolean - True if value represents a boolean
 * 
 * @example
 * isBooleanString('true')   // true
 * isBooleanString('false')  // true
 * isBooleanString('1')      // true
 * isBooleanString('0')      // true
 * isBooleanString('custom') // false
 */
export function isBooleanString(value: string | null): boolean {
  if (value === null || value === '') return false
  const normalized = value.toLowerCase().trim()
  return ['true', 'false', '1', '0'].includes(normalized)
}
