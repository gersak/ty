/**
 * Icon Source File for Build
 * This will be bundled by esbuild into static/js/icons-bundle.js
 */

// Import all icons we need from @gersak/ty
import {
  // Core UI Icons (8)
  home,
  settings,
  searchIcon,
  check,
  x,
  plus,
  minus,
  menuIcon,

  // Theme Icons (2)
  sun,
  moon,

  // Navigation Icons (9)
  edit,
  layers,
  calendar,
  chevronDown,
  chevronUp,
  chevronLeft,
  chevronRight,
  arrowLeft,
  arrowRight,

  // Action Icons (8)
  user,
  users,
  bell,
  mail,
  trash2,
  save,
  download,
  upload,

  // Status Icons (4)
  checkCircle,
  xCircle,
  alertCircle,
  info,

  // Interactive Icons (4)
  eye,
  eyeOff,
  copy,
  share2,

  // Media Icons (3)
  play,
  pause,
  square,

  // File Icons (4)
  file,
  folder,
  clipboard,
  linkIcon,

  // Tech Icons (4)
  code,
  server,
  database,
  terminal,

  // Shapes Icons (3)
  circle,
  triangle,

  // Social Icons (3)
  github,
  heart,
  star,

  // Special Icons (4)
  zap,
  zapOff,
  sparkles,

  // Utility Icons (4)
  refreshCw,
  phone,
  target,
  activity,

  // Additional Icons (11)
  bookOpen,
  wifiOff,
  alertTriangle,
  cloud,
  messageCircle,
  thumbsUp,
  gift,
  tag,
  moreHorizontal,
  moreVertical,
  layoutGrid,
  mousePointer,
} from '@gersak/ty/icons/lucide';

/**
 * Initialize icon registry
 */
function initializeIcons() {
  // Wait for window.tyIcons to be available
  function registerIconBundle() {
    if (!window.tyIcons) {
      console.warn('⏳ Waiting for Ty components...');
      setTimeout(registerIconBundle, 50);
      return;
    }

    // Register all icons
    window.tyIcons.register({
      // Core UI Icons
      'home': home,
      'settings': settings,
      'search': searchIcon,
      'check': check,
      'x': x,
      'plus': plus,
      'minus': minus,
      'menu': menuIcon,

      // Theme Icons
      'sun': sun,
      'moon': moon,

      // Navigation Icons
      'edit': edit,
      'layers': layers,
      'calendar': calendar,
      'chevron-down': chevronDown,
      'chevron-up': chevronUp,
      'chevron-left': chevronLeft,
      'chevron-right': chevronRight,
      'arrow-left': arrowLeft,
      'arrow-right': arrowRight,

      // Action Icons
      'user': user,
      'users': users,
      'bell': bell,
      'mail': mail,
      'trash': trash2,
      'save': save,
      'download': download,
      'upload': upload,

      // Status Icons
      'check-circle': checkCircle,
      'x-circle': xCircle,
      'alert-circle': alertCircle,
      'info': info,

      // Interactive Icons
      'eye': eye,
      'eye-off': eyeOff,
      'copy': copy,
      'share': share2,

      // Media Icons
      'play': play,
      'pause': pause,
      'stop': square,

      // File Icons
      'file': file,
      'folder': folder,
      'clipboard': clipboard,
      'link': linkIcon,

      // Tech Icons
      'code': code,
      'server': server,
      'database': database,
      'terminal': terminal,

      // Shapes Icons
      'circle': circle,
      'square': square,
      'triangle': triangle,

      // Social Icons
      'github': github,
      'heart': heart,
      'star': star,

      // Special Icons
      'zap': zap,
      'zap-off': zapOff,
      'sparkles': sparkles,

      // Utility Icons
      'refresh-cw': refreshCw,
      'phone': phone,
      'target': target,
      'activity': activity,

      // Additional Icons
      'book-open': bookOpen,
      'wifi-off': wifiOff,
      'alert-triangle': alertTriangle,
      'cloud': cloud,
      'message-circle': messageCircle,
      'thumbs-up': thumbsUp,
      'gift': gift,
      'tag': tag,
      'more-horizontal': moreHorizontal,
      'more-vertical': moreVertical,
      'layout-grid': layoutGrid,
      'mouse-pointer': mousePointer,
    });

    const count = 75;
    console.log(`✅ Loaded ${count} icons from bundle (compiled)`);
    console.log('📦 Method: ESBuild bundled (no runtime CDN)');

    // Dispatch ready event
    window.dispatchEvent(new CustomEvent('ty-icons-ready', {
      detail: {
        count: count,
        method: 'compiled-bundle',
        source: 'esbuild'
      }
    }));
  }

  // Start registration when DOM is ready
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', registerIconBundle);
  } else {
    registerIconBundle();
  }
}

// Initialize icons
initializeIcons();

/**
 * Helper Functions for Development
 */

// Check if a specific icon is registered
window.checkTyIcon = function(iconName) {
  if (!window.tyIcons) {
    console.error('❌ Ty icon system not loaded');
    return false;
  }

  const hasIcon = window.tyIcons.has(iconName);
  console.log(`🔍 Icon "${iconName}":`, hasIcon ? 'Available ✅' : 'Not found ❌');
  return hasIcon;
};

// List all registered icons
window.listTyIcons = function() {
  if (!window.tyIcons) {
    console.error('❌ Ty icon system not loaded');
    return [];
  }

  const icons = window.tyIcons.list();
  console.log('📋 Available Ty icons:', icons.sort());
  console.log(`Total: ${icons.length} icons`);
  return icons;
};
