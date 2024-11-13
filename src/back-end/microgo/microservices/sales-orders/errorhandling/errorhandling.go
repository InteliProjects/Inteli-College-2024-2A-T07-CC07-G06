package errorhandling

import (
	"fmt"
	"github.com/gofiber/fiber/v2"
)

func WriteError(errorMessage string, formattingArgs ...any) error {
	return fmt.Errorf(errorMessage, formattingArgs...)
}

func AnyNotNil(errors ...error) (bool, error) {
	for _, err := range errors {
		if err != nil {
			return true, err
		}
	}

	return false, nil
}

func ApiError(context *fiber.Ctx, err error) error {
	return context.Status(400).JSON(fiber.Map{
		"message": err.Error(),
	})
}

func StopIfError(err error) {
	if err != nil {
		panic(err.Error())
	}
}
