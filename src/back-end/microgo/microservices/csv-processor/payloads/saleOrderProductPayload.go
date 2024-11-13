package payloads

import (
	"micronsync/csv-processor/errorhandling"

	"strconv"
)

type SaleOrderProductPayload struct {
	SaleOrderId   int     `json:"saleOrderId" 	validate:"required"`
	ProductId     int     `json:"productId"		validate:"required"`
	DistributorId int     `json:"distributorId"	validate:"required"`
	Quantity      int     `json:"quantity"		validate:"required"`
	UnitPrice     float64 `json:"unitPrice"		validate:"required"`
}

func NewSaleOrderProductPayload(saleOrderId int, productId int, distributorId int, quantity int, unitPrice float64) *SaleOrderProductPayload {
	return &SaleOrderProductPayload{
		SaleOrderId:   saleOrderId,
		ProductId:     productId,
		DistributorId: distributorId,
		Quantity:      quantity,
		UnitPrice:     unitPrice,
	}
}

func (sop *SaleOrderProductPayload) GetValues() []interface{} {
	return []interface{}{sop.SaleOrderId, sop.ProductId, sop.DistributorId, sop.Quantity, sop.UnitPrice}
}

func (*SaleOrderProductPayload) MapCsvRecordToPayload(record []string, fieldsIndexes []int) (EntityPayload, error) {
	saleOrderId, parse1Error := strconv.Atoi(record[fieldsIndexes[0]])
	productId, parse2Error := strconv.Atoi(record[fieldsIndexes[1]])
	distributorId, parse3Error := strconv.Atoi(record[fieldsIndexes[2]])
	quantity, parse4Error := strconv.Atoi(record[fieldsIndexes[3]])
	unitPrice, parse5Error := strconv.ParseFloat(record[fieldsIndexes[4]], 64)

	errorHappened, err := errorhandling.AnyNotNil(parse1Error, parse2Error, parse3Error, parse4Error, parse5Error)
	if errorHappened {
		return nil, errorhandling.WriteError("MapCsvRecordToPayload[SaleOrderProductPayload]: Failed to parse CSV record value.\n%v", err)
	}

	return NewSaleOrderProductPayload(saleOrderId, productId, distributorId, quantity, unitPrice), nil
}
