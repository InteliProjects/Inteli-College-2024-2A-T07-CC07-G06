// Importa o tipo `ReactNode` do React para definir o tipo de `children`
import { ReactNode } from "react";

// Define as propriedades aceitas pelo componente `UploadLayout`
// `children` é um nó React que será renderizado dentro do layout
interface UploadLayoutProps {
  children: ReactNode;
}

/**
 * Componente de layout para páginas de upload.
 * Este componente envolve seus filhos com padding e define a largura e altura totais.
 * 
 * @param {UploadLayoutProps} props - As propriedades que contêm os filhos a serem renderizados dentro do layout.
 * @returns {JSX.Element} Um contêiner `div` que encapsula os filhos fornecidos com estilização específica.
 */
export default async function UploadLayout({ children }: UploadLayoutProps) {
  return <div className="p-8 w-full h-full">{children}</div>;
}
