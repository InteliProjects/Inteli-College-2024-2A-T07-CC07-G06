package payloads

import (
	errorhandling "nsync/utils/error-handling"
	"strconv"
)

// # Product Distributor Payload
//
// The Product Distributor Payload type is defined as a struct that can store all the parsed data from a JSON request referring to product distributors informations (normalized table).
// It implements the Entity Interface methods and have its own constructor defined as follows:
//
// func NewProductDistributorPayload(distributorId int, productId int, amountAvailable int, amountReserved int) *ProductDistributorPayload
type ProductDistributorPayload struct {
	DistributorId   int `json:"distributorId" 	validate:"required"`
	ProductId       int `json:"productId" 		validate:"required"`
	AmountAvailable int `json:"amountAvailable" validate:"required"`
	AmountReserved  int `json:"amountReserved"`
}

// Returns a pointer to a new Product Distributor Payload type struct containing the data from the parameters.
func NewProductDistributorPayload(distributorId int, productId int, amountAvailable int, amountReserved int) *ProductDistributorPayload {
	return &ProductDistributorPayload{
		DistributorId:   distributorId,
		ProductId:       productId,
		AmountAvailable: amountAvailable,
		AmountReserved:  amountReserved,
	}
}

// Returns a slice containing all the values present in the payload struct, in the same order as the product distributor entity fields appear on the application's database.
func (pd *ProductDistributorPayload) GetValues() []interface{} {
	return []interface{}{pd.DistributorId, pd.ProductId, pd.AmountAvailable, pd.AmountReserved}
}

// Maps a CSV file record (row) into a new ProductDistributorPayload struct.
// For more info about the fieldsIndexes parameter, check the description of the EntityPayload interface method MapCsvRecordToPayload.
func (*ProductDistributorPayload) MapCsvRecordToPayload(record []string, fieldsIndexes []int) (EntityPayload, error) {
	distributorId, parse1Error := strconv.Atoi(record[fieldsIndexes[0]])
	productId, parse2Error := strconv.Atoi(record[fieldsIndexes[1]])
	amountAvailable, parse3Error := strconv.Atoi(record[fieldsIndexes[2]])
	amountReserved, parse4Error := strconv.Atoi(record[fieldsIndexes[3]])

	errorHappened, err := errorhandling.AnyNotNil(parse1Error, parse2Error, parse3Error, parse4Error)

	if errorHappened {
		return nil, errorhandling.WriteError("MapCsvRecordToPayload[ProductDistributorPayload]: Failed to parse CSV record value.\n%v", err)
	}

	return NewProductDistributorPayload(distributorId, productId, amountAvailable, amountReserved), nil
}
