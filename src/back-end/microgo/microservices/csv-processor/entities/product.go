package entities

type Product struct {
	Id          int     `json:"id"`
	SKU         string  `json:"sku"`
	Name        string  `json:"name"`
	Price       float64 `json:"price"`
	Description string  `json:"description"`
	LinkImage   string  `json:"linkImage"`
}

func NewProduct(id int, sku string, name string, price float64, description string, linkImage string) *Product {
	return &Product{
		Id:          id,
		SKU:         sku,
		Name:        name,
		Price:       price,
		Description: description,
		LinkImage:   linkImage,
	}
}

func (*Product) GetTableName() string {
	return "products"
}

func (*Product) GetTableColumns() []string {
	return []string{"sku", "name", "price", "description", "link_image"}
}