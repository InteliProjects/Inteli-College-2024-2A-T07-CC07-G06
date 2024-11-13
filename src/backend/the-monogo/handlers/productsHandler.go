package handlers

import (
	"nsync/payloads"
	dbUtils "nsync/utils/database"
	errorhandling "nsync/utils/error-handling"

	"github.com/gofiber/fiber/v2"
)

// Responsible for handling the request to get a list of all products from the database
func GetAllProductsHandler(ctx *fiber.Ctx) error {
	result, dbError := dbUtils.GetAllProducts()
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}
	
	return ctx.Status(200).JSON(result)
}

// Responsible for handling the request to get a list of products from the database by a list os products ids
func GetProductsByIdsHandler(ctx *fiber.Ctx) error {
	var ids payloads.ProductsIdsPayload
	if parseError := ctx.BodyParser(&ids); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetProductsByIds: Failed to parse request body.\n%v", parseError))
	}

	result, serviceError := dbUtils.GetProductsByIds(ids.Ids)
	if serviceError != nil {
		return errorhandling.ApiError(ctx, serviceError)
	}

	return ctx.Status(200).JSON(result)
}