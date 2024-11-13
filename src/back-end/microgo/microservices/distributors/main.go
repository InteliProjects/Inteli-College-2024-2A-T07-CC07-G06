package main

import (
	"github.com/patrickmn/go-cache"
	"micronsync/distributors/config"
	dbDistributors "micronsync/distributors/database"
	"micronsync/distributors/errorhandling"
	handlers "micronsync/distributors/handlers"
	"micronsync/distributors/middleware"
	"os"
	"time"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
)

func main() {
	db, dbError := dbDistributors.OpenConnection(os.Getenv("DB_CONN_STRING"))
	errorhandling.StopIfError(dbError)
	defer db.Close()

	app := fiber.New()

	caching := cache.New(1*time.Minute, 5*time.Minute)

	app.Get("/", func(c *fiber.Ctx) error {
		return c.SendString("Hello, distributors service!")
	})

	app.Use(cors.New(config.CORSConfig()))

	app.Get("/distributors", middleware.CacheMiddleware(caching), handlers.GetAllDistributorHandler)
	app.Get("/distributors/:id<int>", middleware.CacheMiddleware(caching), handlers.GetDistributorByIdHandler)

	listenError := app.Listen(":8092")
	errorhandling.StopIfError(listenError)
}
