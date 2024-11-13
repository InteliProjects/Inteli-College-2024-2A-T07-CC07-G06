package responses

type ProductsResponse struct {
	Id          int     `json:"id"`
	SKU         string  `json:"sku"`
	Name        string  `json:"name"`
	Price       float64 `json:"price"`
	Description string  `json:"description"`
	LinkImage   string  `json:"linkImage"`
}