package entities

import (
	"database/sql"
	errorhandling "nsync/utils/error-handling"
)

// # ProductDistributor Entity
//
// The ProductDistributor type is defined as a struct that can store all the data from a product distributor register present the application's database (normalized table).
// It implements the Entity Interface methods and have its own constructor defined as follows:
//
// func NewProductDistributor(id int, distributorId int, productId int, amountAvailable int, amountReserved int) *ProductDistributor
type ProductDistributor struct {
	Id              int `json:"id"`
	DistributorId   int `json:"distributorId"`
	ProductId       int `json:"productId"`
	AmountAvailable int `json:"amountAvailable"`
	AmountReserved  int `json:"amountReserved"`
}

// Returns a pointer to a new ProductDistributor type struct containing the data from the parameters.
func NewProductDistributor(id int, distributorId int, productId int, amountAvailable int, amountReserved int) *ProductDistributor {
	return &ProductDistributor{
		Id 				: id,
		DistributorId	: distributorId,
		ProductId		: productId,
		AmountAvailable	: amountAvailable,
		AmountReserved	: amountReserved,
	}
}

// Returns the ProductDistributor entity equivalent table name from the application's database.
func (*ProductDistributor) GetTableName() string {
	return "products_distributors"
}

// Returns the ProductDistributor entity equivalent table columns from the application's database (id column excluded).
func (*ProductDistributor) GetTableColumns() []string {
	return []string{"distributor_id", "product_id", "quantity_available", "quantity_reserved"}
}

// Maps the row from the application's database into a new ProductDistributor struct.
func (*ProductDistributor) MapSQLRowToEntity(row *sql.Rows) (Entity, error) {
	pd := &ProductDistributor{}
	readError := row.Scan(&pd.Id, &pd.DistributorId, &pd.ProductId, &pd.AmountAvailable, &pd.AmountReserved)
	if readError != nil {
		return pd, errorhandling.WriteError("MapSQLRowToEntity[ProductDistributor]: Failed to copy row values into struct.\n%v", readError)
	}
	return pd, nil
}
