package payloads_tests

import (
	"nsync/payloads"
	"testing"

	"github.com/stretchr/testify/assert"
)

const (
	expectedSaleOrderId      = 1001
	expectedOPPProductId     = 2001
	expectedOPPDistributorId = 3001
	expectedQuantity         = 10
	expectedUnitPrice        = 99.99
)

func TestNewSaleOrderProductPayload(t *testing.T) {
	payload := payloads.NewSaleOrderProductPayload(expectedSaleOrderId, expectedOPPProductId, expectedOPPDistributorId, expectedQuantity, expectedUnitPrice)

	assert.Equal(t, expectedSaleOrderId, payload.SaleOrderId)
	assert.Equal(t, expectedOPPProductId, payload.ProductId)
	assert.Equal(t, expectedOPPDistributorId, payload.DistributorId)
	assert.Equal(t, expectedQuantity, payload.Quantity)
	assert.Equal(t, expectedUnitPrice, payload.UnitPrice)
}

func TestSaleOrderProductPayloadGetValues(t *testing.T) {
	payload := payloads.NewSaleOrderProductPayload(expectedSaleOrderId, expectedOPPProductId, expectedOPPDistributorId, expectedQuantity, expectedUnitPrice)

	values := payload.GetValues()

	assert.Equal(t, 5, len(values))
	assert.Equal(t, expectedSaleOrderId, values[0])
	assert.Equal(t, expectedOPPProductId, values[1])
	assert.Equal(t, expectedOPPDistributorId, values[2])
	assert.Equal(t, expectedQuantity, values[3])
	assert.Equal(t, expectedUnitPrice, values[4])
}
