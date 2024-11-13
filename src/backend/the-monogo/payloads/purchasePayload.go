package payloads

// # Purchase Payload
//
// The Purchase Payload type is defined as a struct that can store all the parsed data from a JSON request referring to processing purchases.
// It has its own constructor defined as follows:
//
// func NewPurchasePayload(products []PurchasePayloadProduct, cep string, total float64) *PurchasePayload
type PurchasePayload struct {
	Products 	[]*PurchasePayloadProduct 	`json:"products" 	validate:"required"`
	CustomerCep string 						`json:"cep" 		validade:"required"`
}

// # Purchase Payload Product
//
// The Purchase Payload Product type is defined as a struct that can store all the parsed data from a JSON request referring to the products present in a purchase request.
// It has its own constructor defined as follows:
//
// func NewPurchasePayloadProduct(id int, quantity int) *PurchasePayloadProduct
type PurchasePayloadProduct struct {
	ProductId 	int `json:"productId" 	validate:"required"`
	Quantity	int `json:"quantity"	validate:"required"`
}

// Returns a pointer to a new Purchase Payload type struct containing the data from the parameters.
func NewPurchasePayload(products []*PurchasePayloadProduct, cep string, total float64) *PurchasePayload {
	return &PurchasePayload {
		Products	: products,
		CustomerCep	: cep,
	}
}

// Returns a pointer to a new Purchase Payload Product type struct containing the data from the parameters.
func NewPurchasePayloadProduct(id int, quantity int) *PurchasePayloadProduct {
	return &PurchasePayloadProduct {
		ProductId	: id,
		Quantity	: quantity,
	}
}