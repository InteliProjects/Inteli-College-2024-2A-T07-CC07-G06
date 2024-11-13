import { ReactNode } from "react";

interface TestesLayoutProps {
  /**
   * O conteúdo a ser renderizado dentro do layout.
   * @type {ReactNode}
   */
  children: ReactNode;
}

/**
 * Componente de layout para a página de testes.
 *
 * Este componente fornece uma estrutura básica para a página de testes, aplicando
 * padding e definindo a altura e largura do contêiner.
 *
 * @param {TestesLayoutProps} props - Propriedades do componente.
 * @param {ReactNode} props.children - Conteúdo a ser exibido dentro do layout.
 * @returns {Promise<JSX.Element>} - Uma promessa que resolve para o JSX do layout.
 */
export default async function TestesLayout({
  children,
}: TestesLayoutProps): Promise<JSX.Element> {
  return <div className="p-8 w-full h-full">{children}</div>;
}
