package errorhandling_test

import (
	errorhandling "nsync/utils/error-handling"
	"testing"

	"github.com/gofiber/fiber/v2"
	"github.com/stretchr/testify/assert"
	"github.com/valyala/fasthttp"
)

func TestWriteError(t *testing.T) {
	err := errorhandling.WriteError("This is a %s", "test")
	expectedError := "This is a test"

	assert.EqualError(t, err, expectedError)
}

func TestAnyNotNil(t *testing.T) {
	result, err := errorhandling.AnyNotNil(nil, nil)
	assert.False(t, result)
	assert.Nil(t, err)

	sampleErr := errorhandling.WriteError("Test error")
	result, err = errorhandling.AnyNotNil(nil, sampleErr)
	assert.True(t, result)
	assert.Equal(t, sampleErr, err)
}

func TestApiError(t *testing.T) {
	app := fiber.New()

	req := &fasthttp.Request{}
	resp := &fasthttp.Response{}
	ctx := app.AcquireCtx(&fasthttp.RequestCtx{
		Request:  *req,
		Response: *resp,
	})

	defer app.ReleaseCtx(ctx)

	sampleErr := errorhandling.WriteError("Test error")

	err := errorhandling.ApiError(ctx, sampleErr)

	assert.NoError(t, err)
	assert.Equal(t, 400, ctx.Response().StatusCode())
	assert.JSONEq(t, `{"message":"Test error"}`, string(ctx.Response().Body()))
}

func TestStopIfError(t *testing.T) {
	assert.NotPanics(t, func() {
		errorhandling.StopIfError(nil)
	})

	assert.Panics(t, func() {
		errorhandling.StopIfError(errorhandling.WriteError("Test error"))
	})
}
