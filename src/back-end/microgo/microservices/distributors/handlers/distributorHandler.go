package handlers

import (
	"micronsync/distributors/database"
	"micronsync/distributors/errorhandling"

	"github.com/gofiber/fiber/v2"
)

func GetAllDistributorHandler(ctx *fiber.Ctx) error {
	result, dbError := database.GetAllDistributors()
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(result)
}

func GetDistributorByIdHandler(ctx *fiber.Ctx) error {
	id, paramError := ctx.ParamsInt("id")
	if paramError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetProductByIDHandler: Invalid ID %d.\n%v", id, paramError))
	}

	result, dbError := database.GetDistributorById(id)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(result)
}
