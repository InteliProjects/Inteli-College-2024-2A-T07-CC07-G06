import "./globals.css";
import { cn } from "@/lib/utils";
import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "react-toastify/dist/ReactToastify.css";
import { ToastContainer } from "react-toastify";
import SideNavbar from "@/components/side-navbar";

// Importa a fonte "Inter" do Google Fonts com o subconjunto "latin"
const inter = Inter({ subsets: ["latin"] });

// Configuração de metadados para a aplicação
export const metadata: Metadata = {
  title: "NSync Vivo",  // Título da página
  description: "NSync Vivo",  // Descrição da página
  icons: {
    icon: "/vivo_logo.png",  // Ícone da página
  },
};

/**
 * Componente principal de layout da aplicação.
 *
 * @param {Object} props - Props do componente.
 * @param {React.ReactNode} props.children - Conteúdo a ser exibido dentro do layout.
 * @returns {JSX.Element} - O layout da aplicação com o sidebar e o conteúdo principal.
 */
export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={cn(
          "min-h-screen w-full bg-white text-black flex flex-col md:flex-row overflow-y-auto overflow-x-hidden",
          inter.className
        )}
      >
        <ToastContainer />  {/* Componente para exibir notificações */}
        <SideNavbar />  {/* Navegação lateral */}
        <div className="flex-1 overflow-auto pt-10 md:pt-0">{children}</div> {/* Conteúdo principal */}
      </body>
    </html>
  );
}
