package entities

import (
	"database/sql"
	errorhandling "nsync/utils/error-handling"
)

// # Product Entity
//
// The Product type is defined as a struct that can store all the data from a product register present in the application's database.
// It implements the Entity Interface methods and have its own constructor defined as follows:
//
// func NewProduct(id int, sku string, name string, price float64, description string, linkImage string) *Product
type Product struct {
	Id          int     `json:"id"`
	SKU         string  `json:"sku"`
	Name        string  `json:"name"`
	Price       float64 `json:"price"`
	Description string  `json:"description"`
	LinkImage   string  `json:"linkImage"`
}

// Returns a pointer to a new Product type struct containing the data from the parameters.
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

// Returns the Product entity equivalent table name from the application's database.
func (*Product) GetTableName() string {
	return "products"
}

// Returns the Product entity equivalent table columns from the application's database (id column excluded).
func (*Product) GetTableColumns() []string {
	return []string{"sku", "name", "price", "description", "link_image"}
}

// Maps the row from the application's database into a new Product struct.
func (*Product) MapSQLRowToEntity(row *sql.Rows) (*Product, error) {
	product := &Product{}
	readError := row.Scan(&product.Id, &product.SKU, &product.Name, &product.Price, &product.Description, &product.LinkImage)
	if readError != nil {
		return product, errorhandling.WriteError("MapSQLRowToEntity[Product]: Failed to copy row values into struct.\n%v", readError)
	}
	return product, nil
}
