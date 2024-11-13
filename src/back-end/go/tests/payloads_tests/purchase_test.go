package payloads_tests

import (
	"nsync/payloads"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestNewPurchasePayloadProduct(t *testing.T) {
	expectedProductId := 101
	expectedQuantity := 2

	product := payloads.NewPurchasePayloadProduct(expectedProductId, expectedQuantity)

	assert.Equal(t, expectedProductId, product.ProductId)
	assert.Equal(t, expectedQuantity, product.Quantity)
}

func TestNewPurchasePayload(t *testing.T) {
	expectedProducts := []payloads.PurchasePayloadProduct{
		*payloads.NewPurchasePayloadProduct(101, 2),
		*payloads.NewPurchasePayloadProduct(102, 1),
	}
	expectedCep := "12345-678"
	expectedTotal := 150.75

	purchasePayload := payloads.NewPurchasePayload(expectedProducts, expectedCep, expectedTotal)

	assert.Equal(t, expectedProducts, purchasePayload.Products)
	assert.Equal(t, expectedCep, purchasePayload.CustomerCep)
	assert.Equal(t, expectedTotal, purchasePayload.Total)
}

func TestNewPurchasePayloadWithEmptyProducts(t *testing.T) {
	expectedProducts := []payloads.PurchasePayloadProduct{}
	expectedCep := "12345-678"
	expectedTotal := 0.0

	purchasePayload := payloads.NewPurchasePayload(expectedProducts, expectedCep, expectedTotal)

	assert.Empty(t, purchasePayload.Products)
	assert.Equal(t, expectedCep, purchasePayload.CustomerCep)
	assert.Equal(t, expectedTotal, purchasePayload.Total)
}
