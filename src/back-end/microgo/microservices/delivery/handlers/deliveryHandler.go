package handlers

import (
	"micronsync/delivery/errorhandling"
	"micronsync/delivery/payloads"
	"micronsync/delivery/services"
	"time"

	"github.com/gofiber/fiber/v2"
)

func GetDeliveryDatesHandler(ctx *fiber.Ctx) error {
	var deliveryPayload payloads.DeliveryPayload
	if parseError := ctx.BodyParser(&deliveryPayload); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetDeliveryDatesHandler: Failed to parse the incoming data into type DeliveryPayload struct.\n%v", parseError))
	}

	deliveryDates, serviceError := services.GetDeliveryDetails(deliveryPayload.ProductId, deliveryPayload.Quantity, deliveryPayload.Cep)
	if serviceError != nil {
		return errorhandling.ApiError(ctx, serviceError)
	}

	return ctx.Status(202).JSON(fiber.Map{
		"firstDay": deliveryDates.FirstDay.Format(time.DateOnly),
		"lastDay":  deliveryDates.LastDay.Format(time.DateOnly),
	})
}

func GetDeliveryDetailsHandler(ctx *fiber.Ctx) error {
	var deliveryPayload payloads.DeliveryPayload
	if parseError := ctx.BodyParser(&deliveryPayload); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetDeliveryDetailsHandler: Failed to parse the incoming data into type DeliveryPayload struct.\n%v", parseError))
	}

	deliveryDetails, serviceError := services.GetDeliveryDetails(deliveryPayload.ProductId, deliveryPayload.Quantity, deliveryPayload.Cep)
	if serviceError != nil {
		return errorhandling.ApiError(ctx, serviceError)
	}

	return ctx.Status(202).JSON(deliveryDetails)
}