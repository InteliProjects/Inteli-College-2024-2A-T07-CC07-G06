package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"os"
	"strings"

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
	var requestBody map[string]interface{}
	err := json.Unmarshal([]byte(req.Body), &requestBody)
	if err != nil {
		return events.APIGatewayProxyResponse{
			StatusCode: 400,
			Body:       fmt.Sprintf("Invalid JSON format: %v", err),
		}, nil
	}

	productsIDs, ok := requestBody["productsIds"].([]interface{})
	if !ok || len(productsIDs) == 0 {
		return events.APIGatewayProxyResponse{
			StatusCode: 400,
			Body:       "productsIds is required and must be a list of integers",
		}, nil
	}

	var ids []int
	for _, id := range productsIDs {
		intID, ok := id.(float64)
		if !ok {
			return events.APIGatewayProxyResponse{
				StatusCode: 400,
				Body:       "All product IDs must be integers",
			}, nil
		}
		ids = append(ids, int(intID))
	}

	idsPlaceholders := strings.Repeat(", $", len(ids))[2:]
	query := fmt.Sprintf("SELECT id, name, price, description, link_image, sku FROM products WHERE id IN (%s)", idsPlaceholders)

	db, err := getDBConnection()
	if err != nil {
		return events.APIGatewayProxyResponse{
			StatusCode: 500,
			Body:       fmt.Sprintf("Failed to connect to DB: %v", err),
		}, nil
	}
	defer db.Close()

	rows, err := db.Query(query, convertToInterfaceSlice(ids)...)
	if err != nil {
		return events.APIGatewayProxyResponse{
			StatusCode: 500,
			Body:       fmt.Sprintf("Failed to execute query: %v", err),
		}, nil
	}
	defer rows.Close()

	var products []Product
	for rows.Next() {
		var product Product
		if err := rows.Scan(&product.ID, &product.Name, &product.Price, &product.Description, &product.LinkImage, &product.SKU); err != nil {
			return events.APIGatewayProxyResponse{
				StatusCode: 500,
				Body:       fmt.Sprintf("Failed to scan row: %v", err),
			}, nil
		}
		products = append(products, product)
	}

	if len(products) > 0 {
		productsJSON, err := json.Marshal(products)
		if err != nil {
			return events.APIGatewayProxyResponse{
				StatusCode: 500,
				Body:       fmt.Sprintf("Failed to marshal products: %v", err),
			}, nil
		}
		return events.APIGatewayProxyResponse{
			StatusCode: 200,
			Body:       string(productsJSON),
		}, nil
	} else {
		return events.APIGatewayProxyResponse{
			StatusCode: 404,
			Body:       "No products found",
		}, nil
	}
}

func convertToInterfaceSlice(ids []int) []interface{} {
	result := make([]interface{}, len(ids))
	for i, v := range ids {
		result[i] = v
	}
	return result
}

func main() {
	lambda.Start(handler)
}
