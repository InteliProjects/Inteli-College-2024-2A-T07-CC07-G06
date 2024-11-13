// # Package routing
//
// This package provides functions to configurate the API routes.
package routing

import (
	"nsync/entities"
	"nsync/handlers"
	"nsync/payloads"
	"strings"

	"github.com/gofiber/fiber/v2"
)

// Configures all of the API routes based on the Router parameter
func SetupRoutes(apiRouter fiber.Router) {
	setupGenericHandlers[*entities.Distributor, *payloads.DistributorPayload](apiRouter)
	setupGenericHandlers[*entities.Product, *payloads.ProductPayload](apiRouter)
	setupGenericHandlers[*entities.ProductDistributor, *payloads.ProductDistributorPayload](apiRouter)
	setupGenericHandlers[*entities.SaleOrder, *payloads.SaleOrderPayload](apiRouter)
	setupGenericHandlers[*entities.SaleOrderProduct, *payloads.SaleOrderProductPayload](apiRouter)

	apiRouter.Post("/csv-processor/populate-all", handlers.PopulateAllViaCsvHandler)
	apiRouter.Post("/cep/calculate-days", handlers.GetDeliveryDatesHandler)
	apiRouter.Post("/purchases", handlers.ProcessPurchaseHandler)
	apiRouter.Post("/products/ids", handlers.GetProductsByIdsHandler)
}

// Configures generic handlers to deal with CRUD operations for the application entities (present on the Package entities)
func setupGenericHandlers[E entities.Entity, P payloads.EntityPayload](apiRouter fiber.Router) {
	var entity E
	basePath := "/" + strings.ReplaceAll(entity.GetTableName(), "_", "-")

	// Get All
	apiRouter.Get(basePath, handlers.GetAllHandler[E])

	// Get by id
	apiRouter.Get(basePath+"/:id<int>", handlers.GetByIdHandler[E])

	// Create
	apiRouter.Post(basePath, handlers.CreateHandler[E, P])

	// Create via CSV upload
	apiRouter.Post("/csv-processor" + basePath, handlers.CreateViaCsvHandler[E, P])

	// Update
	apiRouter.Put(basePath+"/:id<int>", handlers.UpdateHandler[E, P])

	// Delete
	apiRouter.Delete(basePath+"/:id<int>", handlers.DeleteHandler[E])
}
