"use client";

import React, { useState, useEffect } from "react";
import { Product } from "@/types/product";
import baseApi from "@/hooks/axios-config";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Trash2, ShoppingCart } from "lucide-react"; // Importar o ícone
import FreteComponent from "@/components/frete-component";
import PageTitle from "@/components/page-title";
import FinalizePurchaseModal from "./components/finalize-purchase-modal";

/**
 * Página do carrinho de compras.
 * 
 * Este componente gerencia a exibição dos itens no carrinho, incluindo a quantidade e a remoção de itens. Também calcula o total e exibe componentes relacionados ao frete e à finalização da compra.
 * 
 * @returns {JSX.Element} - Elemento JSX que renderiza a página do carrinho de compras.
 */
export default function CartPage() {
  const [loading, setLoading] = useState(true);
  const [cartItems, setCartItems] = useState<Product[]>([]);
  const [quantities, setQuantities] = useState<{ [key: number]: number }>({});

  useEffect(() => {
    /**
     * Função assíncrona para buscar os itens do carrinho do localStorage e atualizar o estado.
     * 
     * @async
     */
    const fetchCartItems = async () => {
      setLoading(true); // Inicia o loading
      const cart = JSON.parse(localStorage.getItem("cart") || "{}");
      const productIds = Object.keys(cart).map((id) => parseInt(id));
  
      // Se o carrinho estiver vazio, podemos parar o carregamento
      if (productIds.length === 0) {
        setLoading(false);
        setCartItems([]); // Garantir que o estado de itens do carrinho esteja vazio
        return;
      }
  
      // Se há produtos no carrinho, faz a requisição para buscá-los
      try {
        const res = await baseApi.post("/products/ids", { productsIds: productIds });
        const fetchedProducts = res.data;
        setCartItems(fetchedProducts);
        setQuantities(cart);
      } catch (error) {
        console.error("Erro ao buscar produtos:", error);
      } finally {
        setLoading(false); // Finaliza o loading após a requisição
      }
    };
  
    fetchCartItems();
  }, []);

  /**
   * Atualiza a quantidade de um item no carrinho e persiste no localStorage.
   * 
   * @param {number} id - ID do produto.
   * @param {number} quantity - Quantidade do produto.
   */
  const handleQuantityChange = (id: number, quantity: number) => {
    setQuantities((prev) => {
      const updatedQuantities = { ...prev, [id]: quantity };
      localStorage.setItem("cart", JSON.stringify(updatedQuantities));
      return updatedQuantities;
    });
  };

  /**
   * Remove um item do carrinho e atualiza o localStorage.
   * 
   * @param {number} id - ID do produto a ser removido.
   */
  const handleRemoveItem = (id: number) => {
    const updatedCartItems = cartItems.filter((item) => item.id !== id);
    const { [id]: _, ...updatedQuantities } = quantities;
    setCartItems(updatedCartItems);
    setQuantities(updatedQuantities);
    localStorage.setItem("cart", JSON.stringify(updatedQuantities));
  };

  /**
   * Calcula o total do carrinho baseado nos itens e quantidades.
   * 
   * @returns {number} - Total do carrinho formatado em reais.
   */
  const calculateTotal = () => {
    return cartItems.reduce(
      (total, item) => total + item.price * (quantities[item.id] || 1),
      0
    );
  };

  if (loading) {
    return <div>Carregando...</div>; 
  }

  return (
    <div>
      <PageTitle className="p-4 pb-0" title="Carrinho" />
      <div className="flex flex-col md:flex-row gap-4 p-4 max-w-6xl mx-auto">
        {/* Cards dos Produtos no Carrinho */}
        <div className="flex-grow">
          {cartItems.length === 0 ? (
            <div className="text-center p-20">
            {/* Ícone de carrinho com strokeWidth reduzido */}
            <ShoppingCart size={150} strokeWidth={1} className="mx-auto mb-10 text-gray-500" />
            
            {/* Mensagem com texto aumentado */}
            <p className="text-2xl font-semibold mb-2">Seu carrinho está vazio.</p>
            <p className="text-lg text-gray-600 mb-4">Adicione alguns produtos para começar a comprar.</p>
            
            {/* Botão para voltar à loja */}
            <Button
              className="mt-10"
              onClick={() => window.location.href = "/ecommerce"}
            >
              Voltar para a loja
            </Button>
          </div>
          ) : (
            cartItems.map((product) => (
              <Card
                key={product.id}
                className="mb-4 flex flex-col md:flex-row justify-between items-center p-4 w-100 max-w-3xl"
              >
                <CardHeader className="flex items-center flex-shrink-0 w-1/2">
                <img
                    src={product.linkImage}
                    alt={product.name}
                    className="h-24 w-20 object-cover rounded-md"
                  />
                  <CardTitle className="text-md ml-4">{product.name}</CardTitle>
                </CardHeader>

                <CardContent className="flex flex-col items-end justify-between w-1/2 mt-2 md:mt-0">
                  {/* Seção de quantidade e preço */}
                  <div className="flex flex-col items-end w-full">
                    <div className="flex items-center justify-end mb-2 w-full">
                      <Input
                        type="number"
                        value={quantities[product.id]}
                        onChange={(e) =>
                          handleQuantityChange(product.id, Number(e.target.value))
                        }
                        className="w-24 mr-4"
                        min={1}
                      />
                      <Button
                        variant="destructive"
                        onClick={() => handleRemoveItem(product.id)}
                        className="p-2"
                      >
                        <Trash2 size={18} />
                      </Button>
                    </div>
                    <span className="text-lg font-semibold">
                      R${" "}
                      {(product.price * (quantities[product.id] || 1)).toFixed(2)}
                    </span>
                  </div>
                </CardContent>
              </Card>
            ))
          )}
        </div>

        {/* Seção de Frete e Finalizar Compra */}
        {cartItems.length > 0 && (
          <div className="w-full md:w-2/5">
            <div className="flex flex-col p-4 border rounded-md">
              {/* Passar o productId do primeiro item do carrinho */}
              <FreteComponent productId={cartItems[0].id} />

              <div className="flex justify-between mt-4">
                <span className="text-lg font-semibold">Total:</span>
                <span className="text-lg font-bold">
                  R$ {calculateTotal().toFixed(2)}
                </span>
              </div>

              <FinalizePurchaseModal
                cartItems={cartItems}
                quantities={quantities}
                setCartItems={setCartItems}
                setQuantities={setQuantities}
              />
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
