package entities

type ProductDistributor struct {
	Id              int `json:"id"`
	DistributorId   int `json:"distributorId"`
	ProductId       int `json:"productId"`
	AmountAvailable int `json:"amountAvailable"`
	AmountReserved  int `json:"amountReserved"`
}