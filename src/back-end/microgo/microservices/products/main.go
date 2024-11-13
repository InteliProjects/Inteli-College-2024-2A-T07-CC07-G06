package main

import (
	"github.com/patrickmn/go-cache"
	"micronsync/products/config"
	dbProducts "micronsync/products/database"
	"micronsync/products/errorhandling"
	"micronsync/products/handlers"
	"micronsync/products/middleware"
	"os"
	"time"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
)

func main() {
	db, dbError := dbProducts.OpenConnection(os.Getenv("DB_CONN_STRING"))
	errorhandling.StopIfError(dbError)

	defer db.Close()

	app := fiber.New()

	caching := cache.New(1*time.Minute, 5*time.Minute)

	app.Get("/", func(c *fiber.Ctx) error {
		return c.SendString("Hello, products service!")
	})

	app.Use(cors.New(config.CORSConfig()))

	app.Get("/products", middleware.CacheMiddleware(caching), handlers.GetAllProductsHandler)
	app.Get("/products/:id<int>", middleware.CacheMiddleware(caching), handlers.GetProductByIdHandler)
	app.Post("/products/ids", handlers.GetProductsByIdsHandler)

	listenError := app.Listen(":8091")
	errorhandling.StopIfError(listenError)
}
