"use client";

import { redirect } from "next/navigation";

/**
 * Componente Home.
 *
 * Este componente redireciona o usuário para a página "/ecommerce" quando é renderizado.
 *
 * @returns {null} - Não renderiza nada, apenas realiza o redirecionamento.
 */
export default function Home() {
  // Redireciona o usuário para a página "/ecommerce"
  redirect("/ecommerce");
  return null; // O componente não renderiza nenhum conteúdo
}
