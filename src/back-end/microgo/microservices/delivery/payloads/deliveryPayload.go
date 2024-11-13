package payloads

type DeliveryPayload struct {
	Cep       string `json:"cep" 		validate:"required"`
	ProductId int    `json:"productId" 	validate:"required"`
	Quantity  int    `json:"quantity" 	validate:"required"`
}

func NewDeliveryPayload(cep string, productId int, quantity int) *DeliveryPayload {
	return &DeliveryPayload{
		Cep:       cep,
		ProductId: productId,
		Quantity:  quantity,
	}
}
