package entities_tests

import (
	"testing"

	"nsync/entities"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/assert"
)

const (
	testProductID          = 1
	testProductSKU         = "SKU123"
	testProductName        = "Alive's t-shirt"
	testProductPrice       = 119.99
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

func TestGetTableName(t *testing.T) {
	product := &entities.Product{}
	tableName := product.GetTableName()

	assert.Equal(t, "products", tableName)
}

func TestProductGetTableName(t *testing.T) {
	product := &entities.Product{}
	columns := product.GetTableColumns()

	expectedColumns := []string{"sku", "name", "price", "description", "link_image"}
	assert.Equal(t, expectedColumns, columns)
}

func TestProductMapSQLRowToEntity(t *testing.T) {
	db, mock, err := sqlmock.New()
	assert.NoError(t, err)
	defer db.Close()

	mockRows := sqlmock.NewRows([]string{"id", "sku", "name", "price", "description", "link_image"}).
		AddRow(testProductID, testProductSKU, testProductName, testProductPrice, testProductDescription, testProductLinkImage)

	mock.ExpectQuery("SELECT (.+) FROM products").
		WillReturnRows(mockRows)

	rows, err := db.Query("SELECT id, sku, name, price, description, link_image FROM products")
	assert.NoError(t, err)
	defer rows.Close()

	if rows.Next() {
		product := &entities.Product{}
		entity, err := product.MapSQLRowToEntity(rows)

		assert.NoError(t, err)
		assert.IsType(t, &entities.Product{}, entity)
		assert.Equal(t, testProductID, entity.(*entities.Product).Id)
		assert.Equal(t, testProductSKU, entity.(*entities.Product).SKU)
		assert.Equal(t, testProductName, entity.(*entities.Product).Name)
		assert.Equal(t, testProductPrice, entity.(*entities.Product).Price)
		assert.Equal(t, testProductDescription, entity.(*entities.Product).Description)
		assert.Equal(t, testProductLinkImage, entity.(*entities.Product).LinkImage)
	}
}
