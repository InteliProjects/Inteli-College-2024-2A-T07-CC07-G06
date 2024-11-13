package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"math"
	"net/http"
	"os"
	"time"

	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	_ "github.com/lib/pq"
)

type Response struct {
	FirstDay string `json:"firstDay"`
	LastDay  string `json:"lastDay"`
}

type GeoData struct {
	Latitude  float64 `json:"latitude"`
	Longitude float64 `json:"longitude"`
}

var (
	rdsHost     = os.Getenv("RDS_HOST")
	rdsUser     = os.Getenv("RDS_USER")
	rdsPassword = os.Getenv("RDS_PASSWORD")
	rdsDB       = os.Getenv("RDS_DB")
	rdsPort     = os.Getenv("RDS_PORT")
	apiKey      = os.Getenv("GEOAPIFY_API_KEY")
)

func getDBConnection() (*sql.DB, error) {
	connStr := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=require",
		rdsHost, rdsUser, rdsPassword, rdsDB, rdsPort)

	return sql.Open("postgres", connStr)
}

func getGeoData(cep string) (*GeoData, error) {
	url := fmt.Sprintf("https://api.geoapify.com/v1/geocode/search?text=%s&apiKey=%s", cep, apiKey)

	resp, err := http.Get(url)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("failed to fetch geo data, status code: %d", resp.StatusCode)
	}

	var result map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		return nil, err
	}

	features := result["features"].([]interface{})
	if len(features) > 0 {
		geometry := features[0].(map[string]interface{})["geometry"].(map[string]interface{})
		coordinates := geometry["coordinates"].([]interface{})

		return &GeoData{
			Latitude:  coordinates[1].(float64),
			Longitude: coordinates[0].(float64),
		}, nil
	}

	return nil, nil
}

func calculateDistance(lat1, lon1, lat2, lon2 float64) float64 {
	R := 6371.0
	lat1Rad := toRadians(lat1)
	lon1Rad := toRadians(lon1)
	lat2Rad := toRadians(lat2)
	lon2Rad := toRadians(lon2)

	deltaLat := lat2Rad - lat1Rad
	deltaLon := lon2Rad - lon1Rad

	a := math.Sin(deltaLat/2)*math.Sin(deltaLat/2) +
		math.Cos(lat1Rad)*math.Cos(lat2Rad)*math.Sin(deltaLon/2)*math.Sin(deltaLon/2)

	c := 2 * math.Atan2(math.Sqrt(a), math.Sqrt(1-a))

	return R * c
}

func toRadians(deg float64) float64 {
	return deg * math.Pi / 180
}

func calculateDeliveryDates(distance float64) (string, string) {
	var daysToDeliver int

	switch {
	case distance < 1:
		daysToDeliver = 1
	case distance < 10:
		daysToDeliver = 2
	case distance < 50:
		daysToDeliver = 3
	default:
		daysToDeliver = 4
	}

	firstDay := time.Now().AddDate(0, 0, daysToDeliver).Format("2006-01-02")
	lastDay := time.Now().AddDate(0, 0, daysToDeliver*2).Format("2006-01-02")

	return firstDay, lastDay
}

func findNearestDistributor(productID int, quantity int, latCustomer, lonCustomer float64) (*GeoData, error) {
	db, err := getDBConnection()
	if err != nil {
		return nil, fmt.Errorf("error connecting to DB: %v", err)
	}
	defer db.Close()

	query := `
        SELECT d.cep 
        FROM distributors d 
        JOIN products_distributors pd ON pd.distributor_id = d.id 
        WHERE pd.product_id = $1 AND pd.quantity_available >= $2`

	rows, err := db.Query(query, productID, quantity)
	if err != nil {
		return nil, fmt.Errorf("error fetching distributors: %v", err)
	}
	defer rows.Close()

	var nearestDistributor *GeoData
	shortestDistance := math.MaxFloat64

	for rows.Next() {
		var distributorCEP string
		if err := rows.Scan(&distributorCEP); err != nil {
			return nil, err
		}

		geoData, err := getGeoData(distributorCEP)
		if err != nil {
			log.Printf("Error fetching geo data for distributor: %v", err)
			continue
		}

		if geoData != nil {
			distance := calculateDistance(latCustomer, lonCustomer, geoData.Latitude, geoData.Longitude)

			if distance < shortestDistance {
				shortestDistance = distance
				nearestDistributor = geoData
			}
		}
	}

	if nearestDistributor == nil {
		return nil, fmt.Errorf("no distributor found")
	}

	return nearestDistributor, nil
}

func handler(req events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {
	var requestBody map[string]interface{}
	if err := json.Unmarshal([]byte(req.Body), &requestBody); err != nil {
		return events.APIGatewayProxyResponse{
			StatusCode: 400,
			Body:       fmt.Sprintf("Invalid request body: %v", err),
		}, nil
	}

	cep, ok := requestBody["cep"].(string)
	if !ok {
		return events.APIGatewayProxyResponse{
			StatusCode: 400,
			Body:       "Invalid or missing 'cep'",
		}, nil
	}

	productIDFloat, ok := requestBody["productId"].(float64)
	if !ok {
		return events.APIGatewayProxyResponse{
			StatusCode: 400,
			Body:       "Invalid or missing 'productId'",
		}, nil
	}
	productID := int(productIDFloat)

	quantity := 0
	quantityFloat, ok := requestBody["quantity"].(float64)
	if !ok {
		quantity = 1
	} else {
		quantity = int(quantityFloat)
	}

	geoDataCustomer, err := getGeoData(cep)
	if err != nil || geoDataCustomer == nil {
		return events.APIGatewayProxyResponse{
			StatusCode: 400,
			Body:       "Invalid CEP",
		}, nil
	}

	nearestDistributor, err := findNearestDistributor(productID, quantity, geoDataCustomer.Latitude, geoDataCustomer.Longitude)
	if err != nil {
		return events.APIGatewayProxyResponse{
			StatusCode: 404,
			Body:       "No distributor found",
		}, nil
	}

	firstDay, lastDay := calculateDeliveryDates(calculateDistance(geoDataCustomer.Latitude, geoDataCustomer.Longitude, nearestDistributor.Latitude, nearestDistributor.Longitude))

	response := Response{
		FirstDay: firstDay,
		LastDay:  lastDay,
	}

	responseJSON, err := json.Marshal(response)
	if err != nil {
		return events.APIGatewayProxyResponse{
			StatusCode: 500,
			Body:       fmt.Sprintf("Error marshaling response: %v", err),
		}, nil
	}

	return events.APIGatewayProxyResponse{
		StatusCode: 200,
		Body:       string(responseJSON),
	}, nil
}

func main() {
	lambda.Start(handler)
}
