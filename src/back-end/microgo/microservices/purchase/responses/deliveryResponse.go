package responses

import "time"

type DeliveryResponse struct {
	FirstDay			time.Time 					`json:"firstDay"`
	LastDay 			time.Time 					`json:"lastDay"`
	Distributor        *DeliveryDistributor			`json:"distributor"`
	ProductDistributor *DeliveryProductDistributor	`json:"productDistributor"`
}

type DeliveryDistributor struct {
	Id   int    `json:"id"`
	Name string `json:"name"`
	Cep  string `json:"cep"`
}

type DeliveryProductDistributor struct {
	Id              int `json:"id"`
	DistributorId   int `json:"distributorId"`
	ProductId       int `json:"productId"`
	AmountAvailable int `json:"amountAvailable"`
	AmountReserved  int `json:"amountReserved"`
}