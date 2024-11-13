package payloads

type ProductsPayload struct {
	ProductsIds []int `json:"productsIds"`
}

func NewProductsPayload(productsIds []int) *ProductsPayload {
	return &ProductsPayload{
		ProductsIds: productsIds,
	}
}