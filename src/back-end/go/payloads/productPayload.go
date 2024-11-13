package payloads

import (
	errorhandling "nsync/utils/error-handling"
	"strconv"
)

// # Product Payload
//
// The Product Payload type is defined as a struct that can store all the parsed data from a JSON request referring to products informations.
// It implements the Entity Interface methods and have its own constructor defined as follows:
//
// func NewProductPayload(sku string, name string, price float64, description string, linkImage string) *ProductPayload
type ProductPayload struct {
	SKU         string  `json:"sku"			validate:"required"`
	Name        string  `json:"name"		validate:"required"`
	Price       float64 `json:"price"		validate:"required"`
	Description string  `json:"description"	validate:"required"`
	LinkImage   string  `json:"linkImage"	validate:"required"`
}

// Returns a pointer to a new Product Payload type struct containing the data from the parameters.
func NewProductPayload(sku string, name string, price float64, description string, linkImage string) *ProductPayload {
	return &ProductPayload{
		SKU:         sku,
		Name:        name,
		Price:       price,
		Description: description,
		LinkImage:   linkImage,
	}
}

// Returns a slice containing all the values present in the payload struct, in the same order as the product entity fields appear on the application's database.
func (p *ProductPayload) GetValues() []interface{} {
	return []interface{}{p.SKU, p.Name, p.Price, p.Description, p.LinkImage}
}

// Maps a CSV file record (row) into a new ProductPayload struct.
// For more info about the fieldsIndexes parameter, check the description of the EntityPayload interface method MapCsvRecordToPayload.
func (*ProductPayload) MapCsvRecordToPayload(record []string, fieldsIndexes []int) (EntityPayload, error) {
	sku := record[fieldsIndexes[0]]
	name := record[fieldsIndexes[1]]
	price, parseError := strconv.ParseFloat(record[fieldsIndexes[2]], 64)
	description := record[fieldsIndexes[3]]
	linkImage := record[fieldsIndexes[4]]

	if len(sku) == 0 || len(name) == 0 || len(description) == 0 || len(linkImage) == 0 {
		return nil, errorhandling.WriteError("MapCsvRecordToPayload[ProductPayload]: Failed to copy CSV values into struct (EMPTY VALUES).")
	}
	if parseError != nil {
		return nil, errorhandling.WriteError("MapCsvRecordToPayload[ProductPayload]: Failed to parse CSV record value.\n%v", parseError)
	}

	return NewProductPayload(sku, name, price, description, linkImage), nil
}