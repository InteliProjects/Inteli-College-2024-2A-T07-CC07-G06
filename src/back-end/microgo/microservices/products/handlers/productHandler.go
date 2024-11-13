package handlers

import (
	"micronsync/products/database"
	"micronsync/products/errorhandling"
	"micronsync/products/payloads"

	"github.com/gofiber/fiber/v2"
)

func GetAllProductsHandler(ctx *fiber.Ctx) error {
	result, dbError := database.GetAllProducts()
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(result)
}

func GetProductByIdHandler(ctx *fiber.Ctx) error {
	id, paramError := ctx.ParamsInt("id")
	if paramError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetProductByIDHandler: Invalid ID %d.\n%v", id, paramError))
	}

	result, dbError := database.GetProductById(id)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(result)
}

func GetProductsByIdsHandler(ctx *fiber.Ctx) error {
	var body payloads.ProductsIdsPayload
	if parseError := ctx.BodyParser(&body); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetProductsByIdsHandler: Invalid JSON body.\n%v", parseError))
	}

	if len(body.ProductIds) == 0 {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetProductsByIdsHandler: No IDs provided."))
	}

	products, dbError := database.GetProductsByIds(body.ProductIds)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(products)
}
