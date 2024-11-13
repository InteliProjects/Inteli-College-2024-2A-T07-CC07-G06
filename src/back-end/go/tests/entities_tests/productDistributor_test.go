package entities_tests

import (
	"testing"

	"nsync/entities"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/assert"
)

const (
	tetestPDID          = 1
	testPDDistributorID = 101
	testPDProductID     = 202
	testAmountAvailable = 50
	testAmountReserved  = 10
)

func TestNewProductDistributor(t *testing.T) {
	pd := entities.NewProductDistributor(testPDDistributorID, testPDProductID, testAmountAvailable, testAmountReserved)

	assert.Equal(t, testPDDistributorID, pd.DistributorId)
	assert.Equal(t, testPDProductID, pd.ProductId)
	assert.Equal(t, testAmountAvailable, pd.AmountAvailable)
	assert.Equal(t, testAmountReserved, pd.AmountReserved)
}

func TestProductDistributorGetTableName(t *testing.T) {
	pd := &entities.ProductDistributor{}
	tableName := pd.GetTableName()

	assert.Equal(t, "products_distributors", tableName)
}

func TestProductDistributorGetTableColumns(t *testing.T) {
	pd := &entities.ProductDistributor{}
	columns := pd.GetTableColumns()

	expectedColumns := []string{"distributor_id", "product_id", "quantity_available", "quantity_reserved"}
	assert.Equal(t, expectedColumns, columns)
}

func TestProductDistributorMapSQLRowToEntity(t *testing.T) {
	db, mock, err := sqlmock.New()
	assert.NoError(t, err)
	defer db.Close()

	mockRows := sqlmock.NewRows([]string{"id", "distributor_id", "product_id", "quantity_available", "quantity_reserved"}).
		AddRow(tetestPDID, testPDDistributorID, testPDProductID, testAmountAvailable, testAmountReserved)

	mock.ExpectQuery("SELECT (.+) FROM products_distributors").
		WillReturnRows(mockRows)

	rows, err := db.Query("SELECT id, distributor_id, product_id, quantity_available, quantity_reserved FROM products_distributors")
	assert.NoError(t, err)
	defer rows.Close()

	if rows.Next() {
		pd := &entities.ProductDistributor{}
		entity, err := pd.MapSQLRowToEntity(rows)

		assert.NoError(t, err)
		assert.IsType(t, &entities.ProductDistributor{}, entity)
		assert.Equal(t, tetestPDID, entity.(*entities.ProductDistributor).Id)
		assert.Equal(t, testPDDistributorID, entity.(*entities.ProductDistributor).DistributorId)
		assert.Equal(t, testPDProductID, entity.(*entities.ProductDistributor).ProductId)
		assert.Equal(t, testAmountAvailable, entity.(*entities.ProductDistributor).AmountAvailable)
		assert.Equal(t, testAmountReserved, entity.(*entities.ProductDistributor).AmountReserved)
	}
}
