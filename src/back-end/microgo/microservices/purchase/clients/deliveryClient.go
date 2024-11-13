package clients

import (
	"bytes"
	"encoding/json"
	"micronsync/purchases/errorhandling"
	"micronsync/purchases/payloads"
	"micronsync/purchases/responses"
	"net/http"
)

func FetchDeliveryDetails(payload *payloads.DeliveryPayload) (*responses.DeliveryResponse, error) {
	body, marshalError := json.Marshal(payload)
	if marshalError != nil {
		return nil, errorhandling.WriteError("FetchDeliveryDetails: Failed to Marshal DeliveryPayload type body.\n%v", marshalError)
	}

	resp, httpError := http.Post("http://delivery-service:8094/delivery-details", "application/json", bytes.NewBuffer(body))
	if httpError != nil {
		return nil, errorhandling.WriteError("FetchDeliveryDetails: Failed to get delivery details from HTTP request.\n%v", httpError)
	}
	defer resp.Body.Close()

	var delivery responses.DeliveryResponse
	if decodingError := json.NewDecoder(resp.Body).Decode(&delivery); decodingError != nil {
		return nil, errorhandling.WriteError("FetchDeliveryDetails: Failed to decode HTTP response into DeliveryResponse type struct.\n%v", decodingError)
	}

	return &delivery, nil
}
