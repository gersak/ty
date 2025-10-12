/**
 * Number Formatting Utilities
 * PORTED FROM: clj/ty/i18n/number.cljs
 * 
 * Uses native Intl.NumberFormat for locale-aware number formatting
 * Supports currency, percent, compact notation, and custom precision
 */

export type NumberFormatType = 'number' | 'currency' | 'percent' | 'compact'

export interface FormatConfig {
  type: NumberFormatType
  locale?: string
  currency?: string
  precision?: number
}

/**
 * Format a number based on configuration
 * 
 * @example
 * ```typescript
 * formatNumber(1234.56, { type: 'currency', currency: 'USD', locale: 'en-US' })
 * // Returns: "$1,234.56"
 * 
 * formatNumber(0.15, { type: 'percent', precision: 2 })
 * // Returns: "15.00%"
 * 
 * formatNumber(1234567, { type: 'compact' })
 * // Returns: "1.2M"
 * ```
 */
export function formatNumber(
  value: number,
  config: FormatConfig
): string {
  const { type, locale = 'en-US', currency = 'USD', precision } = config
  
  const options: Intl.NumberFormatOptions = {}
  
  // Add precision if specified
  if (precision !== undefined) {
    options.minimumFractionDigits = precision
    options.maximumFractionDigits = precision
  }
  
  // Configure based on type
  switch (type) {
    case 'currency':
      options.style = 'currency'
      options.currency = currency
      break
      
    case 'percent':
      options.style = 'percent'
      // Note: Intl.NumberFormat expects decimal (0.15 for 15%)
      // We'll handle the division in the input component
      break
      
    case 'compact':
      options.notation = 'compact'
      options.compactDisplay = 'short'
      break
      
    case 'number':
    default:
      options.style = 'decimal'
      break
  }
  
  const formatter = new Intl.NumberFormat(locale, options)
  const formatted = formatter.format(value)
  
  // Normalize Unicode spaces for better HTML input compatibility
  // Replace narrow no-break space (U+202F) and thin space (U+2009) with regular non-breaking space (U+00A0)
  // This ensures proper rendering in HTML input elements
  return formatted
    .replace(/\u202F/g, '\u00A0')  // Narrow no-break space → non-breaking space
    .replace(/\u2009/g, '\u00A0')  // Thin space → non-breaking space
}

/**
 * Parse a numeric string to a number
 * Handles various formats and returns null if invalid
 * 
 * @example
 * ```typescript
 * parseNumericValue("1,234.56")  // Returns: 1234.56
 * parseNumericValue("$1,234.56") // Returns: 1234.56
 * parseNumericValue("15%")       // Returns: 15
 * parseNumericValue("abc")       // Returns: null
 * parseNumericValue("")          // Returns: null
 * ```
 */
export function parseNumericValue(value: string): number | null {
  if (!value || value.trim() === '') return null
  
  // Remove common formatting characters
  const cleaned = value
    .replace(/,/g, '')           // Remove thousands separators
    .replace(/\s/g, '')          // Remove spaces
    .replace(/[^\d.-]/g, '')     // Keep only digits, minus, and decimal
    .trim()
  
  if (cleaned === '' || cleaned === '-') return null
  
  const parsed = parseFloat(cleaned)
  return isNaN(parsed) ? null : parsed
}

/**
 * Check if input type requires numeric formatting
 * 
 * @example
 * ```typescript
 * shouldFormat('currency') // true
 * shouldFormat('percent')  // true
 * shouldFormat('text')     // false
 * ```
 */
export function shouldFormat(type: string): boolean {
  return ['number', 'currency', 'percent', 'compact'].includes(type)
}

/**
 * Format currency with locale
 * Convenience function for currency formatting
 */
export function formatCurrency(
  value: number,
  currency: string = 'USD',
  locale: string = 'en-US'
): string {
  return formatNumber(value, { type: 'currency', currency, locale })
}

/**
 * Format percent with locale
 * Convenience function for percent formatting
 * 
 * Note: Expects value as 0.15 for 15%
 */
export function formatPercent(
  value: number,
  locale: string = 'en-US',
  precision?: number
): string {
  return formatNumber(value, { type: 'percent', locale, precision })
}

/**
 * Format compact notation
 * Convenience function for compact formatting
 * 
 * @example
 * formatCompact(1234567) // "1.2M"
 * formatCompact(1234)    // "1.2K"
 */
export function formatCompact(
  value: number,
  locale: string = 'en-US'
): string {
  return formatNumber(value, { type: 'compact', locale })
}
