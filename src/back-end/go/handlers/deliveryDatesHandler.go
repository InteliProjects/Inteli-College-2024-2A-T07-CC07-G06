package handlers

import (
	"nsync/payloads"
	"nsync/services"
	errorhandling "nsync/utils/error-handling"
	"time"

	"github.com/gofiber/fiber/v2"
)

// Responsible for handling the request to calculate delivery dates for a specific product
func GetDeliveryDatesHandler(ctx *fiber.Ctx) error {
	var deliveryPayload payloads.DeliveryPayload
	if parseError := ctx.BodyParser(&deliveryPayload); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetDeliveryDatesHandler: Failed to parse the incoming data into type DeliveryPayload struct.\n%v", parseError))
	}

	deliveryDates, serviceError := services.GetDeliveryDetails(&deliveryPayload)
	if serviceError != nil {
		return errorhandling.ApiError(ctx, serviceError)
	}

	return ctx.Status(202).JSON(fiber.Map{
		"firstDay"	: deliveryDates.FirstDay.Format(time.DateOnly),
		"lastDay" 	: deliveryDates.LastDay.Format(time.DateOnly),
	})
}