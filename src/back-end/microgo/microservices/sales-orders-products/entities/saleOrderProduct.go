package entities

import (
	"database/sql"
	"micronsync/sales-orders-products/errorhandling"
)

type SaleOrderProduct struct {
	Id            int     `json:"id"`
	SaleOrderId   int     `json:"saleOrderId"`
	ProductId     int     `json:"productId"`
	DistributorId int     `json:"distributorId"`
	Quantity      int     `json:"quantity"`
	UnitPrice     float64 `json:"unitPrice"`
}

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

func (*SaleOrderProduct) MapSQLRowToEntity(row *sql.Rows) (*SaleOrderProduct, error) {
	sop := &SaleOrderProduct{}
	readError := row.Scan(&sop.Id, &sop.SaleOrderId, &sop.ProductId, &sop.DistributorId, &sop.Quantity, &sop.UnitPrice)
	if readError != nil {
		return sop, errorhandling.WriteError("MapSQLRowToEntity[SaleOrderProduct]: Failed to copy row values into struct.\n%v", readError)
	}
	return sop, nil
}
