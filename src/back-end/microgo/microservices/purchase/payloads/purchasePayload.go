package payloads

type PurchasePayload struct {
	Products 	[]*PurchasePayloadProduct 	`json:"products" 	validate:"required"`
	CustomerCep string 						`json:"cep" 		validade:"required"`
}

type PurchasePayloadProduct struct {
	ProductId 	int `json:"productId" 	validate:"required"`
	Quantity	int `json:"quantity"	validate:"required"`
}

func NewPurchasePayload(products []*PurchasePayloadProduct, cep string) *PurchasePayload {
	return &PurchasePayload {
		Products	: products,
		CustomerCep	: cep,
	}
}

func NewPurchasePayloadProduct(id int, quantity int) *PurchasePayloadProduct {
	return &PurchasePayloadProduct {
		ProductId	: id,
		Quantity	: quantity,
	}
}