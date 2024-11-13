import React from "react";
import { ReactNode } from "react";

/**
 * Props para o componente CartLayout.
 *
 * @property {ReactNode} children - Elementos filhos a serem renderizados dentro do layout.
 */
interface CartLayoutProps {
  children: ReactNode;
}

/**
 * Componente de layout para a página do carrinho de compras.
 * 
 * Este componente envolve os elementos filhos com um contêiner que aplica padding e define a largura e altura como 100%.
 *
 * @param {CartLayoutProps} props - Props do componente.
 * @returns {JSX.Element} - Elemento JSX que renderiza o layout do carrinho de compras.
 */
export default async function CartLayout({ children }: CartLayoutProps) {
  return <div className="p-8 w-full h-full">{children}</div>;
}
