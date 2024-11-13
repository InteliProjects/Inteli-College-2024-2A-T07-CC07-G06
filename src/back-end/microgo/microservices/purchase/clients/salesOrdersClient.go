package clients

import (
	"bytes"
	"encoding/json"
	"micronsync/purchases/errorhandling"
	"micronsync/purchases/payloads"
	"micronsync/purchases/responses"
	"net/http"
)

func SendSaleOrder(payload *payloads.SaleOrderPayload) (int, error) {
	body, marshalError := json.Marshal(payload)
	if marshalError != nil {
		return 0, errorhandling.WriteError("SendSaleOrder: Failed to Marshal SaleOrderPayload type body.\n%v", marshalError)
	}

	resp, httpError := http.Post("http://sales-orders-service:8095/sales-orders", "application/json", bytes.NewBuffer(body))
	if httpError != nil {
		return 0, errorhandling.WriteError("SendSaleOrder: Failed to get delivery details from HTTP request.\n%v", httpError)
	}
	defer resp.Body.Close()

	var result responses.SendResultResponse
	if decodingError := json.NewDecoder(resp.Body).Decode(&result); decodingError != nil {
		return 0, errorhandling.WriteError("SendSaleOrder: Failed to decode HTTP response into SendResultResponse type struct.\n%v", decodingError)
	}

	return result.CreatedId, nil
}
