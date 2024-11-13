package tests

import (
	"database/sql"
	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"micronsync/products-distributors/entities"
	"testing"
)

const (
	testProductDistributorID = 1
	testDistributorId        = 1
	testProductId            = 1
	testAmountAvailable      = 100
	testAmountReserved       = 50
)

func TestNewProductDistributor(t *testing.T) {
	pd := entities.NewProductDistributor(testDistributorId, testProductId, testAmountAvailable, testAmountReserved)

	assert.Equal(t, testDistributorId, pd.DistributorId)
	assert.Equal(t, testProductId, pd.ProductId)
	assert.Equal(t, testAmountAvailable, pd.AmountAvailable)
	assert.Equal(t, testAmountReserved, pd.AmountReserved)
}

func setupSQLMock(t *testing.T) (*sql.DB, sqlmock.Sqlmock) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err, "Error creating sqlmock")
	return db, mock
}

func TestProductDistributorMapSQLRowToEntity(t *testing.T) {
	db, mock := setupSQLMock(t)
	defer db.Close()

	mockRows := sqlmock.NewRows([]string{"id", "distributor_id", "product_id", "amount_available", "amount_reserved"}).
		AddRow(testProductDistributorID, testDistributorId, testProductId, testAmountAvailable, testAmountReserved)

	mock.ExpectQuery("SELECT (.+) FROM product_distributors").WillReturnRows(mockRows)

	rows, err := db.Query("SELECT id, distributor_id, product_id, amount_available, amount_reserved FROM product_distributors")
	require.NoError(t, err)
	defer rows.Close()

	if rows.Next() {
		pd := &entities.ProductDistributor{}
		entity, err := pd.MapSQLRowToEntity(rows)

		require.NoError(t, err)
		require.IsType(t, &entities.ProductDistributor{}, entity)

		assert.Equal(t, testProductDistributorID, entity.Id)
		assert.Equal(t, testDistributorId, entity.DistributorId)
		assert.Equal(t, testProductId, entity.ProductId)
		assert.Equal(t, testAmountAvailable, entity.AmountAvailable)
		assert.Equal(t, testAmountReserved, entity.AmountReserved)
	}
}
