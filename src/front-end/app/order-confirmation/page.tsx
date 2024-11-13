"use client";

import { useParams, useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { CheckCircle } from "lucide-react";

export default function ConfirmationPage() {
  const { orderId } = useParams(); // Obtém o parâmetro orderId da URL
  const router = useRouter();

  const handleGoBackToShop = () => {
    router.push("/ecommerce"); 
  };

  return (
    <div className="p-8 w-full h-full flex flex-col items-center justify-center">
      {/* Ícone de sucesso */}
      <CheckCircle className="w-16 h-16 text-green-500 mb-4" />

      <h1 className="text-3xl font-bold text-center">Compra Finalizada!</h1>
      <p className="mt-4 text-lg text-center">
        Seu pedido foi concluído com sucesso{" "}
        <span className="font-semibold">{orderId}</span>.
      </p>

      {/* Ações adicionais */}
      <div className="mt-8 space-x-4">
        <Button onClick={handleGoBackToShop}>
          Continuar Comprando
        </Button>
      </div>
    </div>
  );
}
