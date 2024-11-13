package main

import (
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"github.com/patrickmn/go-cache"
	"micronsync/sales-orders-products/config"
	"micronsync/sales-orders-products/database"
	"micronsync/sales-orders-products/errorhandling"
	"micronsync/sales-orders-products/handlers"
	"micronsync/sales-orders-products/middleware"
	"os"
	"time"
)

func main() {
	db, dbError := database.OpenConnection(os.Getenv("DB_CONN_STRING"))
	errorhandling.StopIfError(dbError)
	defer db.Close()

	app := fiber.New()

	caching := cache.New(1*time.Minute, 5*time.Minute)

	app.Get("/", func(c *fiber.Ctx) error {
		return c.SendString("Hello, sales orders products service!")
	})

	app.Use(cors.New(config.CORSConfig()))

	app.Get("/sales-orders-products", middleware.CacheMiddleware(caching), handlers.GetAllSalesOrdersProductsHandler)
	app.Get("/sales-orders-products/:id<int>", middleware.CacheMiddleware(caching), handlers.GetSaleOrderProductByIdHandler)
	app.Post("/sales-orders-products", handlers.CreateSaleOrderProductHandler)

	listenError := app.Listen(":8096")
	errorhandling.StopIfError(listenError)
}
