package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"os"

	"github.com/aws/aws-lambda-go/events"
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

func handler(req events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {
	productID := req.PathParameters["id"]

	db, err := getDBConnection()
	if err != nil {
		return events.APIGatewayProxyResponse{
			StatusCode: 500,
			Body:       fmt.Sprintf("Failed to connect to DB: %v", err),
		}, nil
	}
	defer db.Close()

	query := "SELECT id, name, price, description, link_image, sku FROM products WHERE id = $1"
	row := db.QueryRow(query, productID)

	var product Product
	err = row.Scan(&product.ID, &product.Name, &product.Price, &product.Description, &product.LinkImage, &product.SKU)
	if err == sql.ErrNoRows {
		return events.APIGatewayProxyResponse{
			StatusCode: 404,
			Body:       "Product not found",
		}, nil
	} else if err != nil {
		return events.APIGatewayProxyResponse{
			StatusCode: 500,
			Body:       fmt.Sprintf("Failed to retrieve product: %v", err),
		}, nil
	}

	productJSON, err := json.Marshal(product)
	if err != nil {
		return events.APIGatewayProxyResponse{
			StatusCode: 500,
			Body:       fmt.Sprintf("Failed to marshal product: %v", err),
		}, nil
	}

	return events.APIGatewayProxyResponse{
		StatusCode: 200,
		Body:       string(productJSON),
	}, nil
}

func main() {
	lambda.Start(handler)
}
