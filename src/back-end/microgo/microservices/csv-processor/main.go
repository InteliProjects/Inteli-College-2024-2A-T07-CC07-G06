package main

import (
	"micronsync/csv-processor/config"
	"micronsync/csv-processor/database"
	"micronsync/csv-processor/errorhandling"
	"micronsync/csv-processor/handlers"
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
		return c.SendString("Hello, csv service!")
	})

	app.Use(cors.New(config.CORSConfig()))

	app.Post("/csv-processor/populate-all", handlers.PopulateAllHandler)

	listenError := app.Listen(":8098")
	errorhandling.StopIfError(listenError)
}
