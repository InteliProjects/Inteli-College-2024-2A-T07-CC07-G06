import { cn } from '@/lib/utils';
import React from 'react';

type Props = {
  /**
   * O título a ser exibido no componente.
   * 
   * @type {string}
   */
  title: string;
  
  /**
   * Classes CSS adicionais a serem aplicadas ao componente.
   * 
   * @type {string}
   * @optional
   */
  className?: string;
};

/**
 * Componente de título de página.
 * 
 * Este componente exibe um título de página com estilo padrão, e permite
 * a adição de classes CSS adicionais através da propriedade `className`.
 * 
 * @component
 * @example
 * // Exemplo de uso do componente PageTitle
 * return <PageTitle title="Minha Página" className="text-blue-500" />;
 * 
 * @param {Props} props - Propriedades do componente.
 * @returns {React.Element} O componente `PageTitle`.
 */
export default function PageTitle({ title, className }: Props) {
  return <h1 className={cn('text-2xl font-semibold', className)}>{title}</h1>;
}
