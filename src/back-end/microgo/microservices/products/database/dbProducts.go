package database

import (
	"database/sql"
	"errors"
	"micronsync/products/entities"
	"micronsync/products/errorhandling"

	"github.com/lib/pq"

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

func GetAllProducts() ([]*entities.Product, error) {
	rows, queryError := DB.Query("SELECT id, sku, name, price, description, link_image FROM products")
	if queryError != nil {
		return nil, errorhandling.WriteError("GetAllProducts: Failed to execute query on table products.\n%v", queryError)
	}
	defer rows.Close()

	var p entities.Product
	var products []*entities.Product
	for rows.Next() {
		product, mapError := p.MapSQLRowToEntity(rows)
		if mapError != nil {
			return nil, mapError
		}
		products = append(products, product)
	}

	if rowsError := rows.Err(); rowsError != nil {
		return nil, errorhandling.WriteError("GetAllProducts: Failed to get any result rows.\n%v", rowsError)
	}

	return products, nil
}

func GetProductById(id int) (*entities.Product, error) {
	row := DB.QueryRow("SELECT id, sku, name, price, description, link_image FROM products WHERE id = $1", id)

	product := &entities.Product{}
	if scanError := row.Scan(&product.Id, &product.SKU, &product.Name, &product.Price, &product.Description, &product.LinkImage); scanError != nil {
		if errors.Is(scanError, sql.ErrNoRows) {
			return nil, nil
		}
		return nil, errorhandling.WriteError("GetProductById: Failed to scan row into product struct.\n%v", scanError)
	}

	return product, nil
}

func GetProductsByIds(ids []int) ([]*entities.Product, error) {
	sqlQuery := "SELECT id, sku, name, price, description, link_image FROM products WHERE id = ANY($1)"

	rows, queryError := DB.Query(sqlQuery, pq.Array(ids))
	if queryError != nil {
		return nil, errorhandling.WriteError("GetProductsByIds: Failed to execute query.\n%v", queryError)
	}
	defer rows.Close()

	var products []*entities.Product
	for rows.Next() {
		product := &entities.Product{}
		if scanError := rows.Scan(&product.Id, &product.SKU, &product.Name, &product.Price, &product.Description, &product.LinkImage); scanError != nil {
			return nil, errorhandling.WriteError("GetProductsByIds: Failed to scan row.\n%v", scanError)
		}
		products = append(products, product)
	}

	if rowsError := rows.Err(); rowsError != nil {
		return nil, errorhandling.WriteError("GetProductsByIds: Error occurred during row iteration.\n%v", rowsError)
	}

	return products, nil
}
