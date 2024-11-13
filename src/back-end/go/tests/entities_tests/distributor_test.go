package entities_tests

import (
	"testing"

	"nsync/entities"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/assert"
)

const (
	testDistributorName = "Distributor 1"
	testDistributorCep  = "05510-021"
)

func TestNewDistributor(t *testing.T) {
	distributor := entities.NewDistributor(1, testDistributorName, testDistributorCep)

	assert.Equal(t, 1, distributor.Id)
	assert.Equal(t, testDistributorName, distributor.Name)
	assert.Equal(t, testDistributorCep, distributor.Cep)
}

func TestDistributorGetTableColumns(t *testing.T) {
	distributor := &entities.Distributor{}
	columns := distributor.GetTableColumns()

	expectedColumns := []string{"name", "cep"}
	assert.Equal(t, expectedColumns, columns)
}

func TestDistributorMapSQLRowToEntity(t *testing.T) {
	db, mock, err := sqlmock.New()
	assert.NoError(t, err)
	defer db.Close()

	mockRows := sqlmock.NewRows([]string{"id", "name", "cep"}).AddRow(1, testDistributorName, testDistributorCep)

	mock.ExpectQuery("SELECT (.+) FROM distributors").WillReturnRows(mockRows)

	rows, err := db.Query("SELECT id, name, cep FROM distributors")
	assert.NoError(t, err)
	defer rows.Close()

	if rows.Next() {
		distributor := &entities.Distributor{}
		entity, err := distributor.MapSQLRowToEntity(rows)

		assert.NoError(t, err)
		assert.IsType(t, &entities.Distributor{}, entity)
		assert.Equal(t, 1, entity.(*entities.Distributor).Id)
		assert.Equal(t, testDistributorName, entity.(*entities.Distributor).Name)
		assert.Equal(t, testDistributorCep, entity.(*entities.Distributor).Cep)
	}
}
