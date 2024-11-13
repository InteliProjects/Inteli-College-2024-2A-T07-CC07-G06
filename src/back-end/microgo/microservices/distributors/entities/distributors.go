package entities

import (
	"database/sql"
	"micronsync/distributors/errorhandling"
)

type Distributor struct {
	Id   int    `json:"id"`
	Name string `json:"name"`
	Cep  string `json:"cep"`
}

func NewDistributor(id int, name string, cep string) *Distributor {
	return &Distributor{
		Id:   id,
		Name: name,
		Cep:  cep,
	}
}

func (*Distributor) MapSQLRowToEntity(row *sql.Rows) (*Distributor, error) {
	distributor := &Distributor{}
	readError := row.Scan(&distributor.Id, &distributor.Name, &distributor.Cep)
	if readError != nil {
		return nil, errorhandling.WriteError("MapSQLRowToEntity[Distributor]: Failed to copy row values into struct.\n%v", readError)
	}
	return distributor, nil
}
