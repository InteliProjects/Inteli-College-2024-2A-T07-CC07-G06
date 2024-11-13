import React, { Suspense } from "react";
import { ReactNode } from "react";

interface ProductDetailsLayoutProps {
  /**
   * Conteúdo a ser renderizado dentro do layout.
   * 
   * O tipo `ReactNode` permite qualquer tipo de elemento React, incluindo JSX, componentes e strings.
   */
  children: ReactNode;
}

/**
 * Layout para a página de detalhes do produto.
 * 
 * Este componente é responsável por envolver o conteúdo da página de detalhes do produto com um layout padrão.
 * Utiliza o componente `Suspense` para exibir uma mensagem de carregamento enquanto o conteúdo é carregado de forma assíncrona.
 * 
 * @param {ProductDetailsLayoutProps} props - Propriedades passadas para o layout, incluindo o conteúdo a ser renderizado.
 * @returns {JSX.Element} - Elemento JSX que representa o layout da página de detalhes do produto.
 */
export default async function ProductDetailsLayout({
  children,
}: ProductDetailsLayoutProps) {
  return (
    <div className="p-8 w-full h-full">
      <Suspense fallback={<div>Loading...</div>}>
        {children}
      </Suspense>
    </div>
  );
}
