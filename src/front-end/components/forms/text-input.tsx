import React from 'react';
import {
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from '@/components/ui/form';
import {
  useController,
  Control,
  FieldValues,
  Path,
  PathValue,
} from 'react-hook-form';
import { Input } from '../ui/input';

/**
 * Propriedades para o componente `TextInput`.
 * 
 * @template TFieldValues Tipo genérico para os valores dos campos do formulário.
 */
interface TextInputProps<TFieldValues extends FieldValues> {
  /**
   * Controle do formulário, fornecido pelo React Hook Form.
   * 
   * @type {Control<TFieldValues>}
   */
  control: Control<TFieldValues>;

  /**
   * Nome do campo no formulário.
   * 
   * @type {Path<TFieldValues>}
   */
  name: Path<TFieldValues>;

  /**
   * Rótulo do campo de texto.
   * 
   * @type {string}
   */
  label: string;

  /**
   * Texto de espaço reservado para o campo de texto.
   * 
   * @type {string}
   */
  placeholder: string;

  /**
   * Regras de validação para o campo de texto.
   * Utilizado pelo `react-hook-form` para validar o campo.
   * 
   * @type {any} 
   * @default undefined
   */
  rules?: any; // Pode ser mais específico dependendo das regras de validação usadas

  /**
   * Indica se o campo deve ser somente leitura.
   * Se verdadeiro, o campo será renderizado como somente leitura.
   * 
   * @type {boolean}
   * @default false
   */
  readOnly?: boolean;
}

/**
 * Componente de entrada de texto para formulários.
 * 
 * Este componente utiliza o `react-hook-form` para integração com o controle do formulário e exibe um campo de texto com as propriedades fornecidas.
 * Ele também exibe mensagens de erro associadas ao campo, se houver.
 * 
 * @param {TextInputProps<TFieldValues>} props Propriedades do componente.
 * @returns {JSX.Element} Componente que renderiza um campo de texto integrado ao formulário.
 */
const TextInput = <TFieldValues extends FieldValues>({
  control,
  name,
  label,
  placeholder,
  rules,
  readOnly = false,
}: TextInputProps<TFieldValues>): JSX.Element => {
  const {
    field,
    fieldState: { error },
  } = useController<TFieldValues>({
    control,
    name,
    rules,
    defaultValue: '' as PathValue<TFieldValues, Path<TFieldValues>>,
  });

  return (
    <FormItem>
      <FormLabel>{label}</FormLabel>
      <FormControl>
        <Input {...field} placeholder={placeholder} readOnly={readOnly} />
      </FormControl>
      {error && <FormMessage>{error.message}</FormMessage>}
    </FormItem>
  );
};

export default TextInput;
