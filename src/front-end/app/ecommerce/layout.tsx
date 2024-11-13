import { ReactNode } from "react";

interface EcommerceLayoutProps {
  /**
   * Componentes filhos a serem renderizados dentro do layout.
   * @type {ReactNode}
   */
  children: ReactNode;
}

/**
 * Layout para a página de e-commerce.
 * 
 * Este componente envolve seus filhos em um container com preenchimento (padding) e tamanho de 100% da largura e altura.
 * É utilizado para fornecer uma estrutura básica para páginas de e-commerce, garantindo consistência visual.
 * 
 * @param {EcommerceLayoutProps} props - Propriedades do componente.
 * @returns {JSX.Element} - Elemento JSX que representa o layout com padding e tamanho completo.
 */
export default async function EcommerceLayout({
  children,
}: EcommerceLayoutProps) {
  return <div className="p-8 w-full h-full">{children}</div>;
}
