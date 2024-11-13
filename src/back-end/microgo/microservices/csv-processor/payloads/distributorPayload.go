package payloads

import "micronsync/csv-processor/errorhandling"

type DistributorPayload struct {
	Name string `json:"name"	validate:"required"`
	Cep  string `json:"cep"		validate:"required"`
}

func NewDistributorPayload(name string, cep string) *DistributorPayload {
	return &DistributorPayload{
		Name: name,
		Cep:  cep,
	}
}

func (d *DistributorPayload) GetValues() []interface{} {
	return []interface{}{d.Name, d.Cep}
}

func (*DistributorPayload) MapCsvRecordToPayload(record []string, fieldsIndexes []int) (EntityPayload, error) {
	name := record[fieldsIndexes[0]]
	cep := record[fieldsIndexes[1]]
	if len(name) == 0 || len(cep) == 0 {
		return nil, errorhandling.WriteError("MapCsvRecordToPayload[DistributorPayload]: Failed to copy CSV values into struct (EMPTY VALUES).")
	}
	return NewDistributorPayload(name, cep), nil
}
