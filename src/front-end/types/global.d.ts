import "@tanstack/react-table";

declare module "@tanstack/react-table" {
  interface ColumnMeta {
    mobileHidden?: boolean;
    mobileHeader?: React.ReactNode;
  }
}
