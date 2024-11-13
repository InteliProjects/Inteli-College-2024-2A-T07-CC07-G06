package main

import (
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"github.com/patrickmn/go-cache"
	"micronsync/products-distributors/config"
	dbProductsDistributors "micronsync/products-distributors/database"
	errorhandling "micronsync/products-distributors/errors"
	"micronsync/products-distributors/handlers"
	"micronsync/products-distributors/middleware"
	"os"
	"time"
)

func main() {
	db, dbError := dbProductsDistributors.OpenConnection(os.Getenv("DB_CONN_STRING"))
	errorhandling.StopIfError(dbError)

	defer db.Close()

	app := fiber.New()

	caching := cache.New(1*time.Minute, 5*time.Minute)

	app.Get("/", func(c *fiber.Ctx) error {
		return c.SendString("Hello, products-distributors service!")
	})

	app.Use(cors.New(config.CORSConfig()))

	app.Get("/products-distributors", middleware.CacheMiddleware(caching), handlers.GetAllProductsDistributorsHandler)
	app.Get("/products-distributors/:id<int>", middleware.CacheMiddleware(caching), handlers.GetProductDistributorByIdHandler)
	app.Put("/products-distributors/:id<int>", handlers.UpdateProductDistributorHandler)

	listenError := app.Listen(":8093")
	errorhandling.StopIfError(listenError)
}
