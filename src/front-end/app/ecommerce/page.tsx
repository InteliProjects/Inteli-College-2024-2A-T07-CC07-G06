"use client";

import React, { useEffect, useState } from "react";
import { ShoppingCart } from "lucide-react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import PageTitle from "@/components/page-title";
import { useProducts } from "@/hooks/use-products";
import LoaderComponent from "@/components/loader-component";
import { ProductsTable } from "./components/products-table";

export default function EcommercePage() {
  const { products, isLoading, isError } = useProducts();
  const [cartItemsCount, setCartItemsCount] = useState(0); // Estado para armazenar o número total de itens no carrinho
  const router = useRouter();

  /**
   * Manipulador de clique para redirecionar para a página de detalhes do produto.
   * 
   * @param {number} id - ID do produto a ser exibido.
   */
  useEffect(() => {
    /**
     * Função assíncrona para buscar itens do carrinho no localStorage e calcular o total de itens.
     */
    const fetchCartItems = () => {
      const cart = JSON.parse(localStorage.getItem("cart") || "{}");
      const totalItems = Object.values(cart).reduce(
        (total: number, quantity: any) => total + quantity,
        0
      );
      setCartItemsCount(totalItems); // Atualiza o número de itens no carrinho
    };

    fetchCartItems();
  }, []);

  const handleCardClick = (id: number) => {
    router.push(`/product/?productId=${id}`);
  };

  // Exibe um componente de carregamento enquanto os dados estão sendo carregados
  if (isLoading) {
    return <LoaderComponent />;
  }

  // Exibe uma mensagem de erro se houver um problema ao carregar os dados
  if (isError) {
    return "Erro :(";
  }

  return (
    <div className="flex flex-col gap-5">
      <div className="flex justify-between items-center">
        <PageTitle title="E-Commerce" />
        <Button
          onClick={() => router.push("/cart")}
          className="relative flex items-center justify-center"
        >
          <ShoppingCart className="mr-2" />
          {cartItemsCount > 0 && (
            <span className="absolute top-[-5px] right-[-5px] bg-red-500 text-white rounded-full w-5 h-5 text-xs flex items-center justify-center">
              {cartItemsCount}
            </span>
          )}
          Carrinho
        </Button>
      </div>
      <ProductsTable data={products || []} handleCardClick={handleCardClick} />
    </div>
  );
}
