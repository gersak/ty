/**
 * Icon Registry System
 * Simple icon storage and retrieval
 * PORTED FROM: clj/ty/icons.cljs
 */

/** Icon registry - maps icon names to SVG strings */
const iconRegistry = new Map<string, string>()

/** Watchers for registry changes */
const watchers = new Map<string, (icons: Map<string, string>) => void>()

/**
 * Register multiple icons at once
 * @param icons Object mapping icon names to SVG strings
 */
export function registerIcons(icons: Record<string, string>): void {
  Object.entries(icons).forEach(([name, svg]) => {
    iconRegistry.set(name, svg)
  })
  
  // Notify watchers
  notifyWatchers()
}

/**
 * Register a single icon
 * @param name Icon name
 * @param svg SVG string
 */
export function registerIcon(name: string, svg: string): void {
  iconRegistry.set(name, svg)
  notifyWatchers()
}

/**
 * Lookup an icon by name
 * @param name Icon name
 * @returns SVG string or undefined
 */
export function getIcon(name: string): string | undefined {
  return iconRegistry.get(name)
}

/**
 * Check if an icon exists
 * @param name Icon name
 * @returns true if icon is registered
 */
export function hasIcon(name: string): boolean {
  return iconRegistry.has(name)
}

/**
 * Clear all registered icons
 */
export function clearIcons(): void {
  iconRegistry.clear()
  notifyWatchers()
}

/**
 * Add a watcher for registry changes
 * @param id Unique watcher ID
 * @param callback Function to call when registry changes
 */
export function addWatcher(id: string, callback: (icons: Map<string, string>) => void): void {
  watchers.set(id, callback)
}

/**
 * Remove a watcher
 * @param id Watcher ID to remove
 */
export function removeWatcher(id: string): void {
  watchers.delete(id)
}

/**
 * Notify all watchers of registry changes
 */
function notifyWatchers(): void {
  watchers.forEach(callback => {
    callback(new Map(iconRegistry))
  })
}

/**
 * Get all registered icon names
 * @returns Array of icon names
 */
export function getIconNames(): string[] {
  return Array.from(iconRegistry.keys())
}

/**
 * Get registry size
 * @returns Number of registered icons
 */
export function getIconCount(): number {
  return iconRegistry.size
}
