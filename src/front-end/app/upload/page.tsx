"use client";

import React, { useState } from "react";
import { UploadCloud } from "lucide-react";
import baseApi from "@/hooks/axios-config";
import PageTitle from "@/components/page-title";
import { Button } from "@/components/ui/button";
import { useForm, FormProvider } from "react-hook-form";
import {
  Tabs,
  TabsList,
  TabsTrigger,
  TabsContent,
} from "@/components/ui/tabs";
import {
  HoverCard,
  HoverCardContent,
  HoverCardTrigger,
} from "@/components/ui/hover-card";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import {
  Sheet,
  SheetTrigger,
  SheetContent,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

// Constante que define a quantidade de arquivos esperados
const FILE_COUNT = 5;

/**
 * Componente de upload de arquivos CSV.
 * Usuário pode enviar até 5 arquivos CSV para processar informações diversas
 * (Distribuidores, Produtos, Pedidos, etc.) e atualizar o banco de dados.
 * 
 * @returns {JSX.Element} - Página de upload de arquivos CSV.
 */
export default function UploadPage() {
  // Estados que controlam o processo de upload, erros e nomes dos arquivos.
  const [loading, setLoading] = useState(false); // Controla o estado de carregamento ao enviar os arquivos
  const [error, setError] = useState<string | null>(null); // Armazena erros de validação ou de envio
  const [files, setFiles] = useState<File[]>([]); // Armazena os arquivos enviados pelo usuário
  const [fileNames, setFileNames] = useState<string[]>(Array.from({ length: FILE_COUNT }, () => "")); // Armazena os nomes dos arquivos

  // Configuração dos métodos do formulário com valores padrão para os arquivos
  const formMethods = useForm({
    defaultValues: Object.fromEntries(Array.from({ length: FILE_COUNT }, (_, i) => [`file${i + 1}`, null])),
  });

  const handleFileChange = (index: number, file: File) => {
    const updatedFiles = [...files];
    updatedFiles[index] = file;
    setFiles(updatedFiles);

    const updatedFileNames = [...fileNames];
    updatedFileNames[index] = file.name;
    setFileNames(updatedFileNames);
  };

/**
 * Função que valida os arquivos antes de serem enviados.
 * Verifica se pelo menos 1 arquivo foi selecionado e está no formato CSV.
 * 
 * @returns {boolean} - Retorna true se pelo menos um arquivo for válido, false caso contrário.
 */
const validateFiles = () => {
  if (files.length === 0 || files.every((file) => !file || file.type !== "text/csv")) {
    setError("Por favor, envie pelo menos um arquivo no formato CSV.");
    return false;
  }
  setError(null);
  return true;
};

  /**
   * Função que lida com o envio dos arquivos para o servidor.
   * Valida os arquivos antes de enviar e faz a chamada para a API.
   */
  const submitFiles = async () => {
    if (!validateFiles()) return; // Valida os arquivos antes de prosseguir
    setLoading(true); // Indica que o processo de upload está em andamento
  
    try {
      const formData = new FormData();
  
      // Nomes personalizados dos arquivos
      const fileNames = [
        "FileDistributor",
        "FileProducts",
        "FileProductDistributor",
        "FileSalesOrders",
        "FileSalesProducts",
      ];
  
      // Adiciona os arquivos ao objeto FormData com os novos nomes
      fileTitles.forEach((_, index) => { // Apenas index é necessário agora
        formData.append(fileNames[index], files[index]); // Usa os nomes customizados
      });
  
      // Faz a requisição para a API com os arquivos no formato multipart/form-data
      const res = await baseApi.post("/csv-processor/populate-all", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
  
      // Verifica a resposta da API
      if (res.status === 202) {
        alert("Arquivos enviados com sucesso!");
      } else {
        setError("Ocorreu um erro ao enviar os arquivos.");
      }
    } catch (err) {
      setError("Erro ao conectar ao servidor."); // Define o erro de conexão
    } finally {
      setLoading(false); // Finaliza o processo de carregamento
    }
  };
  
  // Títulos das tabelas correspondentes aos arquivos CSV
  const fileTitles = [
    "Distributors",
    "Products",
    "ProductDistributor",
    "SalesOrders",
    "sales_orders_products",
  ];
  
  // Retorna o JSX para a interface de upload
  return (
    <FormProvider {...formMethods}>
      <div className="relative flex flex-col gap-5">
        <div className="flex justify-between items-center">
          <PageTitle title="Upload de CSVs" />
        </div>

        <p>
          Envie os 5 arquivos CSV necessários para atualizar o banco de dados. Clique em
          <b> Instruções</b> para verificar o formato correto de cada arquivo.
        </p>

        {/* Formulário para upload de arquivos */}
        <form
          onSubmit={formMethods.handleSubmit(submitFiles)}
          className="space-y-4"
        >
          {/* Itera sobre os campos de upload de arquivos */}
          {Array.from({ length: FILE_COUNT }, (_, index) => (
            <FormField
              key={index}
              control={formMethods.control}
              name={`file${index + 1}`}
              render={({ field }) => (
                <FormItem>
                  {/* Exibe o título do campo de acordo com o tipo de arquivo */}
                  <FormLabel className="text-primary">
                    {fileTitles[index]}
                  </FormLabel>
                  <FormControl>
                    <div className="flex flex-col items-center justify-center w-full h-32 border-2 border-dashed border-gray-400 rounded-lg cursor-pointer hover:border-gray-500 transition-colors relative">
                      {/* Ícone de upload */}
                      <UploadCloud className="w-10 h-10 text-gray-400" />
                      {/* Input de arquivo */}
                      <input
                        type="file"
                        accept=".csv"
                        className="opacity-0 absolute w-full h-full cursor-pointer"
                        onChange={(event) => {
                          const file = event.target.files?.[0];
                          if (file) {
                            field.onChange(file); // Atualiza o campo no formulário
                            handleFileChange(index, file); // Função para atualizar o estado do arquivo
                          }
                        }}
                      />
                      {/* Exibe o nome do arquivo selecionado ou mensagem padrão */}
                      <p className="mt-2 text-sm text-gray-500">
                        {fileNames[index] || "Nenhum arquivo selecionado"}
                      </p>
                    </div>
                  </FormControl>
                  {/* Mensagem de erro associada ao campo de arquivo */}
                  <FormMessage>
                    {formMethods.formState.errors[`file${index + 1}`]?.message}
                  </FormMessage>
                </FormItem>
              )}
            />
          ))}

          {/* Exibe mensagem de erro geral se houver */}  
          {error && <p className="text-red-500">{error}</p>}

          {/* Botão para enviar os arquivos */}
          <div className="flex justify-end">
            <Button className="top-0 mt-4 w-40" type="submit" disabled={loading}>
              {loading ? "Enviando..." : "Enviar Arquivos"}
            </Button>
          </div>
        </form>

        {/* Aba lateral com instruções para envio dos arquivos */}
        <Sheet>
          <SheetTrigger asChild>
            <Button className="absolute top-0 right-0 mt-0 mr-2">
              Instruções
            </Button>
          </SheetTrigger>
          {/* Conteúdo da aba lateral com instruções */}
          <SheetContent side="bottom" className="h-[40vh] overflow-auto"> {/* Adicione overflow-auto aqui */}
            <SheetHeader>
              <SheetTitle className="text-primary mb-4">
                Instruções para o envio de arquivos CSV
              </SheetTitle>
            </SheetHeader>

            {/* Tab que permite alternar entre instruções para diferentes arquivos */}   
            <Tabs defaultValue="csv1">
              <div className="overflow-x-auto"> {/* Mantém overflow-x-auto para rolagem horizontal */}
                <TabsList className="justify space-x-2">
                  {fileTitles.map((title, index) => (
                    <TabsTrigger key={index} value={`csv${index + 1}`}>
                      {title}
                    </TabsTrigger>
                  ))}
                </TabsList>
              </div>

              {/* Conteúdo das instruções para cada arquivo CSV */} 
              {fileTitles.map((title, index) => (
                <TabsContent 
                  key={index} 
                  value={`csv${index + 1}`} 
                  className="mt-4 ml-1 overflow-x-auto overflow-y-auto max-h-[30vh]"> {/* Adicione overflow para rolagem */}
                  <p>
                    Para enviar dados de {title.toLowerCase()}, certifique-se de que o formato esteja correto. Utilize o seguinte formato no arquivo CSV:
                  </p>
                  {/* Tabela com as colunas e exemplo de dados do arquivo */}
                  <div className="overflow-x-auto"> {/* Adicione overflow-x-auto aqui para a tabela */}
                    <Table>
                      <TableHeader>
                        <TableRow>
                          {Object.keys(tableColumns[title]).map((key, index) => (
                            <TableHead key={index}>
                              <HoverCard>
                                <HoverCardTrigger className="cursor-pointer">
                                  {key}
                                </HoverCardTrigger>
                                <HoverCardContent className="text-center">
                                  {tableColumns[title][key]}
                                </HoverCardContent>
                              </HoverCard>
                            </TableHead>
                          ))}
                        </TableRow>
                      </TableHeader>
                      <TableBody>
                        {exampleData[title].map((row, index) => (
                          <TableRow key={index}>
                            {Object.values(row).map((cell, index) => (
                              <TableCell key={index}>{cell}</TableCell>
                            ))}
                          </TableRow>
                        ))}
                      </TableBody>
                    </Table>
                  </div>
                </TabsContent>
              ))}
            </Tabs>
          </SheetContent>
        </Sheet>
      </div>
    </FormProvider>
  );
}

/**
 * `tableColumns` é um objeto que define a estrutura das colunas de cada tabela relacionada
 * aos dados de distribuidores, produtos, pedidos e vendas. Cada chave de tabela contém um objeto
 * que mapeia os nomes das colunas para suas descrições legíveis.
 *
 * @type {Record<string, Record<string, string>>}
 * - string: Nome da tabela (ex: "Distributor", "Products").
 * - Record<string, string>: Um objeto que mapeia cada coluna ao seu respectivo nome legível.
 */
const tableColumns: Record<string, Record<string, string>> = {
  "Distributors": {
    "name": "Nome do distribuidor",
    "cep": "CEP (Código de Endereçamento Postal) do distribuidor",
  },
  "Products": {
    "sku": "Código do Produto",
    "name": "Nome do produto",
    "price": "Preço do produto",
    "description": "Descrição do produto",
    "link_image": "URL da imagem do produto",
  },
  "ProductDistributor": {
    "distributor_id": "ID do distribuidor",
    "product_id": "ID do produto",
    "quantity_available": "Quantidade disponível",
    "quantity_reserved": "Quantidade reservada",
  },
  "SalesOrders": {
    "customer_cep": "CEP (Código de Endereçamento Postal) do cliente para entrega",
    "sale_date": "Data da venda. Formato: LocalDateTime",
    "total": "Valor total da venda",
    "first_delivery_date": "Data estimada para o início do período de entrega. Formato: `LocalDate`",
    "last_delivery_date": "Data estimada para o fim do período de entrega. Formato: `LocalDate`",
  },
  "sales_orders_products": {
    "sale_order_id": "ID do pedido",
    "product_id": "ID do produto",
    "distributors_id": "ID do distribuidor",
    "unit_price": "Preço unitário",
    "quantity": "Quantidade",
  },
};

/**
 * `exampleData` é um conjunto de dados de exemplo para cada uma das tabelas definidas em `tableColumns`.
 * Esse objeto simula o conteúdo que cada arquivo CSV deve conter para facilitar o entendimento e a
 * manipulação dos dados.
 *
 * @type {Record<string, Record<string, string | number>[]>}
 * - string: Nome da tabela (ex: "Distributor", "Products").
 * - Record<string, string | number>[]): Lista de objetos que representam linhas de dados para cada tabela.
 */
const exampleData: Record<string, Record<string, string | number>[]> = {
  "Distributors": [
    { "name": "Distribuidor A", "cep": "12345-678" },
    { "name": "Distribuidor B", "cep": "23456-789" },
    { "name": "Distribuidor C", "cep": "34567-890" },
  ],
  "Products": [
    { "sku": "SKU123", "name": "Product A", "price": "49.99", "description": "This is Product A", "link_image": "https://www.example.br/imagem.jpg" },
    { "sku": "SKU124", "name": "Product B", "price": "59.99", "description": "This is Product B", "link_image": "https://www.example.br/imagem.jpg" },
    { "sku": "SKU125", "name": "Product C", "price": "99.99", "description": "This is Product C", "link_image": "https://www.example.br/imagem.jpg" },
  ],
  "ProductDistributor": [
    { "distributor_id": "1", "product_id": "1", "quantity_available": 100, "quantity_reserved": 10 },
    { "distributor_id": "1", "product_id": "2", "quantity_available": 150, "quantity_reserved": 15 },
    { "distributor_id": "2", "product_id": "1", "quantity_available": 200, "quantity_reserved": 20 },
  ],
  "SalesOrders": [
    { "customer_cep": "12345678", "sale_date": "2024-08-01T10:15:30", "total": "299.99", "first_delivery_date": "2024-08-05", "last_delivery_date": "2024-08-08" },
    { "customer_cep": "23456781", "sale_date": "2024-08-02T11:00:00", "total": "499.99", "first_delivery_date": "2024-08-06", "last_delivery_date": "2024-08-09" },
    { "customer_cep": "45123678", "sale_date": "2024-08-03T12:30:45", "total": "199.99", "first_delivery_date": "2024-08-07", "last_delivery_date": "2024-08-10" },
  ],
  "sales_orders_products": [
    { "sale_order_id": "1", "product_id": "1", "distributors_id": "1", "quantity": 2, "unit_price": "49.99" },
    { "sale_order_id": "1", "product_id": "2", "distributors_id": "2", "quantity": 3, "unit_price": "59.99"},
    { "sale_order_id": "2", "product_id": "3", "distributors_id": "3", "quantity": 1, "unit_price": "99.99"},
  ],
};
