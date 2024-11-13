import React, { useState } from "react";
import {
  ColumnDef,
  flexRender,
  SortingState,
  VisibilityState,
  ColumnFiltersState,
  PaginationState,
  getCoreRowModel,
  getSortedRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  useReactTable,
} from "@tanstack/react-table";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";
import { Search, Table as TableIcon, Grid } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ProductCard } from "./product-card";
import { Product } from "@/types/product";

interface ProductsTableProps {
  /**
   * Lista de produtos a serem exibidos na tabela ou grid.
   * @type {Product[]}
   */
  data: Product[];

  /**
   * Função a ser chamada quando um cartão de produto é clicado.
   * @param {number} id - Identificador do produto clicado.
   */
  handleCardClick: (id: number) => void;
}

/**
 * Componente que exibe uma lista de produtos em uma tabela ou grid.
 * 
 * Permite alternar entre visualizações de tabela e grade, realizar filtros globais,
 * e navegar entre páginas. Inclui um campo de pesquisa e opções de paginação.
 * 
 * @param {ProductsTableProps} props - Propriedades do componente.
 * @returns {JSX.Element} - Elemento JSX que representa a tabela ou grid de produtos.
 */
export function ProductsTable({ data, handleCardClick }: ProductsTableProps) {
  // Estado para controlar a visualização (grade ou tabela)
  const [view, setView] = useState<"grid" | "table">("grid");

  // Estado para controle de ordenação
  const [sorting, setSorting] = useState<SortingState>([]);

  // Estado para filtro global de pesquisa
  const [globalFilter, setGlobalFilter] = useState("");

  // Estado para controle de visibilidade das colunas
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({});

  // Estado para filtros das colunas
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);

  // Estado para controle de paginação
  const [pagination, setPagination] = useState<PaginationState>({
    pageIndex: 0,
    pageSize: 12, // Número padrão de produtos por página
  });

  // Definição das colunas da tabela
  const columns: ColumnDef<Product>[] = [
    {
      accessorKey: "name",
      header: "Nome",
      cell: ({ row }) => row.original.name,
    },
    {
      accessorKey: "price",
      header: "Preço (R$)",
      cell: ({ row }) => row.original.price,
    },
  ];

  // Inicialização da tabela com a biblioteca @tanstack/react-table
  const table = useReactTable({
    data,
    columns,
    state: {
      sorting,
      globalFilter,
      columnVisibility,
      columnFilters,
      pagination,
    },
    onSortingChange: setSorting,
    onGlobalFilterChange: setGlobalFilter,
    onColumnVisibilityChange: setColumnVisibility,
    onColumnFiltersChange: setColumnFilters,
    onPaginationChange: setPagination,
    getCoreRowModel: getCoreRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    pageCount: Math.ceil(data.length / pagination.pageSize),
  });

  return (
    <>
      {/* Filtros e Alternância de Visualização */}
      <div className="flex items-center justify-between">
        <div className="flex items-center py-2">
          <div className="relative">
            <Input
              placeholder="Pesquisar..."
              value={globalFilter}
              onChange={(event) => setGlobalFilter(event.target.value)}
              className="pl-10 pr-4 py-2 w-full"
            />
            <div className="absolute top-0 left-0 pl-3 pt-2">
              <Search size={20} />
            </div>
          </div>
        </div>
        <ToggleGroup
          type="single"
          value={view}
          onValueChange={(value) => setView(value as "grid" | "table")}
        >
          <ToggleGroupItem value="grid" aria-label="Toggle grid view">
            <Grid className="h-4 w-4" /> <span className="ml-1">Cards</span>
          </ToggleGroupItem>
          <ToggleGroupItem value="table" aria-label="Toggle table view">
            <TableIcon className="h-4 w-4" />{" "}
            <span className="ml-1">Tabela</span>
          </ToggleGroupItem>
        </ToggleGroup>
      </div>

      {view === "table" ? (
        <div className="rounded-md border overflow-x-auto">
          <Table>
            <TableHeader>
              {table.getHeaderGroups().map((headerGroup) => (
                <TableRow key={headerGroup.id}>
                  {headerGroup.headers.map((header) => (
                    <TableHead key={header.id}>
                      {header.isPlaceholder
                        ? null
                        : flexRender(
                            header.column.columnDef.header,
                            header.getContext()
                          )}
                    </TableHead>
                  ))}
                </TableRow>
              ))}
            </TableHeader>
            <TableBody>
              {table.getRowModel().rows?.length ? (
                table.getRowModel().rows.map((row) => (
                  <TableRow
                    key={row.id}
                    className="cursor-pointer"
                    onClick={() => handleCardClick(row.original.id)}
                  >
                    {row.getVisibleCells().map((cell) => (
                      <TableCell key={cell.id}>
                        {flexRender(
                          cell.column.columnDef.cell,
                          cell.getContext()
                        )}
                      </TableCell>
                    ))}
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell
                    colSpan={columns.length}
                    className="h-24 text-center"
                  >
                    Sem resultados.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4 mt-1 justify-center mx-auto">
          {table.getRowModel().rows?.length ? (
            table
              .getRowModel()
              .rows.map((row) => (
                <ProductCard
                  key={row.original.id}
                  id={row.original.id}
                  name={row.original.name}
                  image={row.original.linkImage}
                  price={row.original.price}
                  onClick={() => handleCardClick(row.original.id)}
                />
              ))
          ) : (
            <div className="text-center p-4 col-span-full">Sem resultados.</div>
          )}
        </div>
      )}

      {/* Paginação */}
      <div className="flex items-center justify-end space-x-2 py-4">
        <Select
          onValueChange={(value) => {
            setPagination((prev) => ({
              ...prev,
              pageSize: Number(value),
            }));
          }}
        >
          <SelectTrigger className="w-50">
            <SelectValue placeholder="Itens por página" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="5">5</SelectItem>
            <SelectItem value="10">10</SelectItem>
            <SelectItem value="12">12</SelectItem>
            <SelectItem value="20">20</SelectItem>
            <SelectItem value="50">50</SelectItem>
          </SelectContent>
        </Select>
        <Button
          variant="outline"
          size="sm"
          onClick={() =>
            setPagination((prev) => ({
              ...prev,
              pageIndex: Math.max(prev.pageIndex - 1, 0),
            }))
          }
          disabled={!table.getCanPreviousPage()}
        >
          Anterior
        </Button>
        <Button
          variant="outline"
          size="sm"
          onClick={() =>
            setPagination((prev) => ({
              ...prev,
              pageIndex: Math.min(prev.pageIndex + 1, table.getPageCount() - 1),
            }))
          }
          disabled={!table.getCanNextPage()}
        >
          Próxima
        </Button>
      </div>
    </>
  );
}
