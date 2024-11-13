FROM golang:1.23.0-bullseye AS build

COPY . /app

WORKDIR /app

RUN go get nsync

RUN go build -o /server

FROM gcr.io/distroless/base-debian10

WORKDIR /

COPY --from=build /server /server

EXPOSE 8080

USER nonroot:nonroot

ENTRYPOINT [ "/server" ]