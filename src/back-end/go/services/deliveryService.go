// # Package services
//
// This package provides services which handle with complex logic processes that are shared by different components throughout the application.
package services

import (
	"fmt"
	"math"
	"nsync/entities"
	"nsync/payloads"
	dbUtils "nsync/utils/database"
	errorhandling "nsync/utils/error-handling"
	"nsync/utils/geolocalization"
	"strings"
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

// The delivery query result struct only exists to store the results of the SQL query sent to the database in the GetDeliveryDetails function.
type deliveryQueryResult struct {
	Distributors 			[]*entities.Distributor
	ProductsDistributors 	[]*entities.ProductDistributor
}

// Returns the delivery information according to a customer CEP, the specified product id and the quantity bought
func GetDeliveryDetails(payload *payloads.DeliveryPayload) (*deliveryDetails, error) {
	var d *entities.Distributor
	var pd *entities.ProductDistributor
	sql := fmt.Sprintf("SELECT pd.id AS pdId, %s, d.id AS dId, %s FROM products_distributors AS pd JOIN distributors AS d ON pd.distributor_id = d.id WHERE pd.product_id = $1 AND pd.quantity_available >= $2 LIMIT 10", strings.Join(pd.GetTableColumns(), ", "), strings.Join(d.GetTableColumns(), ", "))
	rows, dbError := dbUtils.CustomQuery(sql, payload.ProductId, payload.Quantity)
	if dbError != nil {
		return nil, dbError
	}
	defer rows.Close()

	var result deliveryQueryResult
	for rows.Next() {
		var newPd entities.ProductDistributor
		var newD entities.Distributor
		if mapError := rows.Scan(&newPd.Id, &newPd.DistributorId, &newPd.ProductId, &newPd.AmountAvailable, &newPd.AmountReserved, &newD.Id, &newD.Name, &newD.Cep); mapError != nil {
			return nil, errorhandling.WriteError("GetDeliveryDetails: Failed to parse SQL rows into entities.\n%v", mapError)
		}
		result.Distributors = append(result.Distributors, &newD)
		result.ProductsDistributors = append(result.ProductsDistributors, &newPd)
	}

	if rowsError := rows.Err(); rowsError != nil {
		return nil, errorhandling.WriteError("GetDeliveryDetails: Error reading rows.\n%v", rowsError)
	}

	customerCoords, coordsError := geolocalization.GetCoordinates(payload.Cep)
	if coordsError != nil {
		return nil, coordsError
	}

	shortestDistance := math.MaxFloat32
	for i, distributor := range result.Distributors {
		distance, geoError := customerCoords.CalculateDistance(distributor.Cep)
		if geoError != nil {
			return nil, geoError
		}

		if distance < shortestDistance {
			shortestDistance = distance
			d = distributor
			pd = result.ProductsDistributors[i]
		}
	}

	now := time.Now()
	return &deliveryDetails {
		FirstDay			: now.Add(time.Hour * time.Duration(24 * int(shortestDistance / 30))),
		LastDay				: now.Add(time.Hour * time.Duration(24 * int(shortestDistance / 20))),
		Distributor			: d,
		ProductDistributor	: pd,
	}, nil
}