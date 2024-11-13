package payloads_tests

import (
	"nsync/payloads"
	"testing"
	"time"

	"github.com/stretchr/testify/assert"
)

func TestNewSaleOrderPayload(t *testing.T) {
	expectedCustomerCep := "12345-678"
	expectedTotal := 250.50
	expectedSaleDate := time.Now()
	expectedFirstDeliveryDate := expectedSaleDate.AddDate(0, 0, 7)
	expectedLastDeliveryDate := expectedSaleDate.AddDate(0, 0, 14)

	saleOrderPayload := payloads.NewSaleOrderPayload(expectedCustomerCep, expectedTotal, expectedSaleDate, expectedFirstDeliveryDate, expectedLastDeliveryDate)

	assert.Equal(t, expectedCustomerCep, saleOrderPayload.CustomerCep)
	assert.Equal(t, expectedTotal, saleOrderPayload.Total)
	assert.Equal(t, expectedSaleDate, saleOrderPayload.SaleDate)
	assert.Equal(t, expectedFirstDeliveryDate, saleOrderPayload.FirstDeliveryDate)
	assert.Equal(t, expectedLastDeliveryDate, saleOrderPayload.LastDeliveryDate)
}

func TestSaleOrderPayloadGetValues(t *testing.T) {
	expectedCustomerCep := "12345-678"
	expectedTotal := 250.50
	expectedSaleDate := time.Now()
	expectedFirstDeliveryDate := expectedSaleDate.AddDate(0, 0, 7)
	expectedLastDeliveryDate := expectedSaleDate.AddDate(0, 0, 14)

	saleOrderPayload := payloads.NewSaleOrderPayload(expectedCustomerCep, expectedTotal, expectedSaleDate, expectedFirstDeliveryDate, expectedLastDeliveryDate)

	values := saleOrderPayload.GetValues()

	assert.Equal(t, 5, len(values))
	assert.Equal(t, expectedCustomerCep, values[0])
	assert.Equal(t, expectedTotal, values[1])
	assert.Equal(t, expectedSaleDate, values[2])
	assert.Equal(t, expectedFirstDeliveryDate, values[3])
	assert.Equal(t, expectedLastDeliveryDate, values[4])
}
