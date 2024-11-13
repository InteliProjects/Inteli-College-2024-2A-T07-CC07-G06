package tests

import (
	"database/sql"
	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"micronsync/sales-orders-products/entities"
	"testing"
)

const (
	testSaleOrderProductID = 1
	testSaleOrderId        = 1
	testProductId          = 1
	testDistributorId      = 1
	testQuantity           = 10
	testUnitPrice          = 1299.99
)

func TestNewSaleOrderProduct(t *testing.T) {
	sop := entities.NewSaleOrderProduct(testSaleOrderProductID, testSaleOrderId, testProductId, testDistributorId, testQuantity, testUnitPrice)

	assert.Equal(t, testSaleOrderProductID, sop.Id)
	assert.Equal(t, testSaleOrderId, sop.SaleOrderId)
	assert.Equal(t, testProductId, sop.ProductId)
	assert.Equal(t, testDistributorId, sop.DistributorId)
	assert.Equal(t, testQuantity, sop.Quantity)
	assert.Equal(t, testUnitPrice, sop.UnitPrice)
}

func setupSQLMock(t *testing.T) (*sql.DB, sqlmock.Sqlmock) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err, "Error creating sqlmock")
	return db, mock
}

func TestSaleOrderProductMapSQLRowToEntity(t *testing.T) {
	db, mock := setupSQLMock(t)
	defer db.Close()

	mockRows := sqlmock.NewRows([]string{"id", "sale_order_id", "product_id", "distributor_id", "quantity", "unit_price"}).
		AddRow(testSaleOrderProductID, testSaleOrderId, testProductId, testDistributorId, testQuantity, testUnitPrice)

	mock.ExpectQuery("SELECT (.+) FROM sale_order_products").WillReturnRows(mockRows)

	rows, err := db.Query("SELECT id, sale_order_id, product_id, distributor_id, quantity, unit_price FROM sale_order_products")
	require.NoError(t, err)
	defer rows.Close()

	if rows.Next() {
		sop := &entities.SaleOrderProduct{}
		entity, err := sop.MapSQLRowToEntity(rows)

		require.NoError(t, err)
		require.IsType(t, &entities.SaleOrderProduct{}, entity)

		assert.Equal(t, testSaleOrderProductID, entity.Id)
		assert.Equal(t, testSaleOrderId, entity.SaleOrderId)
		assert.Equal(t, testProductId, entity.ProductId)
		assert.Equal(t, testDistributorId, entity.DistributorId)
		assert.Equal(t, testQuantity, entity.Quantity)
		assert.Equal(t, testUnitPrice, entity.UnitPrice)
	}
}
