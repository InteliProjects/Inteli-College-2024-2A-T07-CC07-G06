package clients

import (
	"bytes"
	"encoding/json"
	"micronsync/purchases/errorhandling"
	"micronsync/purchases/payloads"
	"micronsync/purchases/responses"
	"net/http"
)

type ProductsDetailsResult struct {
	Products 	[]*responses.ProductsResponse
	IdToPrice	map[int]float64
	Total		float64
	Error 		error
}

func FetchPurchaseProductsDetailsRoutine(purchaseProducts []*payloads.PurchasePayloadProduct, resultChannel chan *ProductsDetailsResult) {
	var productsPayload payloads.ProductsPayload
	for _, product := range purchaseProducts {
		productsPayload.ProductsIds = append(productsPayload.ProductsIds, product.ProductId)
	}

	body, marshalError := json.Marshal(&productsPayload)
	if marshalError != nil {
		resultChannel <- &ProductsDetailsResult{nil, nil, 0, errorhandling.WriteError("FetchPurchaseProductsDetailsRoutine: Failed to Marshal ProductsPayload type body.\n%v", marshalError)}
		return
	}

	resp, httpError := http.Post("http://products-service:8091/products/ids", "application/json", bytes.NewBuffer(body))
	if httpError != nil {
		resultChannel <- &ProductsDetailsResult{nil, nil, 0, errorhandling.WriteError("FetchPurchaseProductsDetailsRoutine: Failed to get products from HTTP request.\n%v", httpError)}
		return
	}
	defer resp.Body.Close()

	dec := json.NewDecoder(resp.Body)

	_, tokenError := dec.Token()
	if tokenError != nil {
		resultChannel <- &ProductsDetailsResult{nil, nil, 0, errorhandling.WriteError("FetchPurchaseProductsDetailsRoutine: Failed to decode JSON token.\n%v", tokenError)}
		return
	}

	result := &ProductsDetailsResult{IdToPrice: make(map[int]float64)}
	for dec.More() {
		var p responses.ProductsResponse
		if decodingError := dec.Decode(&p); decodingError != nil {
			resultChannel <- &ProductsDetailsResult{nil, nil, 0, errorhandling.WriteError("FetchPurchaseProductsDetailsRoutine: Failed to decode HTTP response into ProductsResponse type struct.\n%v", decodingError)}
			return
		}
		result.Products = append(result.Products, &p)

		for _, purchaseProduct := range purchaseProducts {
			if purchaseProduct.ProductId == p.Id {
				result.IdToPrice[purchaseProduct.ProductId] = p.Price
				result.Total += float64(purchaseProduct.Quantity) * p.Price
				break 
			}
		}
	}

	resultChannel <- result
}
