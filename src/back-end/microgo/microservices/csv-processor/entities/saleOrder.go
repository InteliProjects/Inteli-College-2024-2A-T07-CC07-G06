package entities

import (
	"time"
)

type SaleOrder struct {
	Id                int       `json:"id"`
	CustomerCep       string    `json:"cep"`
	Total             float64   `json:"total"`
	SaleDate          time.Time `json:"saleDate"`
	FirstDeliveryDate time.Time `json:"firstDeliveryDate"`
	LastDeliveryDate  time.Time `json:"lastDeliveryDate"`
}

func NewSaleOrder(id int, customerCep string, total float64, saleDate time.Time, firstDeliveryDate time.Time, lastDeliveryDate time.Time) *SaleOrder {
	return &SaleOrder{
		Id:                id,
		CustomerCep:       customerCep,
		Total:             total,
		SaleDate:          saleDate,
		FirstDeliveryDate: firstDeliveryDate,
		LastDeliveryDate:  lastDeliveryDate,
	}
}

func (*SaleOrder) GetTableName() string {
	return "sales_orders"
}

func (*SaleOrder) GetTableColumns() []string {
	return []string{"customer_cep", "total", "sale_date", "first_delivery_date", "last_delivery_date"}
}