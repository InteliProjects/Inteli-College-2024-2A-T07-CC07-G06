// # Package handlers
//
// This package provides handlers which receive the HTTP requests from the front-end and send back the responses to the client.
package handlers

import (
	dbUtils "nsync/utils/database"
	errorhandling "nsync/utils/error-handling"
	"nsync/entities"
	"nsync/payloads"

	"github.com/gofiber/fiber/v2"
)

// Deals with "GET" requests for the whole database table's content for the specified entity
func GetAllHandler[E entities.Entity](ctx *fiber.Ctx) error {
	result, dbError := dbUtils.GetAll[E]()
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(result)
}

// Deals with "GET" requests for the database table's content for the specified entity according to the row id specified in the route parameters
func GetByIdHandler[E entities.Entity](ctx *fiber.Ctx) error {
	id, idError := ctx.ParamsInt("id")
	if idError != nil {
		var entity E
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetByIdHandler[%T]: Failed to parse url parameter.\n%v", entity, idError))
	}
	result, dbError := dbUtils.GetById[E](id)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(result)
}

// Deals with "POST" requests for the specified entity according to its equivalent payload
func CreateHandler[E entities.Entity, P payloads.EntityPayload](ctx *fiber.Ctx) error {
	var p P
	if parseError := ctx.BodyParser(p); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("CreateHandler[%T]: Failed to parse the incoming data into type %T struct.\n%v", p, p, parseError))
	}

	id, dbError := dbUtils.Create[E](p)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(fiber.Map{
		"createdId": id,
	})
}

// Deals with "PUT" requests for the specified entity according to its equivalent payload and the row id specified in the route parameters
func UpdateHandler[E entities.Entity, P payloads.EntityPayload](ctx *fiber.Ctx) error {
	var p P
	if parseError := ctx.BodyParser(p); parseError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("UpdateHandler[%T]: Failed to parse the incoming data into type %T struct.\n%v", p, p, parseError))
	}

	id, idError := ctx.ParamsInt("id")
	if idError != nil {
		var entity E
		return errorhandling.ApiError(ctx, errorhandling.WriteError("GetByIdHandler[%T]: Failed to parse url parameter.\n%v", entity, idError))
	}

	dbError := dbUtils.Update[E](id, p)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(fiber.Map{
		"message": "data successfully updated",
	})
}

// Deals with "DELETE" requests for the specified entity according to the row id specified in the route parameters
func DeleteHandler[E entities.Entity](ctx *fiber.Ctx) error {
	id, _ := ctx.ParamsInt("id")
	dbError := dbUtils.Delete[E](id)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(200).JSON(fiber.Map{
		"message": "data successfully deleted",
	})
}

// Deals with "POST" requests to create entity data on the application's database from a CSV file for the specified entity according to its equivalent payload
func CreateViaCsvHandler[E entities.Entity, P payloads.EntityPayload](ctx *fiber.Ctx) error {
	var entity E

	fileHeader, fileError := ctx.FormFile("file")
	if fileError != nil {
		return errorhandling.ApiError(ctx, errorhandling.WriteError("CreateViaCSVHandler[%T]: Failed to get the uploaded file from request context.\n%v", entity, fileError))
	}

	dbError := dbUtils.ProcessCsvIntoDB[E, P](fileHeader)
	if dbError != nil {
		return errorhandling.ApiError(ctx, dbError)
	}

	return ctx.Status(202).JSON(fiber.Map{
		"message" : "data successfully created",
	})
}