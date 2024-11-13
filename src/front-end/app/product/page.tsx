"use client";

import React, { useState, useEffect } from "react";
import { Product } from "@/types/product";
import baseApi from "@/hooks/axios-config";
import { useRouter } from "next/navigation";
import { ShoppingCartIcon } from "lucide-react";
import PageTitle from "@/components/page-title";
import { Button } from "@/components/ui/button";
import { useSearchParams } from "next/navigation";
import FreteComponent from "@/components/frete-component";

/**
 * Página de detalhes do produto.
 * 
 * Este componente exibe os detalhes de um produto específico, incluindo uma imagem principal,
 * um carrossel de imagens adicionais, e informações sobre o produto como nome, descrição, SKU e preço.
 * Também permite calcular o frete e adicionar o produto ao carrinho de compras.
 * 
 * Utiliza `useSearchParams` para obter o ID do produto da URL, e `useRouter` para redirecionar para a página do carrinho.
 * 
 * @returns {JSX.Element} - Elemento JSX que representa a página de detalhes do produto.
 */
export default function ProductDetailsPage() {
  // Obtém o ID do produto da URL.
  const searchParams = useSearchParams();
  const productId = Number(searchParams.get("productId"));
  const router = useRouter();

  // Estado para armazenar as informações do produto.
  const [product, setProduct] = useState<Product | null>(null);

  // Efeito colateral para buscar as informações do produto quando o ID muda.
  useEffect(() => {
    async function fetchProduct() {
      if (productId) {
        const res = await baseApi.get(`/products/${productId}`);
        const fetchedProduct = res.data;
        setProduct(fetchedProduct);
      }
    }
    fetchProduct();
  }, [productId]);

  /**
   * Adiciona o produto ao carrinho e redireciona para a página do carrinho.
   * 
   * Atualiza o localStorage com a quantidade de itens no carrinho e usa o roteador para redirecionar.
   */
  const addToCart = () => {
    const cart = JSON.parse(localStorage.getItem("cart") || "{}");

    if (cart[productId]) {
      cart[productId] += 1;
    } else {
      cart[productId] = 1;
    }

    localStorage.setItem("cart", JSON.stringify(cart));
    router.push(`/cart`);
  };

  // Exibe uma mensagem de carregamento enquanto o produto está sendo buscado.
  if (!product) {
    return <div>Carregando...</div>;
  }

  // Calcula o valor das parcelas do produto.
  const parcelValue = (product.price / 12).toFixed(2);

  // Mock de imagens adicionais.
  const mockImages = new Array(3).fill(product.linkImage);

  return (
    <div className="flex flex-col gap-5 p-4 max-w-4xl mx-auto">
      <div className="flex justify-between mb-5">
        <PageTitle title="Detalhes do Produto" />
      </div>
      <div className="flex flex-col md:flex-row gap-4">
        {/* Imagem do produto */}
        <div className="flex-shrink-0 w-full md:w-1/3">
          <img
            src={product.linkImage}
            alt={product.name}
            className="w-64 h-80 object-cover rounded-lg shadow-md"
          />
          {/* Carrossel de imagens */}
          <div className="flex gap-2 mt-4 overflow-x-auto">
            {mockImages.map((img, index) => (
              <div key={index} className="flex-shrink-0 cursor-pointer">
                <img
                  src={img}
                  alt={`Imagem ${index + 1}`}
                  className="w-20 h-24 object-cover rounded-lg border border-gray-300"
                />
              </div>
            ))}
          </div>
        </div>

        {/* Detalhes do produto */}
        <div className="flex-grow flex flex-col justify-between w-full md:w-1/2">
          <div>
            <PageTitle title={product.name || "Produto"} />
            <p className="text-lg font-semibold mt-2">{product.name}</p>
            <p className="mt-2 text-gray-700">{product.description}</p>
            <p className="text-sm text-gray-500 mt-2">SKU: {product.sku}</p>

            {/* Preço e parcelamento */}
            <p className="text-2xl font-bold mt-4">
              R$ {product.price.toFixed(2)}
            </p>
            <p className="text-sm text-gray-500 mt-1">
              Em até 12x de R$ {parcelValue} sem juros
            </p>
          </div>

          <div className="flex flex-col gap-2 mt-4">
            {/* Botão para calcular o frete */}
            <FreteComponent productId={productId} />

            {/* Botão para Adicionar ao Carrinho */}
            <Button
              className="bg-green-500 text-white hover:bg-green-600"
              onClick={addToCart}
            >
              Adicionar ao Carrinho
              <ShoppingCartIcon size={16} className="ms-2" />
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
