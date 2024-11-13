package entities_tests

import (
	"testing"

	"nsync/entities"

	"github.com/stretchr/testify/assert"
)

const (
	testSaleOrderProductID = 1
	testSOPSaleOrderID     = 2
	testSOPProductID       = 3
	testDistributorID      = 4
	testQuantity           = 10
	testUnitPrice          = 99.99
)

func TestNewSaleOrderProduct(t *testing.T) {
	sop := entities.NewSaleOrderProduct(testSaleOrderProductID, testSOPSaleOrderID, testSOPProductID, testDistributorID, testQuantity, testUnitPrice)

	assert.Equal(t, testSaleOrderProductID, sop.Id)
	assert.Equal(t, testSOPSaleOrderID, sop.SaleOrderId)
	assert.Equal(t, testSOPProductID, sop.ProductId)
	assert.Equal(t, testDistributorID, sop.DistributorId)
	assert.Equal(t, testQuantity, sop.Quantity)
	assert.Equal(t, testUnitPrice, sop.UnitPrice)
}

func TestSaleOrderProductGetTableName(t *testing.T) {
	sop := &entities.SaleOrderProduct{}
	tableName := sop.GetTableName()

	assert.Equal(t, "sales_orders_products", tableName)
}

func TestSaleOrderProductGetTableColumns(t *testing.T) {
	sop := &entities.SaleOrderProduct{}
	columns := sop.GetTableColumns()

	expectedColumns := []string{"sale_order_id", "product_id", "distributor_id", "quantity", "unit_price"}
	assert.Equal(t, expectedColumns, columns)
}
