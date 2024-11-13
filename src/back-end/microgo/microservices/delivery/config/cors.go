package config

import (
	"os"

	"github.com/gofiber/fiber/v2/middleware/cors"
)

func CORSConfig() cors.Config {
	return cors.Config{
		AllowOrigins:  os.Getenv("CORS_ORIGINS"),
		AllowMethods:  os.Getenv("CORS_METHODS"),
		AllowHeaders:  os.Getenv("CORS_HEADERS"),
		ExposeHeaders: os.Getenv("CORS_EXPOSE_HEADERS"),
	}
}
