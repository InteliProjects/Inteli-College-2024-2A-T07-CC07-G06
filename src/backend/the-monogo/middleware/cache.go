package middleware

import (
	"github.com/gofiber/fiber/v2"
	"github.com/patrickmn/go-cache"
	"time"
)

func CacheMiddleware(cache *cache.Cache) fiber.Handler {
	return func(c *fiber.Ctx) error {
		if c.Method() != "GET" {
			return c.Next()
		}

		cacheKey := c.OriginalURL()

		if cached, found := cache.Get(cacheKey); found {
			c.Response().Header.Set("Cache-Status", "HIT")
			return c.JSON(cached)
		}

		c.Set("Cache-Status", "MISS")

		if err := c.Next(); err != nil {
			return err
		}

		body := c.Response().Body()

		cache.Set(cacheKey, body, 1*time.Minute)

		return nil
	}
}