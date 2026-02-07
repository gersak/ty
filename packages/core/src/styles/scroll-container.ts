/**
 * TyScrollContainer styles
 * Scroll container with visual shadow indicators at top/bottom edges
 */

export const scrollContainerStyles = `
:host {
  display: block;
  position: relative;
  overflow: hidden;
}

.scroll-wrapper {
  width: 100%;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  scroll-behavior: smooth;
}

:host([max-height]) .scroll-wrapper {
  max-height: var(--scroll-max-height);
}

:host([hide-scrollbar]) .scroll-wrapper {
  scrollbar-width: none;
  -ms-overflow-style: none;
}

:host([hide-scrollbar]) .scroll-wrapper::-webkit-scrollbar {
  display: none;
}

.shadow-overlay {
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 1;
}

.shadow-top,
.shadow-bottom {
  position: absolute;
  left: 0;
  right: 0;
  opacity: 0;
  transition: var(--ty-scroll-shadow-transition, opacity 0.2s ease-out);
  pointer-events: none;
}

.shadow-top {
  top: -40px;
  height: 80px;
  background: var(--ty-scroll-shadow-top, radial-gradient(
    ellipse 100% 30% at center,
    rgba(0, 0, 0, 0.1),
    rgba(0, 0, 0, 0),
    transparent
  ));
  clip-path: inset(50% 0 0 0);
}

.shadow-bottom {
  bottom: -30px;
  height: 60px;
  background: var(--ty-scroll-shadow-bottom, radial-gradient(
    ellipse 100% 20% at center,
    rgba(0, 0, 0, 0.15),
    rgba(0, 0, 0, 0),
    transparent
  ));
  clip-path: inset(0 0 50% 0);
}

.shadow-top.visible,
.shadow-bottom.visible {
  opacity: 1;
}

:host([shadow="false"]) .shadow-overlay {
  display: none;
}
`;
