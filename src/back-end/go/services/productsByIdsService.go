package services

import (
	"fmt"
	"nsync/entities"
	dbUtils "nsync/utils/database"
	"strconv"
	"strings"
)

// Returns a slice of type []*entities.Product representing the products that correspond to the ids passed as parameter. 
func GetProductsByIds(ids []int) ([]*entities.Product, error) {
	idsString := make([]string, len(ids))
	for i, id := range ids {
		idsString[i] = strconv.Itoa(id)
	}

	var p entities.Product
	sql := fmt.Sprintf("SELECT id, %s FROM %s WHERE id IN (%s)", strings.Join(p.GetTableColumns(), ", "), p.GetTableName(), strings.Join(idsString, ", "))
	
	result, dbError := dbUtils.QueryEntity[*entities.Product](sql)
	if dbError != nil {
		return nil, dbError
	}

	return result, nil
}