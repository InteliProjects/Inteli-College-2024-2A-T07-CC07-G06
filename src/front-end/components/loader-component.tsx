import React from 'react';
import { ClipLoader } from 'react-spinners';

/**
 * Componente de carregamento.
 * 
 * Este componente exibe um indicador de carregamento centralizado usando o
 * `ClipLoader` do `react-spinners`. É útil para mostrar feedback visual enquanto
 * o conteúdo está sendo carregado.
 * 
 * @component
 * @example
 * // Exemplo de uso do componente LoaderComponent
 * return <LoaderComponent />;
 * 
 * @returns {React.Element} O componente `LoaderComponent`.
 */
const LoaderComponent = () => {
  return (
    <div className="flex justify-center items-center h-full">
      <ClipLoader color="#F97316" size={50} />
    </div>
  );
};

export default LoaderComponent;
