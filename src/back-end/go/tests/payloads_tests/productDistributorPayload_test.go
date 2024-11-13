package payloads_tests

import (
	"nsync/payloads"
	"testing"

	"github.com/stretchr/testify/assert"
)

const (
	expectedDistributorId   = 1
	expectedProductId       = 101
	expectedAmountAvailable = 50
	expectedAmountReserved  = 10
)

func TestNewProductDistributorPayload(t *testing.T) {

	productDistributorPayload := payloads.NewProductDistributorPayload(expectedDistributorId, expectedProductId, expectedAmountAvailable, expectedAmountReserved)

	assert.Equal(t, expectedDistributorId, productDistributorPayload.DistributorId)
	assert.Equal(t, expectedProductId, productDistributorPayload.ProductId)
	assert.Equal(t, expectedAmountAvailable, productDistributorPayload.AmountAvailable)
	assert.Equal(t, expectedAmountReserved, productDistributorPayload.AmountReserved)
}

func TestProductDistributorPayloadGetValues(t *testing.T) {
	productDistributorPayload := payloads.NewProductDistributorPayload(expectedDistributorId, expectedProductId, expectedAmountAvailable, expectedAmountReserved)

	values := productDistributorPayload.GetValues()

	assert.Equal(t, 4, len(values))
	assert.Equal(t, expectedDistributorId, values[0])
	assert.Equal(t, expectedProductId, values[1])
	assert.Equal(t, expectedAmountAvailable, values[2])
	assert.Equal(t, expectedAmountReserved, values[3])
}
