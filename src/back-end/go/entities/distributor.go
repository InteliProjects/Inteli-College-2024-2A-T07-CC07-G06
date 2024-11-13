package entities

import (
	"database/sql"
	errorhandling "nsync/utils/error-handling"
)

// # Distributor Entity
//
// The Distributor type is defined as a struct that can store all the data from a distributor register present the application's database.
// It implements the Entity Interface methods and have its own constructor defined as follows:
//
// func NewDistributor(id int, name string, cep string) *Distributor
type Distributor struct {
	Id   int    `json:"id"`
	Name string `json:"name"`
	Cep  string `json:"cep"`
}

// Returns a pointer to a new Distributor type struct containing the data from the parameters.
func NewDistributor(id int, name string, cep string) *Distributor {
	return &Distributor{
		Id:   id,
		Name: name,
		Cep:  cep,
	}
}

// Returns the Distributor entity equivalent table name from the application's database.
func (*Distributor) GetTableName() string {
	return "distributors"
}

// Returns the Distributor entity equivalent table columns from the application's database (id column excluded).
func (*Distributor) GetTableColumns() []string {
	return []string{"name", "cep"}
}

// Maps the row from the application's database into a new Distributor struct.
func (*Distributor) MapSQLRowToEntity(row *sql.Rows) (Entity, error) {
	distributor := &Distributor{}
	readError := row.Scan(&distributor.Id, &distributor.Name, &distributor.Cep)
	if readError != nil {
		return distributor, errorhandling.WriteError("MapSQLRowToEntity[Distributor]: Failed to copy row values into struct.\n%v", readError)
	}
	return distributor, nil
}
