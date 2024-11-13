package entities

import (
	"database/sql"
	errorhandling "micronsync/products-distributors/errors"
)

type ProductDistributor struct {
	Id              int `json:"id"`
	DistributorId   int `json:"distributorId"`
	ProductId       int `json:"productId"`
	AmountAvailable int `json:"amountAvailable"`
	AmountReserved  int `json:"amountReserved"`
}

func NewProductDistributor(distributorId int, productId int, amountAvailable int, amountReserved int) *ProductDistributor {
	return &ProductDistributor{
		DistributorId:   distributorId,
		ProductId:       productId,
		AmountAvailable: amountAvailable,
		AmountReserved:  amountReserved,
	}
}

func (*ProductDistributor) MapSQLRowToEntity(row *sql.Rows) (*ProductDistributor, error) {
	pd := &ProductDistributor{}
	readError := row.Scan(&pd.Id, &pd.DistributorId, &pd.ProductId, &pd.AmountAvailable, &pd.AmountReserved)
	if readError != nil {
		return nil, errorhandling.WriteError("MapSQLRowToEntity[ProductDistributor]: Failed to copy row values into struct.\n%v", readError)
	}
	return pd, nil
}
