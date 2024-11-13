package entities

type Distributor struct {
	Id   int    `json:"id"`
	Name string `json:"name"`
	Cep  string `json:"cep"`
}