"use client";

import React, { useState, useEffect } from "react";
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog";
import { Product } from "@/types/product";
import baseApi from "@/hooks/axios-config";
import { useRouter } from "next/navigation";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import FreteComponent from "@/components/frete-component";

/**
 * Props para o componente FinalizePurchaseModal.
 *
 * @property {Product[]} cartItems - Itens no carrinho de compras.
 * @property {Record<number, number>} quantities - Quantidades dos produtos no carrinho, mapeadas por ID do produto.
 */
interface FinalizePurchaseModalProps {
  cartItems: Product[];
  quantities: { [key: number]: number };
  setCartItems: (items: Product[]) => void;
  setQuantities: (quantities: { [key: number]: number }) => void;
}

/**
 * Componente que exibe um modal para finalizar a compra.
 *
 * @param {FinalizePurchaseModalProps} props - Props do componente.
 * @returns {JSX.Element} - Elemento JSX para o modal de finalização de compra.
 */
export default function FinalizePurchaseModal({
  cartItems,
  quantities,
  setCartItems,
  setQuantities,
}: FinalizePurchaseModalProps) {
  // Instancia o hook useRouter
  const router = useRouter();
  // Estado para controlar a visibilidade do modal
  const [open, setOpen] = useState(false);
  // Estado para armazenar o CEP do cliente
  const [cep, setCep] = useState(""); // CEP do cliente
  // Estado para controlar o status de carregamento
  const [loading, setLoading] = useState(false);
  // Estado para armazenar mensagens de erro
  const [error, setError] = useState<string | null>(null);

  // Carrega o CEP do local storage quando o componente é montado
  useEffect(() => {
    const savedCep = localStorage.getItem("userCep");
    if (savedCep) {
      setCep(savedCep);
    }
  }, []);

  /**
   * Calcula o total da compra com base nos itens e quantidades.
   *
   * @returns {number} - Total da compra.
   */
  const calculateTotal = () => {
    return cartItems.reduce(
      (total, item) => total + item.price * (quantities[item.id] || 1),
      0
    );
  };

  /**
   * Remove todos os itens do carrinho e atualiza o localStorage.
   */
  const handleClearCart = () => {
    setCartItems([]);
    setQuantities({});
    localStorage.removeItem("cart");
  };

  /**
   * Manipula a finalização da compra.
   * Envia os dados da compra para o backend e trata a resposta.
   */
  const handlePurchase = async () => {
    // Monta o array de produtos com seus IDs e quantidades
    const products = cartItems.map((item) => ({
      productId: item.id,
      quantity: quantities[item.id] || 1,
    }));

    // Monta o payload conforme esperado pelo backend
    const payload = {
      products,
      cep, // Inclui o CEP do cliente
    };

    setLoading(true);
    try {
      const response = await baseApi.post("/purchases", payload);
      if (response.status === 200) {
      // Redireciona para a página de confirmação de pedido
      router.push("/order-confirmation");

      // Limpa o carrinho após redirecionar
      setTimeout(() => {
        handleClearCart();
      }, 0); // O timeout de 0 ms coloca a limpeza do carrinho no próximo ciclo de eventos

      setOpen(false);
      } else {
        setError("Erro ao finalizar a compra.");
      }
    } catch (err) {
      setError("Erro ao conectar ao servidor.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button className="bg-green-500 text-white hover:bg-green-600 mt-4">
          Finalizar Compra
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Finalizar Compra</DialogTitle>
        </DialogHeader>
        <div className="space-y-4">
          {/* Inputs para informações de pagamento mockadas */}
          <div>
            <label htmlFor="card-number" className="block text-sm font-medium">
              Número do Cartão
            </label>
            <Input
              id="card-number"
              type="text"
              placeholder="1234 5678 9012 3456"
            />
          </div>
          <div>
            <label htmlFor="card-name" className="block text-sm font-medium">
              Nome no Cartão
            </label>
            <Input id="card-name" type="text" placeholder="Nome completo" />
          </div>
          <div className="flex space-x-4">
            <div>
              <label
                htmlFor="expiry-date"
                className="block text-sm font-medium"
              >
                Validade
              </label>
              <Input id="expiry-date" type="text" placeholder="MM/AA" />
            </div>
            <div>
              <label htmlFor="cvv" className="block text-sm font-medium">
                CVV
              </label>
              <Input id="cvv" type="text" placeholder="123" />
            </div>
          </div>
        </div>

        {/* Exibe o total da compra */}
        <div className="flex justify-between">
          <span className="text-lg font-semibold">Total:</span>
          <span className="text-lg font-bold">
            R$ {calculateTotal().toFixed(2)}
          </span>
        </div>

        {/* Componente para exibir o cálculo do frete */}
        {/* Passa o productId para FreteComponent */}
        <FreteComponent productId={cartItems[0]?.id} /> {/* Ajustado para passar productId */}

        <DialogFooter>
          <Button
            className="bg-green-500 text-white hover:bg-green-600"
            onClick={handlePurchase}
            disabled={loading}
          >
            {loading ? "Processando..." : "Comprar"}
          </Button>
        </DialogFooter>

        {/* Exibe mensagens de erro */}
        {error && <div className="text-red-500 mt-2">{error}</div>}
      </DialogContent>
    </Dialog>
  );
}
