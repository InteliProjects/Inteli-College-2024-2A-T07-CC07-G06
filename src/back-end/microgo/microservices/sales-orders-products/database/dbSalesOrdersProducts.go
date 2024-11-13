package database

import (
	"database/sql"
	"micronsync/sales-orders-products/entities"
	"micronsync/sales-orders-products/errorhandling"
	"micronsync/sales-orders-products/payloads"

	_ "github.com/lib/pq"
)

var DB *sql.DB

func OpenConnection(connString string) (*sql.DB, error) {
	db, err := sql.Open("postgres", connString)

	if err != nil {
		return nil, errorhandling.WriteError("OpenConnection: Failed to open connection to database.\n%v", err)
	}

	DB = db
	return db, nil
}

func GetAllSalesOrdersProducts() ([]*entities.SaleOrderProduct, error) {
	rows, queryError := DB.Query("SELECT id, sale_order_id, product_id, distributor_id, quantity, unit_price FROM sales_orders_products")
	if queryError != nil {
		return nil, errorhandling.WriteError("GetAllSalesOrdersProducts: Failed to execute query on table sales_orders_products.\n%v", queryError)
	}
	defer rows.Close()

	var p entities.SaleOrderProduct
	var saleOrderProducts []*entities.SaleOrderProduct
	for rows.Next() {
		saleOrderProduct, mapError := p.MapSQLRowToEntity(rows)
		if mapError != nil {
			return nil, mapError
		}
		saleOrderProducts = append(saleOrderProducts, saleOrderProduct)
	}

	if rowsError := rows.Err(); rowsError != nil {
		return nil, errorhandling.WriteError("GetAllSalesOrdersProducts: Failed to get any result rows.\n%v", rowsError)
	}

	return saleOrderProducts, nil
}

func GetSaleOrderProductById(id int) (*entities.SaleOrderProduct, error) {
	row, queryError := DB.Query("SELECT id, sale_order_id, product_id, distributor_id, quantity, unit_price FROM sales_orders_products WHERE id = $1", id)
	if queryError != nil {
		return nil, errorhandling.WriteError("GetSaleOrderProductById: Failed to execute query on table sales_orders_products.\n%v", queryError)
	}
	defer row.Close()

	row.Next()
	var p entities.SaleOrderProduct
	saleOrderProduct, mapError := p.MapSQLRowToEntity(row)
	if mapError != nil {
		return nil, mapError
	}
	
	if rowError := row.Err(); rowError != nil {
		return nil, errorhandling.WriteError("GetSaleOrderProductById: Failed to get any result rows.\n%v", rowError)
	}
	
	return saleOrderProduct,  nil
}

func CreateSaleOrderProduct(saleOrderProduct *payloads.SaleOrderProductPayload) (int, error) {
	row, queryError := DB.Query("INSERT INTO sales_orders_products (sale_order_id, product_id, distributor_id, quantity, unit_price) VALUES ($1, $2, $3, $4, $5) RETURNING id",
								saleOrderProduct.SaleOrderId, saleOrderProduct.ProductId, saleOrderProduct.DistributorId, saleOrderProduct.Quantity, saleOrderProduct.UnitPrice)
	if queryError != nil {
		return 0, errorhandling.WriteError("CreateSaleOrderProduct': Failed to execute query on table sales_orders_products.\n%v", queryError)
	}
	defer row.Close()

	row.Next()
	var id int
	if scanError := row.Scan(&id); scanError != nil {
		return 0, errorhandling.WriteError("CreateSaleOrderProduct': Failed to get id of created entity.\n%v", scanError)
	}

	if rowError := row.Err(); rowError != nil {
		return 0, errorhandling.WriteError("CreateSaleOrderProduct': Failed to get any result row.\n%v", rowError)
	}

	return id, nil
}