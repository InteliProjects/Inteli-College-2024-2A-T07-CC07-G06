package payloads

import (
	"micronsync/csv-processor/errorhandling"
	"strconv"
)

type ProductDistributorPayload struct {
	DistributorId   int `json:"distributorId" 	validate:"required"`
	ProductId       int `json:"productId" 		validate:"required"`
	AmountAvailable int `json:"amountAvailable" validate:"required"`
	AmountReserved  int `json:"amountReserved"`
}

func NewProductDistributorPayload(distributorId int, productId int, amountAvailable int, amountReserved int) *ProductDistributorPayload {
	return &ProductDistributorPayload{
		DistributorId:   distributorId,
		ProductId:       productId,
		AmountAvailable: amountAvailable,
		AmountReserved:  amountReserved,
	}
}

func (pd *ProductDistributorPayload) GetValues() []interface{} {
	return []interface{}{pd.DistributorId, pd.ProductId, pd.AmountAvailable, pd.AmountReserved}
}

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
