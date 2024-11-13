package payloads

type ProductPayload struct {
	SKU         string  `json:"sku"			validate:"required"`
	Name        string  `json:"name"		validate:"required"`
	Price       float64 `json:"price"		validate:"required"`
	Description string  `json:"description"	validate:"required"`
	LinkImage   string  `json:"linkImage"	validate:"required"`
}

func NewProductPayload(sku string, name string, price float64, description string, linkImage string) *ProductPayload {
	return &ProductPayload{
		SKU:         sku,
		Name:        name,
		Price:       price,
		Description: description,
		LinkImage:   linkImage,
	}
}