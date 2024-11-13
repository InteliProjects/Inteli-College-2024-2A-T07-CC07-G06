package database

import (
	"database/sql"
	"errors"
	"micronsync/distributors/entities"
	"micronsync/distributors/errorhandling"

	_ "github.com/lib/pq"
)

var DB *sql.DB

func OpenConnection(connString string) (*sql.DB, error) {
	db, err := sql.Open("postgres", connString)

	if err != nil {
		return nil, errorhandling.WriteError("OpenConnection: Failed to open connection to database.\n%v", err)
	}

	DB = db
	return db, nil
}

func GetAllDistributors() ([]*entities.Distributor, error) {
	rows, queryError := DB.Query("SELECT id, name, cep FROM distributors")
	if queryError != nil {
		return nil, errorhandling.WriteError("GetAllDistributors: Failed to execute query on table distributors.\n%v", queryError)
	}
	defer rows.Close()

	var d entities.Distributor
	var distributors []*entities.Distributor
	for rows.Next() {
		distributor, mapError := d.MapSQLRowToEntity(rows)
		if mapError != nil {
			return nil, mapError
		}
		distributors = append(distributors, distributor)
	}

	if rowsError := rows.Err(); rowsError != nil {
		return nil, errorhandling.WriteError("GetAllDistributors: Failed to get any result rows.\n%v", rowsError)
	}

	return distributors, nil
}

func GetDistributorById(id int) (*entities.Distributor, error) {
	row := DB.QueryRow("SELECT id, name, cep FROM distributors WHERE id = $1", id)

	distributor := &entities.Distributor{}
	if scanError := row.Scan(&distributor.Id, &distributor.Name, &distributor.Cep); scanError != nil {
		if errors.Is(scanError, sql.ErrNoRows) {
			return nil, nil
		}
		return nil, errorhandling.WriteError("GetDistributorById: Failed to scan row into distributor struct.\n%v", scanError)
	}

	return distributor, nil
}
