/**
 * JavaScript Reserved Words and Common Conflicts
 * 
 * This list includes:
 * - ECMAScript reserved keywords
 * - Future reserved words
 * - Global objects and common conflicts
 */

export const JS_RESERVED_WORDS = new Set([
  // ECMAScript Keywords
  'break', 'case', 'catch', 'class', 'const', 'continue', 'debugger', 
  'default', 'delete', 'do', 'else', 'export', 'extends', 'finally', 
  'for', 'function', 'if', 'import', 'in', 'instanceof', 'new', 
  'return', 'super', 'switch', 'this', 'throw', 'try', 'typeof', 
  'var', 'void', 'while', 'with', 'yield',
  
  // Future Reserved Words
  'enum', 'implements', 'interface', 'let', 'package', 'private', 
  'protected', 'public', 'static', 'await', 'abstract', 'boolean', 
  'byte', 'char', 'double', 'final', 'float', 'goto', 'int', 'long', 
  'native', 'short', 'synchronized', 'throws', 'transient', 'volatile',
  
  // Global Objects (commonly conflicting)
  'Array', 'Date', 'Error', 'Function', 'Number', 'Object', 'String', 
  'Boolean', 'Symbol', 'Map', 'Set', 'WeakMap', 'WeakSet', 'Promise',
  'Proxy', 'Reflect', 'Math', 'JSON', 'Infinity', 'NaN', 'undefined',
  
  // Common DOM/Browser globals that might conflict
  'window', 'document', 'console', 'alert', 'confirm', 'prompt',
  'setTimeout', 'setInterval', 'clearTimeout', 'clearInterval',
  
  // Common icon names that conflict
  'filter', 'map', 'send', 'link', 'image', 'video', 'audio',
  'select', 'input', 'form', 'table', 'list', 'menu', 'option',
  'group', 'meta', 'type', 'name', 'id', 'style', 'title',
  'comment', 'repeat', 'key', 'value', 'get', 'set', 'test',
  'sort', 'print', 'match', 'replace', 'search', 'split', 'join',
  'reverse', 'compare', 'update', 'remove', 'add', 'push', 'pop',
  'shift', 'unshift', 'slice', 'splice', 'concat', 'includes',
  'find', 'findIndex', 'every', 'some', 'reduce', 'fill',
  'entries', 'keys', 'values', 'length', 'constructor', 'prototype',
  'caller', 'arguments', 'apply', 'call', 'bind', 'toString',
  'valueOf', 'hasOwnProperty', 'isPrototypeOf', 'propertyIsEnumerable'
])

/**
 * Check if a name is a reserved word
 */
export function isReservedWord(name) {
  return JS_RESERVED_WORDS.has(name)
}

/**
 * Make identifier safe by adding suffix if reserved
 * 
 * Examples:
 * - 'try' -> 'tryIcon'
 * - 'delete' -> 'deleteIcon'
 * - 'class' -> 'classIcon'
 * - 'check' -> 'check' (no change)
 */
export function makeSafeIdentifier(identifier) {
  if (isReservedWord(identifier)) {
    return identifier + 'Icon'
  }
  return identifier
}
