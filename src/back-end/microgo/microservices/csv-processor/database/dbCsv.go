package database

import (
	"database/sql"
	"encoding/csv"
	"fmt"
	"micronsync/csv-processor/entities"
	"micronsync/csv-processor/errorhandling"
	"micronsync/csv-processor/payloads"
	"mime/multipart"
	"strings"

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

		dbError := create[E](newP)
		if dbError != nil {
			return dbError
		}
	}

	return nil
}

func create[E entities.Entity](p payloads.EntityPayload) error {
	var entity E

	cols := entity.GetTableColumns()

	sql := fmt.Sprintf("INSERT INTO %s (%s) VALUES (%s)", entity.GetTableName(), strings.Join(cols, ", "), getInsertPlaceholdersString(len(cols)))
	_, queryError := DB.Query(sql, p.GetValues()...)
	if queryError != nil {
		return errorhandling.WriteError("Create[%T]: Failed to execute query on table %s.\n%v", entity, entity.GetTableName(), queryError)
	}

	return nil
}

func getInsertPlaceholdersString(placeholdersAmount int) string {
	s := new(strings.Builder)
	for i := 1; i < placeholdersAmount; i++ {
		s.WriteString(fmt.Sprintf("$%d, ", i))
	}
	s.WriteString(fmt.Sprintf("$%d", placeholdersAmount))
	return s.String()
}
