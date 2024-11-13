package main

import (
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"github.com/patrickmn/go-cache"
	"micronsync/sales-orders/config"
	"micronsync/sales-orders/database"
	"micronsync/sales-orders/errorhandling"
	"micronsync/sales-orders/handlers"
	"micronsync/sales-orders/middleware"
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
		return c.SendString("Hello, sales orders service!")
	})

	app.Use(cors.New(config.CORSConfig()))

	app.Get("/sales-orders", middleware.CacheMiddleware(caching), handlers.GetAllSalesOrdersHandler)
	app.Get("/sales-orders/:id<int>", middleware.CacheMiddleware(caching), handlers.GetSaleOrderByIdHandler)
	app.Post("/sales-orders", handlers.CreateSaleOrderHandler)

	listenError := app.Listen(":8095")
	errorhandling.StopIfError(listenError)
}
