package payloads

import (
	"micronsync/csv-processor/errorhandling"

	"strconv"
	"time"
)

type SaleOrderPayload struct {
	CustomerCep       string    `json:"cep"				validate:"required"`
	Total             float64   `json:"total"			validate:"required"`
	SaleDate          time.Time `json:"saleDate"`
	FirstDeliveryDate time.Time `json:"firstDeliveryDate""`
	LastDeliveryDate  time.Time `json:"lastDeliveryDate""`
}

func NewSaleOrderPayload(customerCep string, total float64, saleDate time.Time, firstDeliveryDate time.Time, lastDeliveryDate time.Time) *SaleOrderPayload {
	return &SaleOrderPayload{
		CustomerCep:       customerCep,
		Total:             total,
		SaleDate:          saleDate,
		FirstDeliveryDate: firstDeliveryDate,
		LastDeliveryDate:  lastDeliveryDate,
	}
}

func (so *SaleOrderPayload) GetValues() []interface{} {
	if so.SaleDate.IsZero() {
		so.SaleDate = time.Now()
	}
	return []interface{}{so.CustomerCep, so.Total, so.SaleDate, so.FirstDeliveryDate, so.LastDeliveryDate}
}

func (*SaleOrderPayload) MapCsvRecordToPayload(record []string, fieldsIndexes []int) (EntityPayload, error) {
	customerCep := record[fieldsIndexes[0]]
	total, parse1Error := strconv.ParseFloat(record[fieldsIndexes[1]], 64)
	saleDate, parse2Error := time.Parse("2006-01-02T15:04:05", record[fieldsIndexes[2]])
	firstDeliveryDate, parse3Error := time.Parse(time.DateOnly, record[fieldsIndexes[3]])
	lastDeliveryDate, parse4Error := time.Parse(time.DateOnly, record[fieldsIndexes[4]])

	if len(customerCep) == 0 {
		return nil, errorhandling.WriteError("MapCsvRecordToPayload[SaleOrderPayload]: Failed to copy CSV values into struct (EMPTY VALUES).")
	}

	errorHappened, err := errorhandling.AnyNotNil(parse1Error, parse2Error, parse3Error, parse4Error)
	if errorHappened {
		return nil, errorhandling.WriteError("MapCsvRecordToPayload[SaleOrderPayload]: Failed to parse CSV record value.\n%v", err)
	}

	return NewSaleOrderPayload(customerCep, total, saleDate, firstDeliveryDate, lastDeliveryDate), nil
}
