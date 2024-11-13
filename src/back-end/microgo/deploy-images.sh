# shellcheck disable=SC2125
# shellcheck disable=SC2102
DOCKER_USERNAME=maurodaschagasjunior # Don't forget to open docker desktop

services=("products" "distributors" "products-distributors" "delivery" "purchase" "sales-orders" "sales-orders-products" "csv-processor")

for service in "${services[@]}"
do
  echo "Building and pushing $service..."

  service_path="./microservices/$service"

  docker build -t "$DOCKER_USERNAME"/"$service":latest "$service_path"

  docker push "$DOCKER_USERNAME"/"$service":latest

  echo "$service image pushed successfully!"
done