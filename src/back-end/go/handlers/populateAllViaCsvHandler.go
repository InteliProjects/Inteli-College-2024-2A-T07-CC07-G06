package handlers

import (
	dbUtils "nsync/utils/database"
	errorhandling "nsync/utils/error-handling"
	"nsync/entities"
	"nsync/payloads"
	"mime/multipart"

	"github.com/gofiber/fiber/v2"
)

// Responsible for handling the request to populate all database tables via CSV files upload
func PopulateAllViaCsvHandler(ctx *fiber.Ctx) error {
	fileDHeader, fileDError := ctx.FormFile("fileDistributor")
	filePHeader, filePError := ctx.FormFile("fileProduct")
	filePDHeader, filePDError := ctx.FormFile("fileProductDistributor")
	fileSOHeader, fileSOError := ctx.FormFile("fileSalesOrders")
	fileSOPHeader, fileSOPError := ctx.FormFile("fileSalesProducts")

	if processDError := processCsvIfExists[*entities.Distributor, *payloads.DistributorPayload](fileDHeader, fileDError); processDError != nil {
		return errorhandling.ApiError(ctx, processDError)
	}
	
	if processPError := processCsvIfExists[*entities.Product, *payloads.ProductPayload](filePHeader, filePError); processPError != nil {
		return errorhandling.ApiError(ctx, processPError)
	}
	
	if processPDError := processCsvIfExists[*entities.ProductDistributor, *payloads.ProductDistributorPayload](filePDHeader, filePDError); processPDError != nil {
		return errorhandling.ApiError(ctx, processPDError)
	}
	
	if processSOError := processCsvIfExists[*entities.SaleOrder, *payloads.SaleOrderPayload](fileSOHeader, fileSOError); processSOError != nil {
		return errorhandling.ApiError(ctx, processSOError)
	}
	
	if processSOPError := processCsvIfExists[*entities.SaleOrderProduct, *payloads.SaleOrderProductPayload](fileSOPHeader, fileSOPError); processSOPError != nil {
		return errorhandling.ApiError(ctx, processSOPError)
	}

	return ctx.Status(202).JSON(fiber.Map{
		"message":"successfully processed documents",
	})
}

func processCsvIfExists[E entities.Entity, P payloads.EntityPayload](fileHeader *multipart.FileHeader, err error) error {
	if err != nil {
		return nil
	}

	if dbError := dbUtils.ProcessCsvIntoDB[E, P](fileHeader); dbError != nil {
		return dbError
	}

	return nil
}