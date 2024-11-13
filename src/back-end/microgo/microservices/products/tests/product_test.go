package tests

import (
	"database/sql"
	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"micronsync/products/entities"
	"testing"
)

const (
	testProductID          = 1
	testProductSKU         = "SKU123"
	testProductName        = "Alive's t-shirt"
	testProductPrice       = 1299.99
	testProductDescription = "I doubt they'll give us an alive's t-shirt"
	testProductLinkImage   = "http://alives-t-shirt.com/alives-t-shirt.jpg"
)

func TestNewProduct(t *testing.T) {
	product := entities.NewProduct(testProductID, testProductSKU, testProductName, testProductPrice, testProductDescription, testProductLinkImage)

	assert.Equal(t, testProductID, product.Id)
	assert.Equal(t, testProductSKU, product.SKU)
	assert.Equal(t, testProductName, product.Name)
	assert.Equal(t, testProductPrice, product.Price)
	assert.Equal(t, testProductDescription, product.Description)
	assert.Equal(t, testProductLinkImage, product.LinkImage)
}

func setupSQLMock(t *testing.T) (*sql.DB, sqlmock.Sqlmock) {
	db, mock, err := sqlmock.New()
	require.NoError(t, err, "Error creating sqlmock")
	return db, mock
}

func TestProductMapSQLRowToEntity(t *testing.T) {
	db, mock := setupSQLMock(t)
	defer db.Close()

	mockRows := sqlmock.NewRows([]string{"id", "sku", "name", "price", "description", "link_image"}).
		AddRow(testProductID, testProductSKU, testProductName, testProductPrice, testProductDescription, testProductLinkImage)

	mock.ExpectQuery("SELECT (.+) FROM products").
		WillReturnRows(mockRows)

	rows, err := db.Query("SELECT id, sku, name, price, description, link_image FROM products")
	require.NoError(t, err)
	defer rows.Close()

	if rows.Next() {
		product := &entities.Product{}
		entity, err := product.MapSQLRowToEntity(rows)

		require.NoError(t, err)
		require.IsType(t, &entities.Product{}, entity)

		assert.Equal(t, testProductID, entity.Id)
		assert.Equal(t, testProductSKU, entity.SKU)
		assert.Equal(t, testProductName, entity.Name)
		assert.Equal(t, testProductPrice, entity.Price)
		assert.Equal(t, testProductDescription, entity.Description)
		assert.Equal(t, testProductLinkImage, entity.LinkImage)
	}
}
