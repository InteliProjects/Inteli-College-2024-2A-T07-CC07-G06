// # Package services
//
// This package provides services which handle with complex logic processes that are shared by different components throughout the application.
package services

import (
	"math"
	"nsync/entities"
	"nsync/payloads"
	dbUtils "nsync/utils/database"
	errorhandling "nsync/utils/error-handling"
	"nsync/utils/geolocalization"
	"time"
)

// The delivery details struct encapsulates every information that may be related to a delivery info request:
// the first and last days of estimated delivery time, the distribuutor responsible for the delivery and the storage (product distributor normalized table) that the product will be taken from.
type deliveryDetails struct {
	FirstDay 			time.Time
	LastDay				time.Time
	Distributor 		*entities.Distributor
	ProductDistributor 	*entities.ProductDistributor
}

// Returns the delivery information according to a customer CEP, the specified product id and the quantity bought
func GetDeliveryDetails(payload *payloads.DeliveryPayload) (*deliveryDetails, error) {
	queryChannel := make(chan *dbUtils.DeliveryQueryResult)
	go dbUtils.GetDeliveryDataRoutine(payload.ProductId, payload.Quantity, queryChannel)
	
	geoChannel := make(chan *geolocalization.GetCoordinatesResult)
	go geolocalization.GetCoordinatesRoutine(payload.Cep, geoChannel)

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
	return &deliveryDetails{
		FirstDay:           now.Add(time.Hour * time.Duration(24*int(shortestDistance/30))),
		LastDay:            now.Add(time.Hour * time.Duration(24*int(shortestDistance/20))),
		Distributor:        selectedDistributor,
		ProductDistributor: selectedProductDistributor,
	}, nil
}