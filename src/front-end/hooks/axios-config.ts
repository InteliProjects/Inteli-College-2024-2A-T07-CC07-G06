import axios from "axios";

const baseApi = axios.create({
  // baseURL: process.env.NEXT_PUBLIC_API,
  baseURL: "http://44.198.186.79:30000/"
});
export default baseApi;