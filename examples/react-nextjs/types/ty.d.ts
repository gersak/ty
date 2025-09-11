// TypeScript declarations for Ty components

declare global {
  interface Window {
    ty: {
      icons: {
        add: (icons: Record<string, string>) => void;
      };
    };
  }
}

export {};
