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
import { Textarea } from '../ui/textarea';

/**
 * Propriedades para o componente `TextAreaInput`.
 * 
 * @template TFieldValues Tipo genérico para os valores dos campos do formulário.
 */
interface TextAreaInputProps<TFieldValues extends FieldValues> {
  /**
   * Controle do formulário, fornecido pelo `react-hook-form`.
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
}

/**
 * Componente de entrada de texto em formato de área de texto para formulários.
 * 
 * Este componente utiliza o `react-hook-form` para integração com o controle do formulário e exibe uma área de texto com as propriedades fornecidas.
 * Ele também exibe mensagens de erro associadas ao campo, se houver.
 * 
 * @param {TextAreaInputProps<TFieldValues>} props Propriedades do componente.
 * @returns {JSX.Element} Componente que renderiza uma área de texto integrada ao formulário.
 */
const TextAreaInput = <TFieldValues extends FieldValues>({
  control,
  name,
  label,
  placeholder,
  rules,
}: TextAreaInputProps<TFieldValues>): JSX.Element => {
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
        <Textarea {...field} placeholder={placeholder} />
      </FormControl>
      {error && <FormMessage>{error.message}</FormMessage>}
    </FormItem>
  );
};

export default TextAreaInput;
