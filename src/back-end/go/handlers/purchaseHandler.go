package handlers

import (
	"nsync/entities"
	"nsync/payloads"
	"nsync/services"
	dbUtils "nsync/utils/database"
	errorhandling "nsync/utils/error-handling"
	"time"

	"github.com/gofiber/fiber/v2"
)

// Responsible for handling the request to purchase products
func ProcessPurchaseHandler(ctx *fiber.Ctx) error {
	var purchasePayload payloads.PurchasePayload
	if parseError := ctx.BodyParser(&purchasePayload); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("ProcessPurchaseHandler: Failed to parse request body.\n%v", parseError))
	}

	var distributors []*entities.Distributor
	var productsDistributors []*entities.ProductDistributor
	var productsIds []int
	firstDay := time.Now()
	var lastDay time.Time
	for _, product := range purchasePayload.Products {
		deliveryPayload := payloads.NewDeliveryPayload(purchasePayload.CustomerCep, product.ProductId, product.Quantity)
		deliveryDetails, serviceError := services.GetDeliveryDetails(deliveryPayload)
		if serviceError != nil {
			return errorhandling.ApiError(ctx, serviceError)
		}

		distributors = append(distributors, deliveryDetails.Distributor)
		productsDistributors = append(productsDistributors, deliveryDetails.ProductDistributor)
		productsIds = append(productsIds, product.ProductId)

		if deliveryDetails.FirstDay.Before(firstDay) {
			firstDay = deliveryDetails.FirstDay
		}
		
		if deliveryDetails.LastDay.After(lastDay) {
			lastDay = deliveryDetails.LastDay
		}
	}

	products, serviceError := services.GetProductsByIds(productsIds)
	if serviceError != nil {
		return errorhandling.ApiError(ctx, serviceError)
	}

	idToPrice := make(map[int]float64)
	var total float64
	for _, p := range products {
		for _, purchaseProduct := range purchasePayload.Products {
			if p.Id == purchaseProduct.ProductId {
				total += p.Price * float64(purchaseProduct.Quantity)
				idToPrice[p.Id] = p.Price
				break
			}
		}
	}

	saleOrderId, soDBError := dbUtils.Create[*entities.SaleOrder](payloads.NewSaleOrderPayload(purchasePayload.CustomerCep, total, time.Now(), firstDay, lastDay))
	if soDBError != nil {
		return errorhandling.ApiError(ctx, soDBError)
	}

	for i, pd := range productsDistributors {
		p := purchasePayload.Products[i]
		d := distributors[i]
		_, sopDBError := dbUtils.Create[*entities.SaleOrderProduct](payloads.NewSaleOrderProductPayload(saleOrderId, p.ProductId, d.Id, p.Quantity, idToPrice[p.ProductId]))
		if sopDBError != nil {
			return errorhandling.ApiError(ctx, sopDBError)
		}
		
		pdDBError := dbUtils.Update[*entities.ProductDistributor](pd.Id, payloads.NewProductDistributorPayload(d.Id, p.ProductId, pd.AmountAvailable - p.Quantity, pd.AmountReserved))
		if pdDBError != nil {
			return errorhandling.ApiError(ctx, pdDBError)
		}
	}

	return ctx.Status(202).JSON(fiber.Map{
		"message":"successfully processed purchase",
	})
}