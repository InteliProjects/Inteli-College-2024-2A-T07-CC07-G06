package handlers

import (
	"micronsync/sales-orders-products/database"
	"micronsync/sales-orders-products/errorhandling"
	"micronsync/sales-orders-products/payloads"

	"github.com/gofiber/fiber/v2"
)

func GetAllSalesOrdersProductsHandler(ctx *fiber.Ctx) error {
	result, dbError := database.GetAllSalesOrdersProducts()
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(result)
}

func GetSaleOrderProductByIdHandler(ctx *fiber.Ctx) error {
	id, paramError := ctx.ParamsInt("id")
	if paramError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetSaleOrderProductByIDHandler: Invalid ID %d.\n%v", id, paramError))
	}

	result, dbError := database.GetSaleOrderProductById(id)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(result)
}

func CreateSaleOrderProductHandler(ctx *fiber.Ctx) error {
	sop := &payloads.SaleOrderProductPayload{}
	if parseError := ctx.BodyParser(sop); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("CreateSaleOrderProductHandler: Failed to parse the incoming data into type SaleOrderProductPayload struct.\n%v", parseError))
	}

	createdId, dbError := database.CreateSaleOrderProduct(sop)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(fiber.Map{
		"createdId": createdId,
	})
}