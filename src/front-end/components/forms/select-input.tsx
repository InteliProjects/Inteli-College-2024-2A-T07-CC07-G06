import React from 'react';
import {
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from '@/components/ui/form';
import { useController, Control, FieldValues, Path } from 'react-hook-form';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '../ui/select';

/**
 * Propriedades para o componente `SelectInput`.
 * 
 * @template TFieldValues Tipo genérico para os valores dos campos do formulário.
 */
interface SelectInputProps<TFieldValues extends FieldValues> {
  /**
   * Controle do formulário, fornecido pelo React Hook Form.
   */
  control: Control<TFieldValues>;

  /**
   * Nome do campo no formulário.
   */
  name: Path<TFieldValues>;

  /**
   * Rótulo do campo de seleção.
   */
  label: string;

  /**
   * Opções a serem exibidas no seletor.
   * Cada opção deve ter um rótulo e um valor.
   */
  options: { label: string; value: string }[];
}

/**
 * Componente de entrada de seleção para formulários.
 * 
 * Este componente utiliza o `react-hook-form` para integração com o controle do formulário e exibe um seletor com as opções fornecidas.
 * Ele também exibe mensagens de erro associadas ao campo, se houver.
 * 
 * @param {SelectInputProps<TFieldValues>} props Propriedades do componente.
 * @returns {JSX.Element} Componente que renderiza um campo de seleção integrado ao formulário.
 */
const SelectInput = <TFieldValues extends FieldValues>({
  control,
  name,
  label,
  options,
}: SelectInputProps<TFieldValues>): JSX.Element => {
  const {
    field,
    fieldState: { error },
  } = useController({
    name,
    control,
  });

  return (
    <FormItem>
      <FormLabel>{label}</FormLabel>
      <FormControl>
        <Select onValueChange={field.onChange} value={field.value}>
          <SelectTrigger>
            <SelectValue placeholder="Selecione uma opção" />
          </SelectTrigger>
          <SelectContent>
            {options.map((option) => (
              <SelectItem key={option.value} value={option.value}>
                {option.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </FormControl>
      {error && <FormMessage>{error.message}</FormMessage>}
    </FormItem>
  );
};

export default SelectInput;
