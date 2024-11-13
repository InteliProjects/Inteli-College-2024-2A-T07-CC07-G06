package handlers

import (
	"micronsync/csv-processor/database"
	"micronsync/csv-processor/entities"
	"micronsync/csv-processor/errorhandling"
	"micronsync/csv-processor/payloads"
	"mime/multipart"

	"github.com/gofiber/fiber/v2"
)

func PopulateAllHandler(ctx *fiber.Ctx) error {
	fileDHeader, fileDError := ctx.FormFile("FileDistributor")
	filePHeader, filePError := ctx.FormFile("FileProducts")
	filePDHeader, filePDError := ctx.FormFile("FileProductDistributor")
	fileSOHeader, fileSOError := ctx.FormFile("FileSalesOrders")
	fileSOPHeader, fileSOPError := ctx.FormFile("FileSalesProducts")

	errorChannel := make(chan error)

	go processCsvIfExistsRoutine[*entities.Distributor, *payloads.DistributorPayload](fileDHeader, fileDError, errorChannel)
	go processCsvIfExistsRoutine[*entities.Product, *payloads.ProductPayload](filePHeader, filePError, errorChannel)
	distributorError, productError := <-errorChannel, <-errorChannel
	errorHappened1, err1 := errorhandling.AnyNotNil(distributorError, productError)
	if errorHappened1 {
		return errorhandling.ApiError(ctx, err1)
	}
	
	go processCsvIfExistsRoutine[*entities.ProductDistributor, *payloads.ProductDistributorPayload](filePDHeader, filePDError, errorChannel)
	go processCsvIfExistsRoutine[*entities.SaleOrder, *payloads.SaleOrderPayload](fileSOHeader, fileSOError, errorChannel)
	pdError, saleOrderError := <-errorChannel, <-errorChannel
	errorHappened2, err2 := errorhandling.AnyNotNil(pdError, saleOrderError)
	if errorHappened2 {
		return errorhandling.ApiError(ctx, err2)
	}

	go processCsvIfExistsRoutine[*entities.SaleOrderProduct, *payloads.SaleOrderProductPayload](fileSOPHeader, fileSOPError, errorChannel)
	if sopError := <-errorChannel; sopError != nil {
		return errorhandling.ApiError(ctx, sopError)
	}

	return ctx.Status(202).JSON(fiber.Map{
		"message": "successfully processed documents",
	})
}

func processCsvIfExistsRoutine[E entities.Entity, P payloads.EntityPayload](fileHeader *multipart.FileHeader, err error, errorChannel chan error) {
	if err != nil {
		errorChannel <- nil
		return
	}

	if dbError := database.ProcessCsvIntoDB[E, P](fileHeader); dbError != nil {
		errorChannel <- dbError
		return
	}

	errorChannel <- nil
}
