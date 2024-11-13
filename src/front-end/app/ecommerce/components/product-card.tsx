"use client";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Truck } from "lucide-react";

/**
 * Interface para as propriedades do componente `ProductCard`.
 * 
 * @interface ProductCardProps
 * @property {number} id - Identificador único do produto.
 * @property {string} name - Nome do produto.
 * @property {string} image - URL da imagem do produto.
 * @property {number} price - Preço do produto.
 * @property {() => void} onClick - Função a ser chamada quando o cartão é clicado.
 */
interface ProductCardProps {
  id: number;
  name: string;
  image: string;
  price: number;
  onClick: () => void;
}

/**
 * Componente de cartão de produto.
 * 
 * Renderiza um cartão com informações sobre um produto, incluindo a imagem, nome e preço. O cartão é clicável e aciona a função fornecida via `onClick`.
 * Também exibe um badge de "Frete Grátis".
 * 
 * @param {ProductCardProps} props - Propriedades do componente.
 * @returns {JSX.Element} - Elemento JSX que representa o cartão do produto.
 */
export const ProductCard: React.FC<ProductCardProps> = ({
  id,
  name,
  image,
  price,
  onClick,
}) => {
  return (
    <Card
      onClick={onClick}
      className="cursor-pointer relative w-60 h-80 flex flex-col justify-between transition-shadow hover:shadow-lg"
    >
      <CardHeader className="flex flex-col items-center justify-between p-4 pt-5">
        <img src={image} alt={name} className="h-40 w-30 object-cover mb-2" />
        <CardTitle className="text-md text-center" title={name}>
          {name.length > 30 ? `${name.substring(0, 30)}...` : name}
        </CardTitle>
      </CardHeader>

      <CardContent className="flex justify-between items-center p-3">
        {/* Badge de Frete Grátis */}
        <Badge className="bg-green-500 text-white flex items-center space-x-1">
          <Truck size={16} />
          <span>Frete Grátis</span>
        </Badge>
        <span className="text-right">{`R$ ${price.toFixed(2)}`}</span>
      </CardContent>
    </Card>
  );
};
