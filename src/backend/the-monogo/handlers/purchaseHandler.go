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

type productsDetailsResult struct {
	Products 	[]*entities.Product
	IdToPrice	map[int]float64
	Total		float64
	Error 		error
}

// Responsible for handling the request to purchase products
func ProcessPurchaseHandler(ctx *fiber.Ctx) error {
	var purchasePayload payloads.PurchasePayload
	if parseError := ctx.BodyParser(&purchasePayload); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("ProcessPurchaseHandler: Failed to parse request body.\n%v", parseError))
	}

	productsChannel := make(chan *productsDetailsResult)
	go getPurchaseProductsDetailsRoutine(purchasePayload.Products, productsChannel)

	var distributors []*entities.Distributor
	var productsDistributors []*entities.ProductDistributor
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

		if deliveryDetails.FirstDay.Before(firstDay) {
			firstDay = deliveryDetails.FirstDay
		}
		
		if deliveryDetails.LastDay.After(lastDay) {
			lastDay = deliveryDetails.LastDay
		}
	}

	productsResult := <-productsChannel
	if productsResult.Error != nil {
		return errorhandling.ApiError(ctx, productsResult.Error)
	}

	saleOrderId, saleOrderError := dbUtils.CreateSaleOrder(payloads.NewSaleOrderPayload(purchasePayload.CustomerCep, productsResult.Total, time.Now(), firstDay, lastDay))
	if saleOrderError != nil {
		return errorhandling.ApiError(ctx, saleOrderError)
	}

	errorChannel := make(chan error)
	for i, d := range distributors {
		p := purchasePayload.Products[i]
		pd := productsDistributors[i]

		go dbUtils.CreateSaleOrderProductRoutine(payloads.NewSaleOrderProductPayload(saleOrderId, p.ProductId, d.Id, p.Quantity, productsResult.IdToPrice[p.ProductId]), errorChannel)
		go dbUtils.UpdateProductDistributorRoutine(pd.Id, payloads.NewProductDistributorPayload(d.Id, p.ProductId, pd.AmountAvailable - p.Quantity, pd.AmountReserved), errorChannel)

		sopError, pdError := <-errorChannel, <-errorChannel
		errorHappened, err := errorhandling.AnyNotNil(sopError, pdError)
		if errorHappened {
			return errorhandling.ApiError(ctx, err)
		}
	}

	return ctx.Status(202).JSON(fiber.Map{
		"message":"successfully processed purchase",
	})
}

func getPurchaseProductsDetailsRoutine(purchaseProducts []*payloads.PurchasePayloadProduct, resultChannel chan *productsDetailsResult) {
	var productsIds []int
	for _, product := range purchaseProducts {
		productsIds = append(productsIds, product.ProductId)
	}

	products, dbError := dbUtils.GetProductsByIds(productsIds)
	if dbError != nil {
		resultChannel <- &productsDetailsResult{nil, nil, 0, dbError}
		return
	}

	result := &productsDetailsResult{IdToPrice: make(map[int]float64)}
	for _, p := range products {
		result.Products = append(result.Products, p)

		for _, purchaseProduct := range purchaseProducts {
			if purchaseProduct.ProductId == p.Id {
				result.IdToPrice[purchaseProduct.ProductId] = p.Price
				result.Total += float64(purchaseProduct.Quantity) * p.Price
				break 
			}
		}
	}

	resultChannel <- result
}