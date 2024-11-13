package payloads

import (
	"micronsync/csv-processor/errorhandling"

	"strconv"
)

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

func (p *ProductPayload) GetValues() []interface{} {
	return []interface{}{p.SKU, p.Name, p.Price, p.Description, p.LinkImage}
}

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
