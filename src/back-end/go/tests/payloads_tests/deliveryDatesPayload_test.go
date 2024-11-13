package payloads_tests

import (
	"testing"

	"nsync/payloads"

	"github.com/stretchr/testify/assert"
)

func TestNewDeliveryPayload(t *testing.T) {

	const (
		expectedCep       = "05510-021"
		expectedProductId = 123
		expectedQuantity  = 10
	)

	deliveryPayload := payloads.NewDeliveryPayload(expectedCep, expectedProductId, expectedQuantity)

	assert.Equal(t, expectedCep, deliveryPayload.Cep)
	assert.Equal(t, expectedProductId, deliveryPayload.ProductId)
	assert.Equal(t, expectedQuantity, deliveryPayload.Quantity)
}
