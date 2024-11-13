package payloads_tests

import (
	"nsync/payloads"
	"testing"

	"github.com/stretchr/testify/assert"
)

func TestNewProductsIdsPayload(t *testing.T) {
	expectedIds := []int{101, 102, 103}

	productsIdsPayload := payloads.NewProductsIdsPayload(expectedIds)

	assert.Equal(t, expectedIds, productsIdsPayload.Ids)
}

func TestProductsIdsPayloadEmptyIds(t *testing.T) {
	expectedIds := []int{}

	productsIdsPayload := payloads.NewProductsIdsPayload(expectedIds)

	assert.Equal(t, expectedIds, productsIdsPayload.Ids)
	assert.Empty(t, productsIdsPayload.Ids)
}
