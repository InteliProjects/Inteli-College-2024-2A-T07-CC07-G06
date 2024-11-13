package main

import (
	"nsync/routing"
	dbUtils "nsync/utils/database"
	errorhandling "nsync/utils/error-handling"
	"os"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	//cache "github.com/patrickmn/go-cache"
)

func main() {
	db, dbError := dbUtils.OpenConnection(os.Getenv("DB_CONN_STRING"))
	errorhandling.StopIfError(dbError)
	defer db.Close()

	app := fiber.New()

	app.Use(cors.New(cors.Config{
		AllowOrigins:  os.Getenv("CORS_ORIGINS"),
		AllowMethods:  os.Getenv("CORS_METHODS"),
		AllowHeaders:  os.Getenv("CORS_HEADERS"),
		ExposeHeaders: os.Getenv("CORS_EXPOSE_HEADERS"),
	}))

	api := app.Group("/")

	routing.SetupRoutes(api)

	listenError := app.Listen(":8080")
	errorhandling.StopIfError(listenError)
}
