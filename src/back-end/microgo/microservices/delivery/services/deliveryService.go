package services

import (
	"math"
	"micronsync/delivery/database"
	"micronsync/delivery/entities"
	"micronsync/delivery/errorhandling"
	"micronsync/delivery/geolocalization"
	"time"
)

type DeliveryDetails struct {
	FirstDay           time.Time						`json:"firstDay"`
	LastDay            time.Time						`json:"lastDay"`
	Distributor        *entities.Distributor			`json:"distributor"`
	ProductDistributor *entities.ProductDistributor		`json:"productDistributor"`
}

func GetDeliveryDetails(productId int, quantity int, customerCep string) (*DeliveryDetails, error) {
	queryChannel := make(chan *database.DeliveryQueryResult)
	go database.GetDeliveryDataRoutine(productId, quantity, queryChannel)
	
	geoChannel := make(chan *geolocalization.GetCoordinatesResult)
	go geolocalization.GetCoordinatesRoutine(customerCep, geoChannel)

	dbResult := <-queryChannel
	coordsResult := <-geoChannel

	errorHappened, err := errorhandling.AnyNotNil(dbResult.Error, coordsResult.Error)
	if errorHappened {
		return nil, err
	}

	shortestDistance := math.MaxFloat32
	var selectedDistributor *entities.Distributor
	var selectedProductDistributor *entities.ProductDistributor

	for i, distributor := range dbResult.Distributors {
		distance, geoError := coordsResult.Coords.CalculateDistance(distributor.Cep)
		if geoError != nil {
			return nil, geoError
		}

		if distance < shortestDistance {
			shortestDistance = distance
			selectedDistributor = distributor
			selectedProductDistributor = dbResult.ProductsDistributors[i]
		}
	}

	now := time.Now()
	return &DeliveryDetails{
		FirstDay:           now.Add(time.Hour * time.Duration(24*int(shortestDistance/30))),
		LastDay:            now.Add(time.Hour * time.Duration(24*int(shortestDistance/20))),
		Distributor:        selectedDistributor,
		ProductDistributor: selectedProductDistributor,
	}, nil
}
