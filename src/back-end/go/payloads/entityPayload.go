// # Package payloads
//
// This package provides data structures equivalent to the structures received by the application handlers as payloads on the HTTP requests.
//
// It is mainly used for database transactions and to process "Post"/"Update" requests responses to the application's front-end, as all payloads can be Unmarshalled from JSON.
package payloads

// # EntityPayload Interface
//
// The entity payload interface type defines methods that all entitiy payloads in the Package payloads have to implement.
//
// Entity payloads are payloads received in "Post"/"Update" requests that refer to an entity from the Package entities.
//
// It is mainly used for generic transactions in the code, as it enables the type matching to any entity payload in the Package payloads.
type EntityPayload interface {
	// Returns a slice containing all the values present in the payload struct, in the same order as the equivalent entity fields appear on the application's database.
	GetValues() []interface{}
	// Converts a CSV file record (row) into the payload type.
	// As parameters, the method requires the CSV file record and the order of appearance of the payload's fields (according to the equivalent entity order of fields) in the CSV headers.
	//
	// Example:
	//
	// Distributor (entity): { Id, Name, Cep }
	//
	// Distributor (payload): { Name, Cep }
	//
	// CSV file headers: "Cep", "Name"
	//
	// Correct fieldsIndexes parameter: { 1, 0 } (would be { 0, 1 } if the CSV file headers were ordered as the Distributor fields, like "Name", "Cep")
	MapCsvRecordToPayload(record []string, fieldsIndexes []int) (EntityPayload, error)
}