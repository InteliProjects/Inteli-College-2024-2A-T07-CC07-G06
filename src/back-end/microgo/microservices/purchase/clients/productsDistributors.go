package clients

import (
	"bytes"
	"encoding/json"
	"fmt"
	"micronsync/purchases/errorhandling"
	"micronsync/purchases/payloads"
	"net/http"
)

func UpdateProductDistributorRoutine(id int, payload *payloads.ProductDistributorPayload, errorChannel chan error) {
	body, marshalError := json.Marshal(payload)
	if marshalError != nil {
		errorChannel <- errorhandling.WriteError("UpdateProductDistributor: Failed to Marshal ProductDistributorPayload type body.\n%v", marshalError)
		return
	}

	req, reqError := http.NewRequest(http.MethodPut, fmt.Sprintf("http://products-distributors-service:8093/products-distributors/%d", id), bytes.NewBuffer(body))
	if reqError != nil {
		errorChannel <- errorhandling.WriteError("UpdateProductDistributor: Failed to make HTTP request.\n%v", reqError)
		return
	}

	req.Header.Set("Content-Type", "application/json")
	client := &http.Client{}
	res, httpError := client.Do(req)
	if httpError != nil {
		errorChannel <- errorhandling.WriteError("UpdateProductDistributor: Failed get response from HTTP request.\n%v", httpError)
		return
	}
	defer res.Body.Close()

	errorChannel <- nil
}
