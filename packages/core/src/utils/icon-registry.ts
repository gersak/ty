/**
 * Icon Registry System
 * Simple icon storage and retrieval
 * PORTED FROM: clj/ty/icons.cljs
 */

/** Icon registry - maps icon names to SVG strings */
const iconRegistry = new Map<string, string>()

/** Watchers for registry changes - now receives Set of changed icon names */
const watchers = new Map<string, (changedIcons: Set<string>) => void>()

/** Track which icons each watcher cares about (for selective notification) */
const watcherIconNames = new Map<string, string>()

/** Pending notifications - batched for performance */
let pendingNotifications: Set<string> | null = null
let notificationTimer: number | null = null

/**
 * Register multiple icons at once
 * @param icons Object mapping icon names to SVG strings
 */
export function registerIcons(icons: Record<string, string>): void {
  const changedIcons = new Set<string>()
  
  Object.entries(icons).forEach(([name, svg]) => {
    iconRegistry.set(name, svg)
    changedIcons.add(name)
  })
  
  // Notify watchers with changed icon names (batched)
  scheduleNotification(changedIcons)
}

/**
 * Register a single icon
 * @param name Icon name
 * @param svg SVG string
 */
export function registerIcon(name: string, svg: string): void {
  iconRegistry.set(name, svg)
  scheduleNotification(new Set([name]))
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
  const allIcons = new Set(iconRegistry.keys())
  iconRegistry.clear()
  scheduleNotification(allIcons)
}

/**
 * Add a watcher for registry changes
 * @param id Unique watcher ID
 * @param iconName Optional icon name to watch (for selective notification)
 * @param callback Function to call when watched icons change
 */
export function addWatcher(
  id: string, 
  iconName: string | undefined,
  callback: (changedIcons: Set<string>) => void
): void {
  watchers.set(id, callback)
  if (iconName) {
    watcherIconNames.set(id, iconName)
  }
}

/**
 * Remove a watcher
 * @param id Watcher ID to remove
 */
export function removeWatcher(id: string): void {
  watchers.delete(id)
  watcherIconNames.delete(id)
}

/**
 * Schedule notification for changed icons (batched and deferred)
 */
function scheduleNotification(changedIcons: Set<string>): void {
  // Accumulate changed icons
  if (!pendingNotifications) {
    pendingNotifications = new Set()
  }
  
  changedIcons.forEach(name => pendingNotifications!.add(name))
  
  // Cancel existing timer
  if (notificationTimer !== null) {
    clearTimeout(notificationTimer)
  }
  
  // Schedule notification using requestIdleCallback or setTimeout
  const scheduleCallback = typeof requestIdleCallback !== 'undefined'
    ? requestIdleCallback
    : (cb: IdleRequestCallback) => setTimeout(cb, 0)
  
  notificationTimer = scheduleCallback(() => {
    const toNotify = pendingNotifications!
    pendingNotifications = null
    notificationTimer = null
    
    notifyWatchers(toNotify)
  }) as number
}

/**
 * Notify watchers of changed icons (selective notification)
 */
function notifyWatchers(changedIcons: Set<string>): void {
  watchers.forEach((callback, watcherId) => {
    const watchedIcon = watcherIconNames.get(watcherId)
    
    // If watcher is watching a specific icon, only notify if that icon changed
    if (watchedIcon) {
      if (changedIcons.has(watchedIcon)) {
        callback(new Set([watchedIcon]))
      }
    } else {
      // Watcher wants all changes
      callback(changedIcons)
    }
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