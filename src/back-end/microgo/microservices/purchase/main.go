package main

import (
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"micronsync/purchases/config"
	"micronsync/purchases/errorhandling"
	"micronsync/purchases/handlers"
)

func main() {
	app := fiber.New()

	app.Get("/", func(c *fiber.Ctx) error {
		return c.SendString("Hello, purchase service!")
	})

	app.Use(cors.New(config.CORSConfig()))

	app.Post("/purchases", handlers.ProcessPurchaseHandler)

	listenError := app.Listen(":8097")
	errorhandling.StopIfError(listenError)
}
