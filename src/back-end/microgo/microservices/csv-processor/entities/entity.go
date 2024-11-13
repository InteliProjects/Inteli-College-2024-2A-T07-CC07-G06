package entities

type Entity interface {
	GetTableName() string
	GetTableColumns() []string
}