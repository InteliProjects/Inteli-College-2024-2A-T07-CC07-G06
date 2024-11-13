package payloads

import (
	errorhandling "nsync/utils/error-handling"
	"strconv"
	"time"
)

// # Sale Order Payload
//
// The Sale Order Payload type is defined as a struct that can store all the parsed data from a JSON request referring to sale order informations.
// It implements the Entity Interface methods and have its own constructor defined as follows:
//
// func NewSaleOrderPayload(customerCep string, total float64, saleDate time.Time, firstDeliveryDate time.Time, lastDeliveryDate time.Time) *SaleOrderPayload
type SaleOrderPayload struct {
	CustomerCep 		string 		`json:"cep"				validate:"required"`
	Total 				float64 	`json:"total"			validate:"required"`
	SaleDate 			time.Time 	`json:"saleDate"`
	FirstDeliveryDate 	time.Time 	`json:"firstDeliveryDate""`
	LastDeliveryDate 	time.Time 	`json:"lastDeliveryDate""`
}

// Returns a pointer to a new Sale Order Payload type struct containing the data from the parameters.
func NewSaleOrderPayload(customerCep string, total float64, saleDate time.Time, firstDeliveryDate time.Time, lastDeliveryDate time.Time) *SaleOrderPayload {
	return &SaleOrderPayload {
		CustomerCep 		: customerCep,
		Total 				: total,
		SaleDate			: saleDate,
		FirstDeliveryDate	: firstDeliveryDate,
		LastDeliveryDate	: lastDeliveryDate,
	}
}

// Returns a slice containing all the values present in the payload struct, in the same order as the sale order entity fields appear on the application's database.
func (so *SaleOrderPayload) GetValues() []interface{} {
	if so.SaleDate.IsZero() {
		so.SaleDate = time.Now()
	}
	return []interface{}{ so.CustomerCep, so.Total, so.SaleDate, so.FirstDeliveryDate, so.LastDeliveryDate } 
}

// Maps a CSV file record (row) into a new SaleOrderPayload struct.
// For more info about the fieldsIndexes parameter, check the description of the EntityPayload interface method MapCsvRecordToPayload.
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