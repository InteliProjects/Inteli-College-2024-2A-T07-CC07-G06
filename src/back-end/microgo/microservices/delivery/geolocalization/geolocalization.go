package geolocalization

import (
	"encoding/json"
	"fmt"
	"math"
	"micronsync/delivery/errorhandling"
	"net/http"
)

const EarthRadius float64 = 6371.0
const GeoApiKey string = "cd7c0c3596a34d75ab4de6d45147ac6f"

type GeoApiResponse struct {
	Features []Features `json:"features" validate:"required"`
}

type Features struct {
	Geometry Geometry `json:"geometry" validate:"required"`
}

type Geometry struct {
	Coordinates []float64 `json:"coordinates" validate:"required"`
}

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
	resp, httpError := http.Get(fmt.Sprintf("https://api.geoapify.com/v1/geocode/search?text=%s&apiKey=%s", cep, GeoApiKey))
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


func (c *Coordinates) CalculateDistance(cep string) (float64, error) {
	coordsResult := getCoordinates(cep)
	if coordsResult.Error != nil {
		return 0, coordsResult.Error
	}

	return haversine(c, coordsResult.Coords), nil
}

func haversine(coords1 *Coordinates, coords2 *Coordinates) float64 {
	toRadians := math.Pi / 180

	deltaLat := (coords2.Latitude - coords1.Latitude) * toRadians
	deltaLong := (coords2.Longitude - coords1.Longitude) * toRadians

	a := math.Sin(deltaLat/2)*math.Sin(deltaLat/2) + math.Cos(coords1.Latitude*toRadians)*math.Cos(coords2.Latitude*toRadians)*math.Sin(deltaLong/2)*math.Sin(deltaLong/2)

	c := 2 * math.Atan2(math.Sqrt(a), math.Sqrt(1-a))

	return EarthRadius * c
}
