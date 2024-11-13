package database

import (
	"database/sql"
	"errors"
	"micronsync/products-distributors/entities"
	errorhandling "micronsync/products-distributors/errors"
	"micronsync/products-distributors/payloads"

	_ "github.com/lib/pq"
)

var DB *sql.DB

func OpenConnection(connString string) (*sql.DB, error) {
	db, err := sql.Open("postgres", connString)

	if err != nil {
		return nil, errorhandling.WriteError("OpenConnection: Failed to open connection to database.\n%v", err)
	}

	DB = db
	return db, nil
}

func GetAllProductsDistributors() ([]*entities.ProductDistributor, error) {
	rows, queryError := DB.Query("SELECT id, distributor_id, product_id, quantity_available, quantity_reserved FROM products_distributors")
	if queryError != nil {
		return nil, errorhandling.WriteError("GetAllProductsDistributors: Failed to execute query on table products_distributors.\n%v", queryError)
	}
	defer rows.Close()

	var pd entities.ProductDistributor
	var productsDistributors []*entities.ProductDistributor
	for rows.Next() {
		productDistributor, mapError := pd.MapSQLRowToEntity(rows)
		if mapError != nil {
			return nil, mapError
		}
		productsDistributors = append(productsDistributors, productDistributor)
	}

	if rowsError := rows.Err(); rowsError != nil {
		return nil, errorhandling.WriteError("GetAllProductsDistributors: Failed to get any result rows.\n%v", rowsError)
	}

	return productsDistributors, nil
}

func GetProductDistributorById(id int) (*entities.ProductDistributor, error) {
	row := DB.QueryRow("SELECT id, distributor_id, product_id, quantity_available, quantity_reserved FROM products_distributors WHERE id = $1", id)

	productDistributor := &entities.ProductDistributor{}
	if scanError := row.Scan(&productDistributor.Id, &productDistributor.DistributorId, &productDistributor.ProductId, &productDistributor.AmountAvailable, &productDistributor.AmountReserved); scanError != nil {
		if errors.Is(scanError, sql.ErrNoRows) {
			return nil, nil
		}
		return nil, errorhandling.WriteError("GetProductDistributorById: Failed to scan row into productDistributor struct.\n%v", scanError)
	}

	return productDistributor, nil
}

func UpdateProductDistributor(id int, payload payloads.ProductDistributorPayload) error {
	_, queryError := DB.Query("UPDATE products_distributors SET distributor_id = $1, product_id = $2, quantity_available = $3, quantity_reserved = $4 WHERE id = $5",
								payload.DistributorId, payload.ProductId, payload.AmountAvailable, payload.AmountReserved, id)
	if queryError != nil {
		return errorhandling.WriteError("UpdateProductDistributor: Failed to execute query on table products_distributors.\n%v", queryError)
	}

	return nil
}