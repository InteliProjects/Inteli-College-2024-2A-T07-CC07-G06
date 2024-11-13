package entities

type Distributor struct {
	Id   int    `json:"id"`
	Name string `json:"name"`
	Cep  string `json:"cep"`
}

func NewDistributor(id int, name string, cep string) *Distributor {
	return &Distributor{
		Id:   id,
		Name: name,
		Cep:  cep,
	}
}

func (*Distributor) GetTableName() string {
	return "distributors"
}

func (*Distributor) GetTableColumns() []string {
	return []string{"name", "cep"}
}
