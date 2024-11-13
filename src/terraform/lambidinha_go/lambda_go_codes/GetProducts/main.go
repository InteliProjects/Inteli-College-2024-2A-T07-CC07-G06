package main

import (
	"database/sql"
	"fmt"
	"os"

	"github.com/aws/aws-lambda-go/lambda"
	_ "github.com/lib/pq"
)

var (
	rdsHost     = os.Getenv("RDS_HOST")
	rdsUser     = os.Getenv("RDS_USER")
	rdsPassword = os.Getenv("RDS_PASSWORD")
	rdsDB       = os.Getenv("RDS_DB")
	rdsPort     = os.Getenv("RDS_PORT")
)

type Product struct {
	ID          int     `json:"id"`
	Name        string  `json:"name"`
	Price       float64 `json:"price"`
	Description string  `json:"description"`
	LinkImage   string  `json:"linkImage"`
	SKU         string  `json:"sku"`
}

func getDBConnection() (*sql.DB, error) {
	connStr := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=require",
		rdsHost, rdsUser, rdsPassword, rdsDB, rdsPort)

	db, err := sql.Open("postgres", connStr)
	if err != nil {
		return nil, err
	}

	return db, nil
}

func handler() ([]Product, error) {
	db, err := getDBConnection()
	if err != nil {
		return nil, fmt.Errorf("failed to connect to DB: %v", err)
	}
	defer db.Close()

	rows, err := db.Query("SELECT id, name, price, description, link_image, sku FROM products")
	if err != nil {
		return nil, fmt.Errorf("query failed: %v", err)
	}
	defer rows.Close()

	var products []Product
	for rows.Next() {
		var product Product
		if err := rows.Scan(&product.ID, &product.Name, &product.Price, &product.Description, &product.LinkImage, &product.SKU); err != nil {
			return nil, fmt.Errorf("failed to scan row: %v", err)
		}
		products = append(products, product)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("row error: %v", err)
	}

	return products, nil
}

func main() {
	lambda.Start(handler)
}
