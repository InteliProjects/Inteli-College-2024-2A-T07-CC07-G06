package entities_tests

import (
	"testing"
	"time"

	"nsync/entities"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/assert"
)

const (
	testSaleOrderID = 1
	testCustomerCep = "05510-021"
	testTotal       = 199.99
)

var (
	testSaleDate          = time.Date(2023, 9, 1, 12, 0, 0, 0, time.UTC)
	testFirstDeliveryDate = time.Date(2023, 9, 3, 12, 0, 0, 0, time.UTC)
	testLastDeliveryDate  = time.Date(2023, 9, 7, 12, 0, 0, 0, time.UTC)
)

func TestNewSaleOrder(t *testing.T) {
	so := entities.NewSaleOrder(testSaleOrderID, testCustomerCep, testTotal, testSaleDate, testFirstDeliveryDate, testLastDeliveryDate)

	assert.Equal(t, testSaleOrderID, so.Id)
	assert.Equal(t, testCustomerCep, so.CustomerCep)
	assert.Equal(t, testTotal, so.Total)
	assert.Equal(t, testSaleDate, so.SaleDate)
	assert.Equal(t, testFirstDeliveryDate, so.FirstDeliveryDate)
	assert.Equal(t, testLastDeliveryDate, so.LastDeliveryDate)
}

func TestSaleOrderGetTableName(t *testing.T) {
	so := &entities.SaleOrder{}
	tableName := so.GetTableName()

	assert.Equal(t, "sales_orders", tableName)
}

func TestSaleOrderGetTableColumns(t *testing.T) {
	so := &entities.SaleOrder{}
	columns := so.GetTableColumns()

	expectedColumns := []string{"customer_cep", "total", "sale_date", "first_delivery_date", "last_delivery_date"}
	assert.Equal(t, expectedColumns, columns)
}

func TestSaleOrderMapSQLRowToEntity(t *testing.T) {
	db, mock, err := sqlmock.New()
	assert.NoError(t, err)
	defer db.Close()

	mockRows := sqlmock.NewRows([]string{"id", "customer_cep", "total", "sale_date", "first_delivery_date", "last_delivery_date"}).
		AddRow(testSaleOrderID, testCustomerCep, testTotal, testSaleDate, testFirstDeliveryDate, testLastDeliveryDate)

	mock.ExpectQuery("SELECT (.+) FROM sales_orders").
		WillReturnRows(mockRows)

	rows, err := db.Query("SELECT id, customer_cep, total, sale_date, first_delivery_date, last_delivery_date FROM sales_orders")
	assert.NoError(t, err)
	defer rows.Close()

	if rows.Next() {
		so := &entities.SaleOrder{}
		entity, err := so.MapSQLRowToEntity(rows)

		assert.NoError(t, err)
		assert.IsType(t, &entities.SaleOrder{}, entity)
		assert.Equal(t, testSaleOrderID, entity.(*entities.SaleOrder).Id)
		assert.Equal(t, testCustomerCep, entity.(*entities.SaleOrder).CustomerCep)
		assert.Equal(t, testTotal, entity.(*entities.SaleOrder).Total)
		assert.Equal(t, testSaleDate, entity.(*entities.SaleOrder).SaleDate)
		assert.Equal(t, testFirstDeliveryDate, entity.(*entities.SaleOrder).FirstDeliveryDate)
		assert.Equal(t, testLastDeliveryDate, entity.(*entities.SaleOrder).LastDeliveryDate)
	}
}
