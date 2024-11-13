package clients

import (
	"bytes"
	"encoding/json"
	"micronsync/purchases/errorhandling"
	"micronsync/purchases/payloads"
	"net/http"
)

func SendSaleOrderProductRoutine(payload *payloads.SaleOrderProductPayload, errorChannel chan error) {
	body, marshalError := json.Marshal(payload)
	if marshalError != nil {
		errorChannel <- errorhandling.WriteError("SendSaleOrderProduct: Failed to Marshal SaleOrderProductPayload type body.\n%v", marshalError)
		return
	}

	res, httpError := http.Post("http://sales-orders-products-service:8096/sales-orders-products", "application/json", bytes.NewBuffer(body))
	if httpError != nil {
		errorChannel <- errorhandling.WriteError("SendSaleOrderProduct: Failed to get delivery details from HTTP request.\n%v", httpError)
		return
	}
	defer res.Body.Close()

	errorChannel <- nil
}
