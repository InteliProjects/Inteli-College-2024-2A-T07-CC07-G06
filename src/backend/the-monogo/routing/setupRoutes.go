// # Package routing
//
// This package provides functions to configurate the API routes.
package routing

import (
	"nsync/handlers"
	"nsync/middleware"
	"time"


	"github.com/gofiber/fiber/v2"
	"github.com/patrickmn/go-cache"
)

// Configures all of the API routes based on the Router parameter
func SetupRoutes(apiRouter fiber.Router) {
	caching := cache.New(1*time.Minute, 5*time.Minute)
	apiRouter.Get("/products", middleware.CacheMiddleware(caching), handlers.GetAllProductsHandler)
	apiRouter.Post("/csv-processor/populate-all", handlers.PopulateAllViaCsvHandler)
	apiRouter.Post("/cep/calculate-days", handlers.GetDeliveryDatesHandler)
	apiRouter.Post("/purchases", handlers.ProcessPurchaseHandler)
	apiRouter.Post("/products/ids", handlers.GetProductsByIdsHandler)
}