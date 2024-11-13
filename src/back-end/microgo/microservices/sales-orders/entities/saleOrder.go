package entities

import (
	"database/sql"
	"micronsync/sales-orders/errorhandling"
	"time"
)

type SaleOrder struct {
	Id           		int       `json:"id"`
	CustomerCep  		string    `json:"cep"`
	Total        		float64   `json:"total"`
	SaleDate     		time.Time `json:"saleDate"`
	FirstDeliveryDate	time.Time `json:"firstDeliveryDate"`
	LastDeliveryDate 	time.Time `json:"lastDeliveryDate"`
}

func NewSaleOrder(id int, customerCep string, total float64, saleDate time.Time, firstDeliveryDate time.Time, lastDeliveryDate time.Time) *SaleOrder {
	return &SaleOrder{
		Id					: id,
		CustomerCep			: customerCep,
		Total				: total,
		SaleDate			: saleDate,
		FirstDeliveryDate	: firstDeliveryDate,
		LastDeliveryDate	: lastDeliveryDate,
	}
}

func (*SaleOrder) MapSQLRowToEntity(row *sql.Rows) (*SaleOrder, error) {
	saleOrder := &SaleOrder{}
	readError := row.Scan(&saleOrder.Id, &saleOrder.CustomerCep, &saleOrder.Total, &saleOrder.SaleDate, &saleOrder.FirstDeliveryDate, &saleOrder.LastDeliveryDate)
	if readError != nil {
		return saleOrder, errorhandling.WriteError("MapSQLRowToEntity[SaleOrder]: Failed to copy row values into struct.\n%v", readError)
	}

	return saleOrder, nil
}
