package payloads

type DeliveryPayload struct {
	Cep       string `json:"cep"`
	ProductId int    `json:"productId"`
	Quantity  int    `json:"quantity"`
}

func NewDeliveryPayload(cep string, productId int, quantity int) *DeliveryPayload {
	return &DeliveryPayload{
		Cep:       cep,
		ProductId: productId,
		Quantity:  quantity,
	}
}