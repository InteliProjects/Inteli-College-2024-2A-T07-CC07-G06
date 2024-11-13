package payloads

type SaleOrderProductPayload struct {
	SaleOrderId   int     `json:"saleOrderId" 	validate:"required"`
	ProductId     int     `json:"productId"		validate:"required"`
	DistributorId int     `json:"distributorId"	validate:"required"`
	Quantity      int     `json:"quantity"		validate:"required"`
	UnitPrice     float64 `json:"unitPrice"		validate:"required"`
}

func NewSaleOrderProductPayload(saleOrderId int, productId int, distributorId int, quantity int, unitPrice float64) *SaleOrderProductPayload {
	return &SaleOrderProductPayload{
		SaleOrderId:   saleOrderId,
		ProductId:     productId,
		DistributorId: distributorId,
		Quantity:      quantity,
		UnitPrice:     unitPrice,
	}
}