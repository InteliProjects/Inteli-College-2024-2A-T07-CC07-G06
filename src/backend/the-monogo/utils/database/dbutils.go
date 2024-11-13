// # Package dbUtils
//
// This package provides functions to access and modify the application's database.
//
// It is only used for database transactions.
package dbUtils

import (
	entities "nsync/entities"
	errorhandling "nsync/utils/error-handling"
	"nsync/payloads"
	"database/sql"
	"encoding/csv"
	"fmt"
	"mime/multipart"
	"strings"

	"github.com/lib/pq"
)

type DeliveryQueryResult struct {
	Distributors         []*entities.Distributor
	ProductsDistributors []*entities.ProductDistributor
	Error                error
}

var DB *sql.DB

// Stabilishes connection with the application's database using the PostgreSQL driver.
//
// This function must be called on the "main" function of the application so the *sql.DB.Close() method can be deferred to execute once the application is terminated.
func OpenConnection(connString string) (*sql.DB, error) {
	db, err := sql.Open("postgres", connString)

	if err != nil {
		return nil, errorhandling.WriteError("OpenConnection: Failed to open connection to database.\n%v", err)
	}

	DB = db
	return db, nil
}

// Inserts new content in a table on the application's database based on the specified payload from Package payloads.
//
// The table is selected based on the specified entity from Package entities.
func create[E entities.Entity](p payloads.EntityPayload) (int, error) {
	var entity E

	cols := entity.GetTableColumns()

	sql := fmt.Sprintf("INSERT INTO %s (%s) VALUES (%s) RETURNING id", entity.GetTableName(), strings.Join(cols, ", "), getInsertPlaceholdersString(len(cols)))
	row, queryError := DB.Query(sql, p.GetValues()...)
	if queryError != nil {
		return 0, errorhandling.WriteError("Create[%T]: Failed to execute query on table %s.\n%v", entity, entity.GetTableName(), queryError)
	}
	defer row.Close()

	row.Next()
	var id int
	if scanError := row.Scan(&id); scanError != nil {
		return 0, errorhandling.WriteError("Create[%T]: Failed to get id of created entity.\n%v", entity, scanError)
	}

	if rowError := row.Err(); rowError != nil {
		return 0, errorhandling.WriteError("Create[%T]: Failed to get any result row.\n%v", entity, rowError)
	}

	return id, nil
}

// Returns a string that can be used in a "INSERT" PostgreSQL query as the inserted values placeholders, which will be replaced by arguments.
//
// Example:
//
// getInsertPlaceholdersString(5) => "$1, $2, $3, $4, $5"
func getInsertPlaceholdersString(placeholdersAmount int) string {
	s := new(strings.Builder)
	for i := 1; i < placeholdersAmount; i++ {
		s.WriteString(fmt.Sprintf("$%d, ", i))
	}
	s.WriteString(fmt.Sprintf("$%d", placeholdersAmount))
	return s.String()
}

// Maps an uploaded CSV file into the specified payload from Package payloads.
// Then, inserts the payload into the application's database.
//
// The table is selected based on the specified entity from Package entities.
func ProcessCsvIntoDB[E entities.Entity, P payloads.EntityPayload](fileHeader *multipart.FileHeader) error {
	var entity E

	file, openError := fileHeader.Open()
	if openError != nil {
		return errorhandling.WriteError("ProcessCsvIntoDB[%T]: Failed to open the uploaded file.\n%v", entity, openError)
	}
	defer file.Close()

	reader := csv.NewReader(file)
	headers, readError := reader.Read()
	if readError != nil {
		return errorhandling.WriteError("ProcessCsvIntoDB[%T]: Failed to read the uploaded file.\n%v", entity, readError)
	}

	fields := entity.GetTableColumns()
	if len(headers) != len(fields) {
		return errorhandling.WriteError("ProcessCsvIntoDB[%T]: Uploaded file does not contain required columns.\nFile: %v\nRequired: %v", entity, headers, fields)
	}

	fieldsIndexes := make([]int, len(fields))
	for i, col := range fields {
		for j, header := range headers {
			if strings.ToLower(header) == col {
				fieldsIndexes[i] = j
				break
			}
		}
	}

	var p P
	for record, err := reader.Read(); err == nil; record, err = reader.Read() {
		newP, mapError := p.MapCsvRecordToPayload(record, fieldsIndexes)
		if mapError != nil {
			return mapError
		}

		_, dbError := create[E](newP)
		if dbError != nil {
			return dbError
		}
	}

	return nil
}

func GetAllProducts() ([]*entities.Product, error) {
	rows, queryError := DB.Query("SELECT id, sku, name, price, description, link_image FROM products")
	if queryError != nil {
		return nil, errorhandling.WriteError("GetAllProducts: Failed to execute query on table products.\n%v", queryError)
	}
	defer rows.Close()

	var p entities.Product
	var products []*entities.Product
	for rows.Next() {
		product, mapError := p.MapSQLRowToEntity(rows)
		if mapError != nil {
			return nil, mapError
		}
		products = append(products, product)
	}

	if rowsError := rows.Err(); rowsError != nil {
		return nil, errorhandling.WriteError("GetAllProducts: Failed to get any result rows.\n%v", rowsError)
	}

	return products, nil
}

func GetProductById(id int) (*entities.Product, error) {
	row := DB.QueryRow("SELECT id, sku, name, price, description, link_image FROM products WHERE id = $1", id)

	product := &entities.Product{}
	if scanError := row.Scan(&product.Id, &product.SKU, &product.Name, &product.Price, &product.Description, &product.LinkImage); scanError != nil {
		return nil, errorhandling.WriteError("GetProductById: Failed to scan row into product struct.\n%v", scanError)
	}

	return product, nil
}

func GetProductsByIds(ids []int) ([]*entities.Product, error) {
	sqlQuery := "SELECT id, sku, name, price, description, link_image FROM products WHERE id = ANY($1)"

	rows, queryError := DB.Query(sqlQuery, pq.Array(ids))
	if queryError != nil {
		return nil, errorhandling.WriteError("GetProductsByIds: Failed to execute query.\n%v", queryError)
	}
	defer rows.Close()

	var products []*entities.Product
	for rows.Next() {
		product := &entities.Product{}
		if scanError := rows.Scan(&product.Id, &product.SKU, &product.Name, &product.Price, &product.Description, &product.LinkImage); scanError != nil {
			return nil, errorhandling.WriteError("GetProductsByIds: Failed to scan row.\n%v", scanError)
		}
		products = append(products, product)
	}

	if rowsError := rows.Err(); rowsError != nil {
		return nil, errorhandling.WriteError("GetProductsByIds: Error occurred during row iteration.\n%v", rowsError)
	}

	return products, nil
}

func UpdateProductDistributorRoutine(id int, payload *payloads.ProductDistributorPayload, errorChannel chan error) {
	_, queryError := DB.Query("UPDATE products_distributors SET distributor_id = $1, product_id = $2, quantity_available = $3, quantity_reserved = $4 WHERE id = $5",
								payload.DistributorId, payload.ProductId, payload.AmountAvailable, payload.AmountReserved, id)
	if queryError != nil {
		errorChannel <- errorhandling.WriteError("UpdateProductDistributor: Failed to execute query on table products_distributors.\n%v", queryError)
		return
	}

	errorChannel <- nil
}

func CreateSaleOrderProductRoutine(saleOrderProduct *payloads.SaleOrderProductPayload, errorChannel chan error) {
	_, queryError := DB.Query("INSERT INTO sales_orders_products (sale_order_id, product_id, distributor_id, quantity, unit_price) VALUES ($1, $2, $3, $4, $5)",
								saleOrderProduct.SaleOrderId, saleOrderProduct.ProductId, saleOrderProduct.DistributorId, saleOrderProduct.Quantity, saleOrderProduct.UnitPrice)
	if queryError != nil {
		errorChannel <- errorhandling.WriteError("CreateSaleOrderProduct': Failed to execute query on table sales_orders_products.\n%v", queryError) 
		return
	}

	errorChannel <- nil
}

func CreateSaleOrder(saleOrder *payloads.SaleOrderPayload) (int, error) {
	row, queryError := DB.Query("INSERT INTO sales_orders (customer_cep, total, sale_date, first_delivery_date, last_delivery_date) VALUES ($1, $2, $3, $4, $5) RETURNING id",
								saleOrder.CustomerCep, saleOrder.Total, saleOrder.SaleDate, saleOrder.FirstDeliveryDate, saleOrder.LastDeliveryDate)
	if queryError != nil {
		return 0, errorhandling.WriteError("CreateSaleOrder: Failed to execute query on table sales_orders.\n%v", queryError)
	}
	defer row.Close()

	row.Next()
	var id int
	if scanError := row.Scan(&id); scanError != nil {
		return 0, errorhandling.WriteError("CreateSaleOrder: Failed to get id of created entity.\n%v", scanError)
	}

	if rowError := row.Err(); rowError != nil {
		return 0, errorhandling.WriteError("CreateSaleOrder: Failed to get any result row.\n%v", rowError)
	}

	return id, nil
}

func GetDeliveryDataRoutine(productId int, quantity int, queryChannel chan *DeliveryQueryResult) {
	rows, queryError := DB.Query("SELECT pd.id AS pdId, pd.distributor_id, pd.product_id, pd.quantity_available, pd.quantity_reserved, d.id AS dId, d.name, d.cep FROM products_distributors AS pd JOIN distributors AS d ON pd.distributor_id = d.id WHERE pd.product_id = $1 AND pd.quantity_available >= $2 LIMIT 10",
		productId, quantity)
	if queryError != nil {
		queryChannel <- &DeliveryQueryResult{nil, nil, errorhandling.WriteError("GetDeliveryDataRoutine: Failed to execute SQL query.\n%v", queryError)}
		return
	}
	defer rows.Close()

	var result DeliveryQueryResult
	for rows.Next() {
		var newD entities.Distributor
		var newPd entities.ProductDistributor
		if mapError := rows.Scan(&newPd.Id, &newPd.DistributorId, &newPd.ProductId, &newPd.AmountAvailable, &newPd.AmountReserved, &newD.Id, &newD.Name, &newD.Cep); mapError != nil {
			queryChannel <- &DeliveryQueryResult{nil, nil, errorhandling.WriteError("GetDeliveryDataRoutine: Failed to parse SQL rows into entities.\n%v", mapError)}
			return
		}

		result.Distributors = append(result.Distributors, &newD)
		result.ProductsDistributors = append(result.ProductsDistributors, &newPd)
	}

	if rowsError := rows.Err(); rowsError != nil {
		queryChannel <- &DeliveryQueryResult{nil, nil, errorhandling.WriteError("GetDeliveryDataRoutine: Error reading rows.\n%v", rowsError)}
		return
	}

	queryChannel <- &result
}