package main

import (
	"micronsync/delivery/config"
	"micronsync/delivery/database"
	"micronsync/delivery/errorhandling"
	"micronsync/delivery/handlers"
	"os"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
)

func main() {
	db, dbError := database.OpenConnection(os.Getenv("DB_CONN_STRING"))
	errorhandling.StopIfError(dbError)
	defer db.Close()

	app := fiber.New()

	app.Get("/", func(c *fiber.Ctx) error {
		return c.SendString("Hello, delivery service!")
	})

	app.Use(cors.New(config.CORSConfig()))

	apiRouter := app.Group("/")
	apiRouter.Post("/calculate-days", handlers.GetDeliveryDatesHandler)
	apiRouter.Post("/delivery-details", handlers.GetDeliveryDetailsHandler)

	listenError := app.Listen(":8094")
	errorhandling.StopIfError(listenError)
}
