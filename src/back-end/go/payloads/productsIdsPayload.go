package payloads

// # Product Ids Payload
//
// The Product Ids Payload type is defined as a struct that can store all the parsed data from a JSON request referring to products ids.
// It has its own constructor defined as follows:
//
// func NewProductsIdsPayload(ids []int) *ProductsIdsPayload
type ProductsIdsPayload struct {
	Ids []int `json:"productsIds" validate:"required"`
}

// Returns a pointer to a new Product Ids Payload type struct containing the data from the parameters.
func NewProductsIdsPayload(ids []int) *ProductsIdsPayload {
	return &ProductsIdsPayload {
		Ids : ids,
	}
}