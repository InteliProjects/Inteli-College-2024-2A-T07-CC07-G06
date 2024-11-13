'use client';

import { useState } from 'react';
import '@/styles/table-styles.css';
import {
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
  ColumnDef,
} from '@tanstack/react-table';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  DropdownMenu,
  DropdownMenuCheckboxItem,
  DropdownMenuContent,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Printer, Search } from 'lucide-react';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

/**
 * Props para o componente DataTable.
 * 
 * @template TData Tipo dos dados exibidos na tabela.
 * @template TValue Tipo dos valores de uma coluna.
 * 
 * @property {ColumnDef<TData, TValue>[]} columns - Definições das colunas da tabela.
 * @property {TData[]} data - Dados a serem exibidos na tabela.
 * @property {boolean} [enableFiltering=false] - Se verdadeiro, habilita a filtragem global.
 * @property {boolean} [enableColumnVisibility=false] - Se verdadeiro, habilita a visibilidade das colunas.
 * @property {(row: TData) => void} [onRowClick] - Função chamada ao clicar em uma linha da tabela.
 */
interface DataTableProps<TData, TValue> {
  columns: ColumnDef<TData, TValue>[];
  data: TData[];
  enableFiltering?: boolean;
  enableColumnVisibility?: boolean;
  onRowClick?: (row: TData) => void;
}

/**
 * Componente DataTable.
 * 
 * Um componente de tabela genérico que suporta filtragem global, visibilidade de colunas, e paginação.
 * 
 * @param {DataTableProps<TData, TValue>} props - Props para configurar o DataTable.
 * 
 * @returns {JSX.Element} O componente DataTable renderizado.
 */
export function DataTable<TData, TValue>({
  columns,
  data,
  enableFiltering = false,
  enableColumnVisibility = false,
  onRowClick,
}: DataTableProps<TData, TValue>) {
  const [sorting, setSorting] = useState<SortingState>([]);
  const [globalFilter, setGlobalFilter] = useState('');
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({});
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);
  const [pagination, setPagination] = useState<PaginationState>({
    pageIndex: 0,
    pageSize: 10,
  });

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
      <div>
        <div className="flex items-center justify-between">
          {/* Filtro Global */}
          {enableFiltering && (
            <div className="flex items-center justify-between">
              <div className="flex items-center py-4">
                <div className="relative max-w">
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
            </div>
          )}
        </div>

        {/* Visibilidade das Colunas */}
        {enableColumnVisibility && (
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="outline" className="ml-auto">
                Colunas
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              {table
                .getAllColumns()
                .filter((column) => column.getCanHide())
                .map((column) => {
                  return (
                    <DropdownMenuCheckboxItem
                      key={column.id}
                      className="capitalize"
                      checked={column.getIsVisible()}
                      onCheckedChange={(value) =>
                        column.toggleVisibility(!!value)
                      }
                    >
                      {column.id}
                    </DropdownMenuCheckboxItem>
                  );
                })}
            </DropdownMenuContent>
          </DropdownMenu>
        )}

        {/* Tabela - Desktop */}
        <div
          className="rounded-md border hidden md:block overflow-x-auto"
        >
          <Table>
            <TableHeader>
              {table.getHeaderGroups().map((headerGroup) => (
                <TableRow key={headerGroup.id}>
                  {headerGroup.headers.map((header) => {
                    return (
                      <TableHead key={header.id}>
                        {header.isPlaceholder
                          ? null
                          : flexRender(
                              header.column.columnDef.header,
                              header.getContext(),
                            )}
                      </TableHead>
                    );
                  })}
                </TableRow>
              ))}
            </TableHeader>
            <TableBody>
              {table.getRowModel().rows?.length ? (
                table.getRowModel().rows.map((row) => (
                  <TableRow
                    key={row.id}
                    data-state={row.getIsSelected() && 'selected'}
                    onClick={() => onRowClick && onRowClick(row.original)}
                    className="cursor-pointer"
                  >
                    {row.getVisibleCells().map((cell) => (
                      <TableCell key={cell.id}>
                        {flexRender(
                          cell.column.columnDef.cell,
                          cell.getContext(),
                        )}
                      </TableCell>
                    ))}
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={columns.length} className="h-24 text-center">
                    No results.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </div>

        {/* Tabela - Mobile */}
        <div className="block md:hidden">
          {table.getRowModel().rows?.length ? (
            table.getRowModel().rows.map((row) => (
              <div
                key={row.id}
                className="border rounded-md p-4 mb-4 relative cursor-pointer"
                onClick={() => onRowClick && onRowClick(row.original)}
              >
                {row.getVisibleCells().map((cell) => {
                  const columnDef = cell.column.columnDef;

                  // Condicional para renderizar o conteúdo da célula e cabeçalho com base em mobileHidden
                  if (columnDef.meta?.mobileHidden) return null;

                  return cell.column.id === 'actions' ? (
                    <div key={cell.id} className="card-actions">
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext(),
                      )}
                    </div>
                  ) : (
                    <div key={cell.id} className="mb-2 flex">
                      {columnDef.meta?.mobileHeader}
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext(),
                      )}
                    </div>
                  );
                })}
              </div>
            ))
          ) : (
            <div className="text-center p-4">No results.</div>
          )}
        </div>

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
                pageIndex: Math.min(
                  prev.pageIndex + 1,
                  table.getPageCount() - 1,
                ),
              }))
            }
            disabled={!table.getCanNextPage()}
          >
            Próxima
          </Button>
        </div>
      </div>
    </>
  );
}
