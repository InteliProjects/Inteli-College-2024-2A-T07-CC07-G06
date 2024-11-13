package entities

import (
	"database/sql"
	"micronsync/products/errorhandling"
)

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

func (*Product) MapSQLRowToEntity(row *sql.Rows) (*Product, error) {
	product := &Product{}
	readError := row.Scan(&product.Id, &product.SKU, &product.Name, &product.Price, &product.Description, &product.LinkImage)
	if readError != nil {
		return nil, errorhandling.WriteError("MapSQLRowToEntity[Product]: Failed to copy row values into struct.\n%v", readError)
	}
	return product, nil
}
