package payloads

type EntityPayload interface {
	GetValues() []interface{}
	MapCsvRecordToPayload(record []string, fieldsIndexes []int) (EntityPayload, error)
}