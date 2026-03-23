/**
 * Mobile detection utility
 *
 * Uses CSS media query via matchMedia for reliable detection:
 * - `pointer: coarse` identifies touch-primary devices (not laptops with touchscreens)
 * - Breakpoint is evaluated by the browser, always in sync with CSS
 * - Called on each use (not cached), so it responds to viewport changes and rotation
 */

/**
 * Default mobile breakpoint (px).
 * Matches phones in portrait and most phones in landscape.
 */
const MOBILE_BREAKPOINT = 768;

/**
 * Detect mobile touch devices.
 * Returns true when the primary pointer is coarse (touch) AND viewport is narrow.
 */
export function isMobileTouch(breakpoint: number = MOBILE_BREAKPOINT): boolean {
  return window.matchMedia(`(pointer: coarse) and (max-width: ${breakpoint}px)`).matches;
}
