// # Package errorhandling
//
// This package provides functions to create custom errors and handle errors.
//
// It is and should be used throughout the whole application so the developers can know specifically what went wrong and where it happened.
package errorhandling

import (
	"fmt"

	"github.com/gofiber/fiber/v2"
)

// Creates a custom error with the specified error message.
//
// The error message must specify any placeholder values in the string based on the "fmt" package tags ("%s", "%d", "%v"...).
// They will be replaced accordingly and in order of appearance by the "formattingArgs" parameter values.
func WriteError(errorMessage string, formattingArgs ...any) error {
	return fmt.Errorf(errorMessage, formattingArgs...)
}

// Checks whether any of the errors passed as parameters is not nil.
// If so, the error is returned alongside a "true" valued boolean.
func AnyNotNil(errors ...error) (bool, error) {
	for _, err := range errors {
		if err != nil {
			return true, err
		}
	}
	
	return false, nil
}

// Returns a 400 HTTP status (Bad Request) error with the message specified on the "err" parameter.
//
// This function should only be used by the application's handlers, as it returns a HTTP status to the client.
func ApiError(context *fiber.Ctx, err error) error {
	return context.Status(400).JSON(fiber.Map{
		"message" : err.Error(),
	})
}

// Panics the application if the "err" parameter is not nil, causing the termination of the program if the "recover" function is not called before the panic gets to "main" function.
// The error passed as a parameter is logged before the program is terminated.
//
// The usage of this function is heavily discouraged:
// it should only be used to deal with critical errors on really vital parts of the application that are undismissable to even run the program,
// as it does not handle the error and stops the program's execution immediately.
func StopIfError(err error) {
	if err != nil {
		panic(err.Error())
	}
}
