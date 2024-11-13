package payloads

// # Delivery Payload
//
// The Delivery Payload type is defined as a struct that can store all the parsed data from a JSON request referring to delivery informations.
// It has its own constructor defined as follows:
//
// func NewDeliveryPayload(cep string, productId int, quantity int) *DeliveryPayload
type DeliveryPayload struct {
	Cep 		string 	`json:"cep" 		validate:"required"`
	ProductId 	int 	`json:"productId 	validate:"required"`
	Quantity 	int 	`json:"quantity" 	validate:"required"`
}

// Returns a pointer to a new Delivery Payload type struct containing the data from the parameters.
func NewDeliveryPayload(cep string, productId int, quantity int) *DeliveryPayload {
	return &DeliveryPayload {
		Cep			: cep,
		ProductId	: productId,
		Quantity	: quantity,
	}
}