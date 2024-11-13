package entities

type SaleOrderProduct struct {
	Id            int     `json:"id"`
	SaleOrderId   int     `json:"saleOrderId"`
	ProductId     int     `json:"productId"`
	DistributorId int     `json:"distributorId"`
	Quantity      int     `json:"quantity"`
	UnitPrice     float64 `json:"unitPrice"`
}

func NewSaleOrderProduct(id int, saleOrderId int, productId int, distributorId int, quantity int, unitPrice float64) *SaleOrderProduct {
	return &SaleOrderProduct{
		Id:            id,
		SaleOrderId:   saleOrderId,
		ProductId:     productId,
		DistributorId: distributorId,
		Quantity:      quantity,
		UnitPrice:     unitPrice,
	}
}

func (*SaleOrderProduct) GetTableName() string {
	return "sales_orders_products"
}

func (*SaleOrderProduct) GetTableColumns() []string {
	return []string{"sale_order_id", "product_id", "distributor_id", "quantity", "unit_price"}
}