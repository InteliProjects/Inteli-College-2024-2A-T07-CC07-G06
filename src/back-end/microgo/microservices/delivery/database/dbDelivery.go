package database

import (
	"database/sql"
	"micronsync/delivery/entities"
	"micronsync/delivery/errorhandling"

	_ "github.com/lib/pq"
)

type DeliveryQueryResult struct {
	Distributors         []*entities.Distributor
	ProductsDistributors []*entities.ProductDistributor
	Error                error
}

var DB *sql.DB

func OpenConnection(connString string) (*sql.DB, error) {
	db, err := sql.Open("postgres", connString)

	if err != nil {
		return nil, errorhandling.WriteError("OpenConnection: Failed to open connection to database.\n%v", err)
	}

	DB = db
	return db, nil
}

func GetDeliveryDataRoutine(productId int, quantity int, queryChannel chan *DeliveryQueryResult) {
	rows, queryError := DB.Query("SELECT pd.id AS pdId, pd.distributor_id, pd.product_id, pd.quantity_available, pd.quantity_reserved, d.id AS dId, d.name, d.cep FROM products_distributors AS pd JOIN distributors AS d ON pd.distributor_id = d.id WHERE pd.product_id = $1 AND pd.quantity_available >= $2 LIMIT 10",
		productId, quantity)
	if queryError != nil {
		queryChannel <- &DeliveryQueryResult{nil, nil, errorhandling.WriteError("GetDeliveryDataRoutine: Failed to execute SQL query.\n%v", queryError)}
		return
	}
	defer rows.Close()

	var result DeliveryQueryResult
	for rows.Next() {
		var newD entities.Distributor
		var newPd entities.ProductDistributor
		if mapError := rows.Scan(&newPd.Id, &newPd.DistributorId, &newPd.ProductId, &newPd.AmountAvailable, &newPd.AmountReserved, &newD.Id, &newD.Name, &newD.Cep); mapError != nil {
			queryChannel <- &DeliveryQueryResult{nil, nil, errorhandling.WriteError("GetDeliveryDataRoutine: Failed to parse SQL rows into entities.\n%v", mapError)}
			return
		}

		result.Distributors = append(result.Distributors, &newD)
		result.ProductsDistributors = append(result.ProductsDistributors, &newPd)
	}

	if rowsError := rows.Err(); rowsError != nil {
		queryChannel <- &DeliveryQueryResult{nil, nil, errorhandling.WriteError("GetDeliveryDataRoutine: Error reading rows.\n%v", rowsError)}
		return
	}

	queryChannel <- &result
}
