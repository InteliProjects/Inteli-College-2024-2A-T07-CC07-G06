package entities

import (
	"database/sql"
	errorhandling "nsync/utils/error-handling"
	"time"
)

// # SaleOrder Entity
//
// The SaleOrder type is defined as a struct that can store all the data from a sale order register present in the application's database.
// It implements the Entity Interface methods and have its own constructor defined as follows:
//
// func NewSaleOrder(id int, customerCep string, total float64, saleDate time.Time, firstDeliveryDate time.Time, lastDeliveryDate time.Time) *SaleOrder
type SaleOrder struct {
	Id           		int       `json:"id"`
	CustomerCep  		string    `json:"cep"`
	Total        		float64   `json:"total"`
	SaleDate     		time.Time `json:"saleDate"`
	FirstDeliveryDate	time.Time `json:"firstDeliveryDate"`
	LastDeliveryDate 	time.Time `json:"lastDeliveryDate"`
}

// Returns a pointer to a new SaleOrder type struct containing the data from the parameters.
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

// Returns the SaleOrder entity equivalent table name from the application's database.
func (*SaleOrder) GetTableName() string {
	return "sales_orders"
}

// Returns the SaleOrder entity equivalent table columns from the application's database (id column excluded).
func (*SaleOrder) GetTableColumns() []string {
	return []string{"customer_cep", "total", "sale_date", "first_delivery_date", "last_delivery_date"}
}

// Maps the row from the application's database into a new SaleOrder struct.
func (*SaleOrder) MapSQLRowToEntity(row *sql.Rows) (Entity, error) {
	saleOrder := &SaleOrder{}
	readError := row.Scan(&saleOrder.Id, &saleOrder.CustomerCep, &saleOrder.Total, &saleOrder.SaleDate, &saleOrder.FirstDeliveryDate, &saleOrder.LastDeliveryDate)
	if readError != nil {
		return saleOrder, errorhandling.WriteError("MapSQLRowToEntity[SaleOrder]: Failed to copy row values into struct.\n%v", readError)
	}

	return saleOrder, nil
}
