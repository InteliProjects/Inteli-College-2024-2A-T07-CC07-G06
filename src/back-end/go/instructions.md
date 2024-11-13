# Go

This project uses Fiber, the Express-Inspired Go Framework.

If you want to learn more about Fiber, please visit its website: https://gofiber.io.

## Application requirements

- Go (download and install: https://go.dev/doc/install)

## Running the application locally

It's important to note that it's mandatory to set the ```.env``` file variables before running the application locally:

- Replace "DB_HOST" with your database host address
- Replace "DB_PORT" with your database port 
- Replace "DB_USERNAME" with your database username 
- Replace "DB_PASSWORD" with your database password 
- Replace "DB_NAME" with your database name

The "CORS_ORIGINS" variable can be changed according to the CORS configuration you want for the application.

#### Running without Docker

```bash
# On the project's root folder
go mod tidy # Checks the dependencies
go run ./main.go # Runs the application
```

#### Running with Docker

First, install Docker: https://www.docker.com/products/docker-desktop/

```bash
# On the project's root folder
docker build -t ImageName . # Builds the project image. Replace "ImageName" with the desired image name
docker-compose up # Runs the application
```

## Extra

The ```.bashrc``` file is a template for a ```.bashrc``` file used for deploying the application on to a Linux OS virtual machine. First, edit its variables accordingly (same as with the ```.env``` file). Then, on the linux terminal, run:

```bash
source ~./bashrc
```

Then your environment variables will be set.

In the case of deploying to a Linux OS virtual machine from a locally built Docker image, run the application with the following command:

```bash
docker run -d -p 8080:8080 NameOfTheImage # replace "NameOfTheImage" with the name of the image
```