package payloads_tests

import (
	"testing"

	"nsync/payloads"

	"github.com/stretchr/testify/assert"
)

const (
	expectedDistributor = "Distributor 1"
	expectedCep         = "05510-021"
)

func TestNewDistributorPayload(t *testing.T) {

	distributorPayload := payloads.NewDistributorPayload(expectedDistributor, expectedCep)

	assert.Equal(t, expectedDistributor, distributorPayload.Name)
	assert.Equal(t, expectedCep, distributorPayload.Cep)
}

func TestDistributorPayloadGetValues(t *testing.T) {
	distributorPayload := payloads.NewDistributorPayload(expectedDistributor, expectedCep)

	values := distributorPayload.GetValues()

	assert.Equal(t, 2, len(values))
	assert.Equal(t, expectedDistributor, values[0])
	assert.Equal(t, expectedCep, values[1])
}
