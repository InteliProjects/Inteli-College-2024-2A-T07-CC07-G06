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

	_ "github.com/lib/pq"
)

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

// Returns all content from a table on the application's database, mapped into the specified entity from Package entities.
//
// The table is selected based on the entity.
func GetAll[E entities.Entity]() ([]entities.Entity, error) {
	var entity E

	sql := fmt.Sprintf("SELECT id, %s FROM %s", strings.Join(entity.GetTableColumns(), ", "), entity.GetTableName())
	rows, queryError := DB.Query(sql)
	if queryError != nil {
		return nil, errorhandling.WriteError("GetAll[%T]: Failed to execute query on table %s.\n%v", entity, entity.GetTableName(), queryError)
	}
	defer rows.Close()

	var result []entities.Entity
	for rows.Next() {
		e, mapError := entity.MapSQLRowToEntity(rows)
		if mapError != nil {
			return nil, mapError
		}
		result = append(result, e)
	}

	if rowsError := rows.Err(); rowsError != nil {
		return nil, errorhandling.WriteError("GetAll[%T]: Failed to get any result rows.\n%v", entity, rowsError)
	}

	return result, nil
}

// Returns content from a table on the application's database referring to the specified row id, mapped to the specified entity from Package entities.
//
// The table is selected based on the entity.
func GetById[E entities.Entity](id int) (entities.Entity, error) {
	var entity E

	sql := fmt.Sprintf("SELECT id, %s FROM %s WHERE id = $1", strings.Join(entity.GetTableColumns(), ", "), entity.GetTableName())
	row, queryError := DB.Query(sql, id)
	if queryError != nil {
		return entity, errorhandling.WriteError("GetById[%T]: Failed to execute query on table %s with id %v.\n%v", entity, entity.GetTableName(), id, queryError)
	}
	defer row.Close()

	row.Next()
	result, mapError := entity.MapSQLRowToEntity(row)
	if mapError != nil {
		return entity, mapError
	}

	if rowError := row.Err(); rowError != nil {
		return entity, errorhandling.WriteError("GetById[%T]: Failed to get any result row.\n%v", entity, rowError)
	}

	return result, nil
}

// Inserts new content in a table on the application's database based on the specified payload from Package payloads.
//
// The table is selected based on the specified entity from Package entities.
func Create[E entities.Entity](p payloads.EntityPayload) (int, error) {
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

// Updates content in a table on the application's database referring to the specified row id.
//
// The table is selected based on the specified entity from Package entities.
//
// The new data is based on the specified payload from Package payloads, related to the entity.
func Update[E entities.Entity](id int, p payloads.EntityPayload) error {
	var entity E

	sql := fmt.Sprintf("UPDATE %s SET %s WHERE id = %v", entity.GetTableName(), getUpdatePlaceholdersString(entity.GetTableColumns()), id)
	_, queryError := DB.Query(sql, p.GetValues()...)
	if queryError != nil {
		return errorhandling.WriteError("Create[%T]: Failed to execute query on table %s.\n%v", entity, entity.GetTableName(), queryError)
	}
	return nil
}

// Deletes content in a table on the application's database referring to the specified row id.
//
// The table is selected based on the specified entity from Package entities.
func Delete[E entities.Entity](id int) error {
	var entity E

	sql := fmt.Sprintf("DELETE FROM %s WHERE id = $1", entity.GetTableName())
	_, queryError := DB.Query(sql, id)
	if queryError != nil {
		return errorhandling.WriteError("Delete[%T]: Failed to execute query on table %s with id %v.\n%v", entity, entity.GetTableName(), id, queryError)
	}

	return nil
}

// Executes a custom query with the intent to modify or return content from only one table in the application's database (one entity).
//
// The "sql" string parameter must be a SQL query that respects PostgreSQL syntax.
// The "args" parameter is used to replace any placeholders ("$1", "$2"...) present in the SQL query, in order of appearance.
//
// The table is selected based on the specified entity from Package entities.
func QueryEntity[E entities.Entity](sql string, args ...any) ([]E, error) {
	var entity E
	rows, queryError := DB.Query(sql, args...)
	if queryError != nil {
		return nil, errorhandling.WriteError("QueryEntity[%T]: Failed to execute following query: %s.\n%v", entity, sql, queryError)
	}
	defer rows.Close()

	var result []E
	for rows.Next() {
		e, mapError := entity.MapSQLRowToEntity(rows)
		if mapError != nil {
			return nil, mapError
		}
		result = append(result, e.(E))
	}

	if rowsError := rows.Err(); rowsError != nil {
		return nil, errorhandling.WriteError("QueryEntity[%T]: Error reading rows.\n%v", entity, rowsError)
	}

	return result, nil
}

// Executes a custom query on the application's database, returning the correspondent results.
//
// The usage of this function should be avoided as it does not restrict the actions that can be made onto the database, which is unsafe.
// Only use it if you are really sure about your SQL query.
//
// The "sql" string parameter must be a SQL query that respects PostgreSQL syntax.
// The "args" parameter is used to replace any placeholders ("$1", "$2"...) present in the SQL query, in order of appearance.
func CustomQuery(sql string, args ...any) (*sql.Rows, error) {
	rows, queryError := DB.Query(sql, args...)
	if queryError != nil {
		return nil, errorhandling.WriteError("CustomQuery: Failed to execute following query: %s.\n%v", sql, queryError)
	}

	return rows, nil
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

		_, dbError := Create[E](newP)
		if dbError != nil {
			return dbError
		}
	}

	return nil
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

// Returns a string that can be used in a "UPDATE" PostgreSQL query as the columns to be updated and the correspondent values placeholders, which will be replaced by arguments.
//
// Example:
//
// getUpdatePlaceholdersString({ "id", "name", "cep" }) => "id = $1, name = $2, cep = $3"
func getUpdatePlaceholdersString(columns []string) string {
	s := new(strings.Builder)
	last := len(columns) - 1
	for i := 0; i < last; i++ {
		s.WriteString(fmt.Sprintf("%s = $%d, ", columns[i], i + 1))
	}
	s.WriteString(fmt.Sprintf("%s = $%d", columns[last], last + 1))
	return s.String()
}