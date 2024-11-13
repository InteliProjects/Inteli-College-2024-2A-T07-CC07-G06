package tests

import (
	"database/sql"
	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"micronsync/distributors/entities"
	"testing"
)

const (
	testDistributorID   = 1
	testDistributorName = "Distributor A"
	testDistributorCep  = "05510-021"
)

func TestNewDistributor(t *testing.T) {
	distributor := entities.NewDistributor(testDistributorID, testDistributorName, testDistributorCep)

	assert.Equal(t, testDistributorID, distributor.Id)
	assert.Equal(t, testDistributorName, distributor.Name)
	assert.Equal(t, testDistributorCep, distributor.Cep)
}

func setupSQLMock(t *testing.T) (*sql.DB, sqlmock.Sqlmock) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err, "Error creating sqlmock")
	return db, mock
}

func TestDistributorMapSQLRowToEntity(t *testing.T) {
	db, mock := setupSQLMock(t)
	defer db.Close()

	mockRows := sqlmock.NewRows([]string{"id", "name", "cep"}).
		AddRow(testDistributorID, testDistributorName, testDistributorCep)

	mock.ExpectQuery("SELECT (.+) FROM distributors").WillReturnRows(mockRows)

	rows, err := db.Query("SELECT id, name, cep FROM distributors")
	require.NoError(t, err)
	defer rows.Close()

	if rows.Next() {
		distributor := &entities.Distributor{}
		entity, err := distributor.MapSQLRowToEntity(rows)

		require.NoError(t, err)
		require.IsType(t, &entities.Distributor{}, entity)

		assert.Equal(t, testDistributorID, entity.Id)
		assert.Equal(t, testDistributorName, entity.Name)
		assert.Equal(t, testDistributorCep, entity.Cep)
	}
}
