// # Package geolocalization
//
// This package provides functions to calculate coordinates and coordinates-based distances from the "CEP" field of entities and payloads from Packages entities and payloads.
//
// It is mainly used on delivery logic.
package geolocalization

import (
	"encoding/json"
	"fmt"
	"math"
	"net/http"
	errorhandling "nsync/utils/error-handling"
)

const earthRadius float64 = 6371.0
const geoApiKey string = "62525684e3134e83862f8c04b0882e30"

// The GeoApiResponse struct should not be used outside the geolocalization package,
// though it is exported by said package because it is necessary to do so in order to Unmarshall the API response into the struct.
type GeoApiResponse struct {
	Features []Features `json:"features" validate:"required"`
}

// The Features struct is only a part of the GeoApiResponse and follows the same principle as that other struct: it should not be used outside the geolocalization package,
// though it is exported by said package because it is necessary to do so in order to Unmarshall the API response into the struct.
type Features struct {
	Geometry Geometry `json:"geometry" validate:"required"`
}

// The Geometry struct is only a part of the Features struct and follows the same principle as that other struct: it should not be used outside the geolocalization package,
// though it is exported by said package because it is necessary to do so in order to Unmarshall the API response into the struct.
type Geometry struct {
	Coordinates []float64 `json:"coordinates" validate:"required"`
}

// The Coordinates struct represents geographic coordinates.
type Coordinates struct {
	Latitude  float64
	Longitude float64
}

type GetCoordinatesResult struct {
	Coords *Coordinates
	Error 	error
}

func GetCoordinatesRoutine(cep string, coordsChannel chan *GetCoordinatesResult) {
	coordsChannel <- getCoordinates(cep)
}

func getCoordinates(cep string) *GetCoordinatesResult {
	resp, httpError := http.Get(fmt.Sprintf("https://api.geoapify.com/v1/geocode/search?text=%s&apiKey=%s", cep, geoApiKey))
	if httpError != nil {
		return &GetCoordinatesResult{nil, errorhandling.WriteError("getCoordinates: Failed to complete http request with CEP=%s.\n%v", cep, httpError)}
	}
	defer resp.Body.Close()

	var geoData GeoApiResponse
	if decodingError := json.NewDecoder(resp.Body).Decode(&geoData); decodingError != nil {
		return &GetCoordinatesResult{nil, errorhandling.WriteError("getCoordinates: Failed to decode http response into GeoApiResponse type struct.\n%v", decodingError)}
	}

	if len(geoData.Features) == 0 {
		return &GetCoordinatesResult{nil, errorhandling.WriteError("getCoordinates: No geolocalization data returned.")}
	}

	return &GetCoordinatesResult{&Coordinates{geoData.Features[0].Geometry.Coordinates[0], geoData.Features[0].Geometry.Coordinates[1]}, nil}
}

// This method calculates the distance from "c" to the specified CEP, in kilometers.
func (c *Coordinates) CalculateDistance(cep string) (float64, error) {
	coordsResult := getCoordinates(cep)
	if coordsResult.Error != nil {
		return 0, coordsResult.Error
	}

	return haversine(c, coordsResult.Coords), nil
}

// Formula to calculate the distance between two points in a sphere based on their geographic coordinates.
func haversine(coords1 *Coordinates, coords2 *Coordinates) float64 {
	toRadians := math.Pi / 180

	deltaLat := (coords2.Latitude - coords1.Latitude) * toRadians
	deltaLong := (coords2.Longitude - coords1.Longitude) * toRadians

	a := math.Sin(deltaLat/2)*math.Sin(deltaLat/2) + math.Cos(coords1.Latitude*toRadians)*math.Cos(coords2.Latitude*toRadians)*math.Sin(deltaLong/2)*math.Sin(deltaLong/2)

	c := 2 * math.Atan2(math.Sqrt(a), math.Sqrt(1-a))

	return earthRadius * c
}
