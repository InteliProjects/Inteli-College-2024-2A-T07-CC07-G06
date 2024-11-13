package handlers

import (
	"micronsync/products-distributors/database"
	errorhandling "micronsync/products-distributors/errors"
	"micronsync/products-distributors/payloads"

	"github.com/gofiber/fiber/v2"
)

func GetAllProductsDistributorsHandler(ctx *fiber.Ctx) error {
	result, dbError := database.GetAllProductsDistributors()
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(result)
}

func GetProductDistributorByIdHandler(ctx *fiber.Ctx) error {
	id, paramError := ctx.ParamsInt("id")
	if paramError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetProductDistributorByIDHandler: Invalid ID %d.\n%v", id, paramError))
	}

	result, dbError := database.GetProductDistributorById(id)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(result)
}

func UpdateProductDistributorHandler(ctx *fiber.Ctx) error {
	var payload payloads.ProductDistributorPayload
	if parseError := ctx.BodyParser(&payload); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("UpdateProductDistributorHandler: Failed to parse the incoming data into type ProductDistributor struct.\n%v", parseError))
	}

	id, idError := ctx.ParamsInt("id")
	if idError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("UpdateProductDistributorHandler: Failed to parse url parameter.\n%v", idError))
	}

	dbError := database.UpdateProductDistributor(id, payload)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(fiber.Map{
		"message":"successfully updated",
	})
}