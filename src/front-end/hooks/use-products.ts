import useSWR, { mutate } from "swr";
import baseApi from "./axios-config";
import { Post } from "@/types/post-mock";
import { Product } from "@/types/product";

const URL = "/products";

const fetcher = async (url: string) => {
  const response = await baseApi.get(url);
  return response.data;
};

export const useProducts = () => {
  const { data: products, error } = useSWR<Product[]>(URL, fetcher);

  return {
    products,
    isLoading: !error && !products,
    isError: error,
  };
};
