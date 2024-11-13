"use client";

import React, { useState } from "react";
import PageTitle from "@/components/page-title";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Button } from "@/components/ui/button";

/**
 * Lista de produtos disponíveis para seleção.
 */
const products = [
  { value: "1", label: "Produto 1" },
  { value: "2", label: "Produto 2" },
  { value: "3", label: "Produto 3" },
];

/**
 * Lista de lojas disponíveis para seleção.
 */
const stores = [
  { value: "A", label: "Loja A" },
  { value: "B", label: "Loja B" },
  { value: "C", label: "Loja C" },
];

/**
 * Página de teste de carga para simulação de compra de produto.
 * 
 * Esta página permite configurar e iniciar um teste de carga, ajustando
 * o número de requisições, o intervalo entre grupos de requisições, e o
 * número de requisições por grupo. Também permite selecionar o produto
 * e a loja para o teste.
 * 
 * @returns {JSX.Element} Componente que renderiza a página de teste de carga.
 */
export default function TestesPage() {
  const [numRequests, setNumRequests] = useState(100); // Número de requisições a serem realizadas
  const [requestInterval, setRequestInterval] = useState(1000); // Intervalo entre grupos de requisições (em milissegundos)
  const [requestsPerGroup, setRequestsPerGroup] = useState(10); // Número de requisições por grupo
  const [selectedProduct, setSelectedProduct] = useState(""); // Produto selecionado
  const [selectedStore, setSelectedStore] = useState(""); // Loja selecionada

  return (
    <div className="flex flex-col gap-8 p-5">
      <div className="flex justify-between items-center">
        <PageTitle title="Teste de Carga - Compra de Produto" />
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
        {/* Configuração do número de requisições */}
        <div className="flex flex-col gap-2">
          <Label htmlFor="numRequests">Número de Requisições</Label>
          <Input
            id="numRequests"
            type="number"
            value={numRequests}
            onChange={(e) => setNumRequests(parseInt(e.target.value))}
            placeholder="100"
            min="1"
          />
        </div>

        {/* Configuração do intervalo entre grupos de requisições */}
        <div className="flex flex-col gap-2">
          <Label htmlFor="requestInterval">Intervalo entre Grupos (ms)</Label>
          <Input
            id="requestInterval"
            type="number"
            value={requestInterval}
            onChange={(e) => setRequestInterval(parseInt(e.target.value))}
            placeholder="1000"
            min="0"
          />
        </div>

        {/* Configuração de requisições por grupo */}
        <div className="flex flex-col gap-2">
          <Label htmlFor="requestsPerGroup">Requisições por Grupo</Label>
          <Input
            id="requestsPerGroup"
            type="number"
            value={requestsPerGroup}
            onChange={(e) => setRequestsPerGroup(parseInt(e.target.value))}
            placeholder="10"
            min="1"
          />
        </div>

        {/* Seleção do produto */}
        <div className="flex flex-col gap-2">
          <Label htmlFor="selectProduct">Selecione o Produto</Label>
          <Select onValueChange={setSelectedProduct}>
            <SelectTrigger>
              <SelectValue placeholder="Selecione um Produto" />
            </SelectTrigger>
            <SelectContent>
              {products.map((product) => (
                <SelectItem key={product.value} value={product.value}>
                  {product.label}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        {/* Seleção da loja */}
        <div className="flex flex-col gap-2">
          <Label htmlFor="selectStore">Selecione a Loja</Label>
          <Select onValueChange={setSelectedStore}>
            <SelectTrigger>
              <SelectValue placeholder="Selecione uma Loja" />
            </SelectTrigger>
            <SelectContent>
              {stores.map((store) => (
                <SelectItem key={store.value} value={store.value}>
                  {store.label}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </div>

      {/* Botão para iniciar o teste de carga */}
      <div className="flex justify-end mt-6">
        <Button
          onClick={() => {
            console.log("Iniciar Teste de Carga");
            console.log("Produto Selecionado:", selectedProduct);
            console.log("Loja Selecionada:", selectedStore);
          }}
        >
          Iniciar Teste
        </Button>
      </div>
    </div>
  );
}
