"use client";

import React, { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Truck } from "lucide-react";
import { Card } from "./ui/card";
import { Badge } from "./ui/badge";
import baseApi from "@/hooks/axios-config";

/**
 * Formata o CEP para o formato "XXXXX-XXX".
 * 
 * @param {string} value - O valor do CEP a ser formatado.
 * @returns {string} O CEP formatado.
 */
const formatCep = (value: string) => {
  const cleanedValue = value.replace(/\D/g, "");

  if (cleanedValue.length <= 5) {
    return cleanedValue;
  }
  return `${cleanedValue.slice(0, 5)}-${cleanedValue.slice(5, 8)}`;
};

/**
 * Componente para calcular o frete com base no CEP e ID do produto.
 * 
 * Este componente permite que o usuário insira um CEP para calcular o prazo de
 * entrega de um produto específico. O resultado é exibido junto com um badge
 * indicando frete grátis, se aplicável.
 * 
 * @component
 * @param {Object} props - Propriedades do componente.
 * @param {number} props.productId - O ID do produto para o qual o frete está sendo calculado.
 * @example
 * // Exemplo de uso do componente FreteComponent
 * return <FreteComponent productId={123} />;
 * 
 * @returns {React.Element} O componente `FreteComponent`.
 */
export default function FreteComponent({ productId }: { productId: number }) {
  /**
   * Estado para armazenar o CEP inserido pelo usuário.
   * @type {string}
   */
  const [cep, setCep] = useState("");

  /**
   * Estado para armazenar o resultado da calculadora de frete.
   * @type {string | null}
   */
  const [result, setResult] = useState<string | null>(null);

  /**
   * Estado para armazenar mensagens de erro.
   * @type {string | null}
   */
  const [error, setError] = useState<string | null>(null);

  /**
   * Hook para carregar o CEP do local storage quando o componente é montado.
   */
  useEffect(() => {
    const savedCep = localStorage.getItem("userCep");
    if (savedCep) {
      setCep(savedCep);
    }
  }, []);

  /**
   * Hook para salvar o CEP no local storage sempre que ele mudar.
   */
  useEffect(() => {
    if (cep) {
      localStorage.setItem("userCep", cep);
    }
  }, [cep]);

  /**
   * Função para calcular o frete com base no CEP e no ID do produto.
   * Realiza uma requisição para o backend e exibe o resultado ou uma mensagem de erro.
   */
  const handleCalculate = async () => {
    try {
      setError(null); // Limpa erros anteriores

      // Construir o corpo da requisição com o CEP e o productId
      const requestBody = {
        cep,
        productId,
      };

      // Realiza a requisição POST com o corpo da solicitação
      const response = await baseApi.post(`/cep/calculate-days`, requestBody);
      const { firstDay, lastDay } = response.data;

      // Formata as datas para o formato brasileiro e com o mês por extenso
      const firstFormattedDay = new Date(firstDay).toLocaleDateString("pt-BR", {
        day: "numeric",
        month: "long",
      });
      const lastFormattedDay = new Date(lastDay).toLocaleDateString("pt-BR", {
        day: "numeric",
        month: "long",
      });
      setResult(
        `O produto chega entre ${firstFormattedDay} e ${lastFormattedDay}!`
      );
    } catch (err) {
      setError("Erro ao calcular o frete. Verifique o CEP e tente novamente.");
    }
  };

  return (
    <Card className="p-4 shadow-lg w-100">
      <div className="flex flex-col gap-4">
        <div className="flex gap-2 items-center">
          <Input
            type="text"
            placeholder="Digite seu CEP"
            value={formatCep(cep)}
            onChange={(e) => setCep(e.target.value)}
            className="flex-grow"
          />
          <Button onClick={handleCalculate}>
            Calcular <Truck className="text-white ms-2" />
          </Button>
        </div>
        {result && (
          <div className="flex justify-between items-center mt-4">
            <div className="flex flex-col">
              <p className="text-gray-700 font-semibold">{result}</p>
              <p className="text-gray-500 mt-2 text-sm">
                Entregue pelos Correios
              </p>
            </div>
            <Badge className="bg-green-500 text-white flex items-center px-2 py-1">
              <Truck size={16} />
              <span className="ms-1">Frete Grátis</span>
            </Badge>
          </div>
        )}
        {error && (
          <div className="text-red-500 mt-4">
            <p>{error}</p>
          </div>
        )}
      </div>
    </Card>
  );
}
