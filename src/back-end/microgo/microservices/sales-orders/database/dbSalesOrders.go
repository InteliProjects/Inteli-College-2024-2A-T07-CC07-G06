package database

import (
	"database/sql"
	"micronsync/sales-orders/entities"
	"micronsync/sales-orders/errorhandling"
	"micronsync/sales-orders/payloads"

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

func GetAllSalesOrders() ([]*entities.SaleOrder, error) {
	rows, queryError := DB.Query("SELECT id, customer_cep, total, sale_date, first_delivery_date, last_delivery_date FROM sales_orders")
	if queryError != nil {
		return nil, errorhandling.WriteError("GetAllSalesOrders: Failed to execute query on table sales_orders.\n%v", queryError)
	}
	defer rows.Close()

	var p entities.SaleOrder
	var saleOrders []*entities.SaleOrder
	for rows.Next() {
		SaleOrder, mapError := p.MapSQLRowToEntity(rows)
		if mapError != nil {
			return nil, mapError
		}
		saleOrders = append(saleOrders, SaleOrder)
	}

	if rowsError := rows.Err(); rowsError != nil {
		return nil, errorhandling.WriteError("GetAllSalesOrders: Failed to get any result rows.\n%v", rowsError)
	}

	return saleOrders, nil
}

func GetSaleOrderById(id int) (*entities.SaleOrder, error) {
	row, queryError := DB.Query("SELECT id, customer_cep, total, sale_date, first_delivery_date, last_delivery_date FROM sales_orders WHERE id = $1", id)
	if queryError != nil {
		return nil, errorhandling.WriteError("GetSaleOrderById: Failed to execute query on table sales_orders.\n%v", queryError)
	}
	defer row.Close()

	row.Next()
	var p entities.SaleOrder
	saleOrder, mapError := p.MapSQLRowToEntity(row)
	if mapError != nil {
		return nil, mapError
	}
	
	if rowError := row.Err(); rowError != nil {
		return nil, errorhandling.WriteError("GetSaleOrderById: Failed to get any result rows.\n%v", rowError)
	}
	
	return saleOrder,  nil
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