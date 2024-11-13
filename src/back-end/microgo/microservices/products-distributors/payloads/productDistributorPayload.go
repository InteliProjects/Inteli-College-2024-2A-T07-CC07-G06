package payloads

type ProductDistributorPayload struct {
	DistributorId   int `json:"distributorId" 	validate:"required"`
	ProductId       int `json:"productId" 		validate:"required"`
	AmountAvailable int `json:"amountAvailable" validate:"required"`
	AmountReserved  int `json:"amountReserved"`
}

func NewProductDistributorPayload(distributorId int, productId int, amountAvailable int, amountReserved int) *ProductDistributorPayload {
	return &ProductDistributorPayload{
		DistributorId:   distributorId,
		ProductId:       productId,
		AmountAvailable: amountAvailable,
		AmountReserved:  amountReserved,
	}
}