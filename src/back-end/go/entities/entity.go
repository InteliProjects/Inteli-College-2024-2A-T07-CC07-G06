// # Package entities
//
// This package provides data structures equivalent to the structures storaged in the application's database.
//
// It is mainly used for database transactions and to return "Get" requests responses to the application's front-end, as all entities can be Marshalled to JSON.
package entities

import "database/sql"

// # Entity Interface
//
// The entity interface type defines methods that all entities in the Package entities have to implement.
//
// It is mainly used for generic transactions in the code, as it enables the type matching to any entity in the Package entities.
type Entity interface {
	// Returns the name of the table that stores the entity's data in the application's database.
	GetTableName() string
	// Returns the columns of the table that correspond to the entity's data in the application's database (id column excluded).
	GetTableColumns() []string
	// Converts a row of data from the application's database into the entity type.
	// The row's content must be ordered in the same order as the columns returned by GetTableColumns method.
	MapSQLRowToEntity(row *sql.Rows) (Entity, error)
}