package payloads

import "time"

type SaleOrderPayload struct {
	CustomerCep       string    `json:"cep"				validate:"required"`
	Total             float64   `json:"total"			validate:"required"`
	SaleDate          time.Time  `json:"saleDate"`
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