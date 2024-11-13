package handlers

import (
	"nsync/payloads"
	"nsync/services"
	errorhandling "nsync/utils/error-handling"

	"github.com/gofiber/fiber/v2"
)

// Responsible for handling the request to get a list of products from the database by a list os products ids
func GetProductsByIdsHandler(ctx *fiber.Ctx) error {
	var ids payloads.ProductsIdsPayload
	if parseError := ctx.BodyParser(&ids); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetProductsByIds: Failed to parse request body.\n%v", parseError))
	}

	result, serviceError := services.GetProductsByIds(ids.Ids)
	if serviceError != nil {
		return errorhandling.ApiError(ctx, serviceError)
	}

	return ctx.Status(200).JSON(result)
}