package entities

import (
	"database/sql"
	errorhandling "nsync/utils/error-handling"
)

// # SaleOrderProduct Entity
//
// The SaleOrderProduct type is defined as a struct that can store all the data from a sale order product register present the application's database (normalized table).
// It implements the Entity Interface methods and have its own constructor defined as follows:
//
// func NewSaleOrderProduct(id int, saleOrderId int, productId int, distributorId int, quantity int, unitPrice float64) *SaleOrderProduct
type SaleOrderProduct struct {
	Id            int     `json:"id"`
	SaleOrderId   int     `json:"saleOrderId"`
	ProductId     int     `json:"productId"`
	DistributorId int     `json:"distributorId"`
	Quantity      int     `json:"quantity"`
	UnitPrice     float64 `json:"unitPrice"`
}

// Returns a pointer to a new SaleOrderProduct type struct containing the data from the parameters.
func NewSaleOrderProduct(id int, saleOrderId int, productId int, distributorId int, quantity int, unitPrice float64) *SaleOrderProduct {
	return &SaleOrderProduct{
		Id:            id,
		SaleOrderId:   saleOrderId,
		ProductId:     productId,
		DistributorId: distributorId,
		Quantity:      quantity,
		UnitPrice:     unitPrice,
	}
}

// Returns the SaleOrderProduct entity equivalent table name from the application's database.
func (*SaleOrderProduct) GetTableName() string {
	return "sales_orders_products"
}

// Returns the SaleOrderProduct entity equivalent table columns from the application's database (id column excluded).
func (*SaleOrderProduct) GetTableColumns() []string {
	return []string{"sale_order_id", "product_id", "distributor_id", "quantity", "unit_price"}
}

// Maps the row from the application's database into a new SaleOrderProduct struct.
func (*SaleOrderProduct) MapSQLRowToEntity(row *sql.Rows) (*SaleOrderProduct, error) {
	sop := &SaleOrderProduct{}
	readError := row.Scan(&sop.Id, &sop.SaleOrderId, &sop.ProductId, &sop.DistributorId, &sop.Quantity, &sop.UnitPrice)
	if readError != nil {
		return sop, errorhandling.WriteError("MapSQLRowToEntity[SaleOrderProduct]: Failed to copy row values into struct.\n%v", readError)
	}
	return sop, nil
}
