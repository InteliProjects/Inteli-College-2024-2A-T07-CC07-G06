package payloads

import errorhandling "nsync/utils/error-handling"

// # Distributor Payload
//
// The Distributor Payload type is defined as a struct that can store all the parsed data from a JSON request referring to distributors informations.
// It implements the Entity Interface methods and have its own constructor defined as follows:
//
// func NewDistributorPayload(name string, cep string) *DistributorPayload
type DistributorPayload struct {
	Name string `json:"name"	validate:"required"`
	Cep  string `json:"cep"		validate:"required"`
}

// Returns a pointer to a new Distributor Payload type struct containing the data from the parameters.
func NewDistributorPayload(name string, cep string) *DistributorPayload {
	return &DistributorPayload{
		Name: name,
		Cep:  cep,
	}
}

// Returns a slice containing all the values present in the payload struct, in the same order as the distributor entity fields appear on the application's database.
func (d *DistributorPayload) GetValues() []interface{} {
	return []interface{}{d.Name, d.Cep}
}

// Maps a CSV file record (row) into a new DistributorPayload struct.
// For more info about the fieldsIndexes parameter, check the description of the EntityPayload interface method MapCsvRecordToPayload.
func (*DistributorPayload) MapCsvRecordToPayload(record []string, fieldsIndexes []int) (EntityPayload, error) {
	name := record[fieldsIndexes[0]]
	cep := record[fieldsIndexes[1]]
	if len(name) == 0 || len(cep) == 0 {
		return nil, errorhandling.WriteError("MapCsvRecordToPayload[DistributorPayload]: Failed to copy CSV values into struct (EMPTY VALUES).")
	}
	return NewDistributorPayload(name, cep), nil
}