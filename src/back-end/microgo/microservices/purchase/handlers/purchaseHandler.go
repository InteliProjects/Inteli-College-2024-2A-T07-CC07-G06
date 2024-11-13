package handlers

import (
	"micronsync/purchases/clients"
	"micronsync/purchases/errorhandling"
	"micronsync/purchases/payloads"
	"micronsync/purchases/responses"
	"time"

	"github.com/gofiber/fiber/v2"
)

func ProcessPurchaseHandler(ctx *fiber.Ctx) error {
	var purchasePayload payloads.PurchasePayload
	if parseError := ctx.BodyParser(&purchasePayload); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("ProcessPurchaseHandler: Failed to parse request body.\n%v", parseError))
	}

	productsChannel := make(chan *clients.ProductsDetailsResult)
	go clients.FetchPurchaseProductsDetailsRoutine(purchasePayload.Products, productsChannel)

	var distributors []*responses.DeliveryDistributor
	var productsDistributors []*responses.DeliveryProductDistributor
	firstDay := time.Now()
	var lastDay time.Time
	for _, product := range purchasePayload.Products {
		deliveryDetails, deliveryError := clients.FetchDeliveryDetails(payloads.NewDeliveryPayload(purchasePayload.CustomerCep, product.ProductId, product.Quantity))
		if deliveryError != nil {
			return errorhandling.ApiError(ctx, deliveryError)
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

	saleOrderId, saleOrderError := clients.SendSaleOrder(payloads.NewSaleOrderPayload(purchasePayload.CustomerCep, productsResult.Total, time.Now(), firstDay, lastDay))
	if saleOrderError != nil {
		return errorhandling.ApiError(ctx, saleOrderError)
	}

	errorChannel := make(chan error)
	for i, d := range distributors {
		p := purchasePayload.Products[i]
		pd := productsDistributors[i]

		go clients.SendSaleOrderProductRoutine(payloads.NewSaleOrderProductPayload(saleOrderId, p.ProductId, d.Id, p.Quantity, productsResult.IdToPrice[p.ProductId]), errorChannel)
		go clients.UpdateProductDistributorRoutine(pd.Id, payloads.NewProductDistributorPayload(d.Id, p.ProductId, pd.AmountAvailable - p.Quantity, pd.AmountReserved), errorChannel)

		sopError, pdError := <-errorChannel, <-errorChannel
		errorHappened, err := errorhandling.AnyNotNil(sopError, pdError)
		if errorHappened {
			return errorhandling.ApiError(ctx, err)
		}
	}

	return ctx.Status(200).JSON(fiber.Map{
		"message":"successfully processed purchase",
	})
}