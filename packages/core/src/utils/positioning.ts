/**
 * Core positioning engine for floating elements
 * Adapted from ty.positioning ClojureScript implementation
 * 
 * Handles smart positioning of tooltips, dropdowns, popups, and other
 * floating elements relative to anchor elements with automatic overflow
 * detection and placement fallback.
 */

// ============================================================================
// Types
// ============================================================================

/**
 * Placement orientation - how the floating element is positioned
 */
export type PlacementOrientation = 'horizontal' | 'vertical';

/**
 * Vertical alignment options
 */
export type VerticalAlign = 'top' | 'center' | 'bottom' | 'end';

/**
 * Horizontal alignment options
 */
export type HorizontalAlign = 'start' | 'center' | 'end';

/**
 * All available placement options
 */
export type Placement = 
  | 'top-start' | 'top' | 'top-end'
  | 'right-start' | 'right' | 'right-end'
  | 'bottom-start' | 'bottom' | 'bottom-end'
  | 'left-start' | 'left' | 'left-end';

/**
 * Placement configuration
 */
export interface PlacementConfig {
  vertical: VerticalAlign;
  horizontal: HorizontalAlign;
  orientation?: PlacementOrientation;
}

/**
 * Element rectangle with calculated center points
 */
export interface ElementRect {
  top: number;
  left: number;
  right: number;
  bottom: number;
  width: number;
  height: number;
  centerX: number;
  centerY: number;
}

/**
 * Viewport dimensions and scroll position
 */
export interface ViewportRect {
  width: number;
  height: number;
  scrollX: number;
  scrollY: number;
}

/**
 * Overflow data for all edges
 */
export interface OverflowData {
  top: number;
  left: number;
  bottom: number;
  right: number;
}

/**
 * Calculated position result
 */
export interface PositionResult {
  x: number;
  y: number;
  placement: Placement;
  overflow: OverflowData;
  overflowAmount: number;
  fits: boolean;
}

/**
 * Options for findBestPosition
 */
export interface PositionOptions {
  targetEl: HTMLElement;
  floatingEl: HTMLElement;
  preferences?: Placement[];
  offset?: number;
  padding?: number;
  containerPadding?: number;
}

/**
 * Options for calculatePlacement
 */
export interface CalculatePlacementOptions {
  targetRect: ElementRect;
  floatingRect: ElementRect;
  placement: Placement;
  offset: number;
  padding: number;
  scrollbarWidth: number;
  containerPadding: number;
}

/**
 * Cleanup function type
 */
export type CleanupFn = () => void;

// ============================================================================
// Placement Definitions
// ============================================================================

/**
 * Map of all placement configurations
 */
export const placements: Record<Placement, PlacementConfig> = {
  'top-start': {
    vertical: 'top',
    horizontal: 'start',
  },
  'top': {
    vertical: 'top',
    horizontal: 'center',
  },
  'top-end': {
    vertical: 'top',
    horizontal: 'end',
  },
  'right-start': {
    vertical: 'center',
    horizontal: 'end',
    orientation: 'vertical',
  },
  'right': {
    vertical: 'center',
    horizontal: 'end',
    orientation: 'vertical',
  },
  'right-end': {
    vertical: 'end',
    horizontal: 'end',
    orientation: 'vertical',
  },
  'bottom-start': {
    vertical: 'bottom',
    horizontal: 'start',
  },
  'bottom': {
    vertical: 'bottom',
    horizontal: 'center',
  },
  'bottom-end': {
    vertical: 'bottom',
    horizontal: 'end',
  },
  'left-start': {
    vertical: 'center',
    horizontal: 'start',
    orientation: 'vertical',
  },
  'left': {
    vertical: 'center',
    horizontal: 'start',
    orientation: 'vertical',
  },
  'left-end': {
    vertical: 'end',
    horizontal: 'start',
    orientation: 'vertical',
  },
};

/**
 * Default placement preference lists for different use cases
 */
export const placementPreferences = {
  default: [
    'bottom-start', 'bottom-end', 'top-start', 'top-end',
    'bottom', 'top', 'left', 'right'
  ] as Placement[],
  tooltip: [
    'top', 'bottom', 'left', 'right',
    'top-start', 'top-end', 'bottom-start', 'bottom-end'
  ] as Placement[],
  dropdown: [
    'bottom-start', 'bottom-end', 'top-start', 'top-end',
    'bottom', 'top', 'right', 'left'
  ] as Placement[],
};

// ============================================================================
// DOM Measurement Helpers
// ============================================================================

/**
 * Get element dimensions relative to viewport with calculated center points
 */
export function getElementRect(el: HTMLElement): ElementRect {
  const rect = el.getBoundingClientRect();
  return {
    top: rect.top,
    left: rect.left,
    right: rect.right,
    bottom: rect.bottom,
    width: rect.width,
    height: rect.height,
    centerX: rect.left + rect.width / 2,
    centerY: rect.top + rect.height / 2,
  };
}

/**
 * Get viewport dimensions and scroll position
 */
export function getViewportRect(): ViewportRect {
  return {
    width: window.innerWidth,
    height: window.innerHeight,
    scrollX: window.scrollX,
    scrollY: window.scrollY,
  };
}

// ============================================================================
// Position Calculation
// ============================================================================

/**
 * Calculate position for a specific placement
 * Returns x, y coordinates and overflow information
 */
export function calculatePlacement(options: CalculatePlacementOptions): PositionResult {
  const {
    targetRect,
    floatingRect,
    placement,
    offset,
    padding,
    scrollbarWidth,
    containerPadding,
  } = options;

  const config = placements[placement];
  const { vertical, horizontal, orientation } = config;
  const viewport = getViewportRect();

  // Calculate X position
  let x: number;
  if (orientation === 'vertical') {
    // Left/right placements
    if (horizontal === 'start') {
      x = targetRect.left - floatingRect.width - offset + containerPadding;
    } else {
      x = targetRect.right + offset - containerPadding;
    }
  } else {
    // Top/bottom placements
    if (horizontal === 'start') {
      x = targetRect.left;
    } else if (horizontal === 'center') {
      x = targetRect.centerX - floatingRect.width / 2;
    } else {
      x = targetRect.right - floatingRect.width;
    }
  }

  // Calculate Y position
  let y: number;
  if (orientation === 'vertical') {
    // Left/right placements
    if (vertical === 'center') {
      y = targetRect.centerY - floatingRect.height / 2;
    } else if (vertical === 'end') {
      y = targetRect.bottom - floatingRect.height - containerPadding;
    } else {
      y = targetRect.top;
    }
  } else {
    // Top/bottom placements
    if (vertical === 'top') {
      y = targetRect.top - floatingRect.height - offset + containerPadding;
    } else {
      y = targetRect.bottom + offset - containerPadding;
    }
  }

  // Calculate overflow for each edge
  const overflow: OverflowData = {
    top: Math.min(0, y - padding),
    left: Math.min(0, x - padding),
    bottom: Math.min(0, viewport.height - (y + floatingRect.height + padding)),
    right: Math.min(0, viewport.width - (x + floatingRect.width + padding + scrollbarWidth)),
  };

  // Sum of absolute overflow values
  const overflowAmount = Object.values(overflow).reduce(
    (sum, val) => sum + Math.abs(val),
    0
  );

  return {
    x: Math.round(x),
    y: Math.round(y),
    placement,
    overflow,
    overflowAmount,
    fits: overflowAmount === 0,
  };
}

/**
 * Find the best position for the floating element
 * Tries all preference placements and returns the one that fits best
 */
export function findBestPosition(options: PositionOptions): PositionResult {
  const {
    targetEl,
    floatingEl,
    preferences = placementPreferences.default,
    offset = 8,
    padding = 8,
    containerPadding = 0,
  } = options;

  const targetRect = getElementRect(targetEl);
  const floatingRect = getElementRect(floatingEl);
  const scrollbarWidth = 15;

  // Calculate all candidate positions
  const candidates = preferences.map(placement =>
    calculatePlacement({
      targetRect,
      floatingRect,
      placement,
      offset,
      padding,
      containerPadding,
      scrollbarWidth,
    })
  );

  // Find first that fits, or one with least overflow
  const bestPosition = candidates.find(c => c.fits) ||
    candidates.reduce((best, current) =>
      current.overflowAmount < best.overflowAmount ? current : best
    );

  // Adjust for scrollbar if needed
  if (bestPosition.overflow.right < 0) {
    bestPosition.x += bestPosition.overflow.right;
  }

  return bestPosition;
}

// ============================================================================
// Auto-Update Functionality
// ============================================================================

/**
 * Create auto-update system for position tracking
 * Continuously monitors and updates position when target or floating element changes
 * 
 * @param targetEl - The anchor element to position relative to
 * @param floatingEl - The floating element to position
 * @param updateFn - Callback function that receives new position data
 * @param config - Position options (same as findBestPosition)
 * @returns Cleanup function to stop auto-updating
 */
export function autoUpdate(
  targetEl: HTMLElement,
  floatingEl: HTMLElement,
  updateFn: (position: PositionResult) => void,
  config: Omit<PositionOptions, 'targetEl' | 'floatingEl'>
): CleanupFn {
  let active = true;
  let scrollRafId: number | null = null;
  let resizeTimeout: number | null = null;
  let resizeObserver: ResizeObserver | null = null;
  let mutationObserver: MutationObserver | null = null;

  // Update function that recalculates position
  const update = () => {
    if (!active) return;
    const position = findBestPosition({
      targetEl,
      floatingEl,
      ...config,
    });
    updateFn(position);
  };

  // Initial update
  update();

  // Debounced update for resize events
  const debouncedUpdate = () => {
    if (resizeTimeout !== null) {
      clearTimeout(resizeTimeout);
    }
    resizeTimeout = window.setTimeout(() => {
      resizeTimeout = null;
      update();
    }, 10);
  };

  // RAF-based scroll handler for smooth updates
  const handleScroll = () => {
    if (!active) return;
    if (scrollRafId === null) {
      scrollRafId = requestAnimationFrame(() => {
        scrollRafId = null;
        update();
      });
    }
  };

  // Observe size changes on target, floating element, and body
  resizeObserver = new ResizeObserver(debouncedUpdate);
  resizeObserver.observe(targetEl);
  resizeObserver.observe(floatingEl);
  resizeObserver.observe(document.body);

  // Observe DOM attribute changes on target
  mutationObserver = new MutationObserver(update);
  mutationObserver.observe(targetEl, {
    attributes: true,
    attributeFilter: ['class', 'style'],
  });

  // Listen for scroll events (capture phase for better performance)
  window.addEventListener('scroll', handleScroll, true);
  window.addEventListener('resize', debouncedUpdate);

  // Return cleanup function
  return () => {
    active = false;
    if (scrollRafId !== null) {
      cancelAnimationFrame(scrollRafId);
    }
    if (resizeTimeout !== null) {
      clearTimeout(resizeTimeout);
    }
    if (resizeObserver) {
      resizeObserver.disconnect();
    }
    if (mutationObserver) {
      mutationObserver.disconnect();
    }
    window.removeEventListener('scroll', handleScroll, true);
    window.removeEventListener('resize', debouncedUpdate);
  };
}
