package tests

import (
	"database/sql"
	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"micronsync/sales-orders/entities"
	"testing"
	"time"
)

const (
	testSaleOrderID = 1
	testCustomerCep = "05510-021"
	testTotal       = 1299.99
)

var (
	testSaleDate          = time.Date(2023, 9, 23, 0, 0, 0, 0, time.UTC)
	testFirstDeliveryDate = time.Date(2023, 9, 30, 0, 0, 0, 0, time.UTC)
	testLastDeliveryDate  = time.Date(2023, 10, 7, 0, 0, 0, 0, time.UTC)
)

func TestNewSaleOrder(t *testing.T) {
	saleOrder := entities.NewSaleOrder(testSaleOrderID, testCustomerCep, testTotal, testSaleDate, testFirstDeliveryDate, testLastDeliveryDate)

	assert.Equal(t, testSaleOrderID, saleOrder.Id)
	assert.Equal(t, testCustomerCep, saleOrder.CustomerCep)
	assert.Equal(t, testTotal, saleOrder.Total)
	assert.Equal(t, testSaleDate, saleOrder.SaleDate)
	assert.Equal(t, testFirstDeliveryDate, saleOrder.FirstDeliveryDate)
	assert.Equal(t, testLastDeliveryDate, saleOrder.LastDeliveryDate)
}

func setupSQLMock(t *testing.T) (*sql.DB, sqlmock.Sqlmock) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err, "Error creating sqlmock")
	return db, mock
}

func TestSaleOrderMapSQLRowToEntity(t *testing.T) {
	db, mock := setupSQLMock(t)
	defer db.Close()

	mockRows := sqlmock.NewRows([]string{"id", "customer_cep", "total", "sale_date", "first_delivery_date", "last_delivery_date"}).
		AddRow(testSaleOrderID, testCustomerCep, testTotal, testSaleDate, testFirstDeliveryDate, testLastDeliveryDate)

	mock.ExpectQuery("SELECT (.+) FROM sale_orders").WillReturnRows(mockRows)

	rows, err := db.Query("SELECT id, customer_cep, total, sale_date, first_delivery_date, last_delivery_date FROM sale_orders")
	require.NoError(t, err)
	defer rows.Close()

	if rows.Next() {
		saleOrder := &entities.SaleOrder{}
		entity, err := saleOrder.MapSQLRowToEntity(rows)

		require.NoError(t, err)
		require.IsType(t, &entities.SaleOrder{}, entity)

		assert.Equal(t, testSaleOrderID, entity.Id)
		assert.Equal(t, testCustomerCep, entity.CustomerCep)
		assert.Equal(t, testTotal, entity.Total)
		assert.Equal(t, testSaleDate, entity.SaleDate)
		assert.Equal(t, testFirstDeliveryDate, entity.FirstDeliveryDate)
		assert.Equal(t, testLastDeliveryDate, entity.LastDeliveryDate)
	}
}
