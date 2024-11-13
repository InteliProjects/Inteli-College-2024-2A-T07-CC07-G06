package entities

type ProductDistributor struct {
	Id              int `json:"id"`
	DistributorId   int `json:"distributorId"`
	ProductId       int `json:"productId"`
	AmountAvailable int `json:"amountAvailable"`
	AmountReserved  int `json:"amountReserved"`
}

func NewProductDistributor(distributorId int, productId int, amountAvailable int, amountReserved int) *ProductDistributor {
	return &ProductDistributor{
		DistributorId:   distributorId,
		ProductId:       productId,
		AmountAvailable: amountAvailable,
		AmountReserved:  amountReserved,
	}
}

func (*ProductDistributor) GetTableName() string {
	return "products_distributors"
}

func (*ProductDistributor) GetTableColumns() []string {
	return []string{"distributor_id", "product_id", "quantity_available", "quantity_reserved"}
}