package main

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"math"
	"net/http"
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
	apiKey      = os.Getenv("API_KEY")
)

type Product struct {
	ProductID int `json:"productId"`
	Quantity  int `json:"quantity"`
}

type EventBody struct {
	Products []Product `json:"products"`
	CEP      string    `json:"cep"`
}

type GeoData struct {
	Latitude  float64 `json:"latitude"`
	Longitude float64 `json:"longitude"`
}

type GeoAPIResponse struct {
	Features []struct {
		Geometry struct {
			Coordinates []float64 `json:"coordinates"`
		} `json:"geometry"`
	} `json:"features"`
}

func getGeoData(cep string) (*GeoData, error) {
	url := fmt.Sprintf("https://api.geoapify.com/v1/geocode/search?text=%s&apiKey=%s", cep, apiKey)
	resp, err := http.Get(url)
	if err != nil {
		return nil, fmt.Errorf("Error fetching geo data: %v", err)
	}
	defer resp.Body.Close()

	var result GeoAPIResponse
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		return nil, err
	}

	if len(result.Features) > 0 {
		coords := result.Features[0].Geometry.Coordinates
		return &GeoData{
			Latitude:  coords[1],
			Longitude: coords[0],
		}, nil
	}
	return nil, fmt.Errorf("No geo data found for CEP")
}

func calculateDistance(lat1, lon1, lat2, lon2 float64) float64 {
	R := 6371.0
	lat1Rad := lat1 * math.Pi / 180
	lon1Rad := lon1 * math.Pi / 180
	lat2Rad := lat2 * math.Pi / 180
	lon2Rad := lon2 * math.Pi / 180

	deltaLat := lat2Rad - lat1Rad
	deltaLon := lon2Rad - lon1Rad

	a := math.Sin(deltaLat/2)*math.Sin(deltaLat/2) + math.Cos(lat1Rad)*math.Cos(lat2Rad)*math.Sin(deltaLon/2)*math.Sin(deltaLon/2)
	c := 2 * math.Atan2(math.Sqrt(a), math.Sqrt(1-a))

	return R * c
}

func getDBConnection() (*sql.DB, error) {
	connStr := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=require", rdsHost, rdsUser, rdsPassword, rdsDB, rdsPort)
	return sql.Open("postgres", connStr)
}

func handler(ctx context.Context, event map[string]interface{}) (map[string]interface{}, error) {
	// Extract body from event, assuming it's a string
	bodyStr, ok := event["body"].(string)
	if !ok {
		return map[string]interface{}{
			"statusCode": 400,
			"body":       `{"message": "Invalid request format"}`,
		}, nil
	}

	// Unmarshal the body into the EventBody struct
	var body EventBody
	if err := json.Unmarshal([]byte(bodyStr), &body); err != nil {
		return map[string]interface{}{
			"statusCode": 400,
			"body":       `{"message": "Invalid request body"}`,
		}, nil
	}

	// Validate products and CEP
	if len(body.Products) == 0 || body.CEP == "" {
		return map[string]interface{}{
			"statusCode": 400,
			"body":       `{"message": "Invalid products or CEP"}`,
		}, nil
	}

	// Fetch customer geo data
	customerGeoData, err := getGeoData(body.CEP)
	if err != nil {
		return map[string]interface{}{
			"statusCode": 400,
			"body":       `{"message": "Invalid CEP"}`,
		}, nil
	}

	db, err := getDBConnection()
	if err != nil {
		return map[string]interface{}{
			"statusCode": 500,
			"body":       fmt.Sprintf(`{"message": "DB connection error: %v"}`, err),
		}, nil
	}
	defer db.Close()

	latCustomer, lonCustomer := customerGeoData.Latitude, customerGeoData.Longitude

	for _, product := range body.Products {
		var nearestDistributorID int
		var nearestDistance float64 = math.MaxFloat64

		rows, err := db.Query(`
            SELECT d.id, d.cep, pd.quantity_available
            FROM distributors d
            JOIN products_distributors pd ON pd.distributor_id = d.id
            WHERE pd.product_id = $1 AND pd.quantity_available >= $2
        `, product.ProductID, product.Quantity)

		if err != nil {
			return map[string]interface{}{
				"statusCode": 500,
				"body":       fmt.Sprintf(`{"message": "DB query error: %v"}`, err),
			}, nil
		}
		defer rows.Close()

		for rows.Next() {
			var distributorID int
			var distributorCEP string
			var quantityAvailable int
			err := rows.Scan(&distributorID, &distributorCEP, &quantityAvailable)
			if err != nil {
				return map[string]interface{}{
					"statusCode": 500,
					"body":       fmt.Sprintf(`{"message": "DB row scan error: %v"}`, err),
				}, nil
			}

			geoDataDistributor, err := getGeoData(distributorCEP)
			if err == nil {
				distance := calculateDistance(latCustomer, lonCustomer, geoDataDistributor.Latitude, geoDataDistributor.Longitude)
				if distance < nearestDistance {
					nearestDistance = distance
					nearestDistributorID = distributorID
				}
			}
		}

		if nearestDistributorID == 0 {
			return map[string]interface{}{
				"statusCode": 404,
				"body":       fmt.Sprintf(`{"message": "No nearby distributor found for product %d"}`, product.ProductID),
			}, nil
		}

		_, err = db.Exec(`
            UPDATE products_distributors
            SET quantity_available = quantity_available - $1
            WHERE distributor_id = $2 AND product_id = $3
        `, product.Quantity, nearestDistributorID, product.ProductID)

		if err != nil {
			return map[string]interface{}{
				"statusCode": 500,
				"body":       fmt.Sprintf(`{"message": "Error updating product quantity: %v"}`, err),
			}, nil
		}
	}

	return map[string]interface{}{
		"statusCode": 200,
		"body":       `{"message": "Successfully processed purchase"}`,
	}, nil
}

func main() {
	lambda.Start(handler)
}
