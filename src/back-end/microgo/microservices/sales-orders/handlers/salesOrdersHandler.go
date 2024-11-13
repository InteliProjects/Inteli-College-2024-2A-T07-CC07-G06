package handlers

import (
	"micronsync/sales-orders/database"
	"micronsync/sales-orders/errorhandling"
	"micronsync/sales-orders/payloads"

	"github.com/gofiber/fiber/v2"
)

func GetAllSalesOrdersHandler(ctx *fiber.Ctx) error {
	result, dbError := database.GetAllSalesOrders()
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(result)
}

func GetSaleOrderByIdHandler(ctx *fiber.Ctx) error {
	id, paramError := ctx.ParamsInt("id")
	if paramError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetSaleOrderByIDHandler: Invalid ID %d.\n%v", id, paramError))
	}

	result, dbError := database.GetSaleOrderById(id)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(result)
}

func CreateSaleOrderHandler(ctx *fiber.Ctx) error {
	so := &payloads.SaleOrderPayload{}
	if parseError := ctx.BodyParser(so); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("CreateSaleOrderHandler: Failed to parse the incoming data into type SaleOrderPayload struct.\n%v", parseError))
	}

	createdId, dbError := database.CreateSaleOrder(so)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(fiber.Map{
		"createdId": createdId,
	})
}