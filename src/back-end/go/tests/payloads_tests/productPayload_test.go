package payloads_tests

import (
	"nsync/payloads"
	"testing"

	"github.com/stretchr/testify/assert"
)

const (
	expectedSKU         = "12345"
	expectedName        = "Product Name"
	expectedPrice       = 99.99
	expectedDescription = "This is a product description."
	expectedLinkImage   = "http://example.com/image.png"
)

func TestNewProductPayload(t *testing.T) {

	productPayload := payloads.NewProductPayload(expectedSKU, expectedName, expectedPrice, expectedDescription, expectedLinkImage)

	assert.Equal(t, expectedSKU, productPayload.SKU)
	assert.Equal(t, expectedName, productPayload.Name)
	assert.Equal(t, expectedPrice, productPayload.Price)
	assert.Equal(t, expectedDescription, productPayload.Description)
	assert.Equal(t, expectedLinkImage, productPayload.LinkImage)
}

func TestProductPayloadGetValues(t *testing.T) {
	productPayload := payloads.NewProductPayload(expectedSKU, expectedName, expectedPrice, expectedDescription, expectedLinkImage)

	values := productPayload.GetValues()

	assert.Equal(t, 5, len(values))
	assert.Equal(t, expectedSKU, values[0])
	assert.Equal(t, expectedName, values[1])
	assert.Equal(t, expectedPrice, values[2])
	assert.Equal(t, expectedDescription, values[3])
	assert.Equal(t, expectedLinkImage, values[4])
}
