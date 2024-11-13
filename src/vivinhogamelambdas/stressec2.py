import json
import requests
from concurrent.futures import ThreadPoolExecutor

# Corpo mockado para a requisição
PAYLOAD = {
    "products": [
        {
            "productId": 1,
            "quantity": 1
        }
    ],
    "cep": "03343-010"
}

HEADERS = {
    'Content-Type': 'application/json'
}

def send_post_request(base_url):
    url = f'{base_url}/purchases'
    response = requests.post(url, json=PAYLOAD, headers=HEADERS)
    return response.status_code, response.text

def lambda_handler(event, context):
    base_url = event.get('baseUrl')
    num_requests = event.get('numRequests', 1)

    if not base_url:
        return {
            "statusCode": 400,
            "body": "Missing 'baseUrl' in event"
        }

    with ThreadPoolExecutor() as executor:
        futures = [executor.submit(send_post_request, base_url) for _ in range(num_requests)]
        results = [future.result() for future in futures]

    return {
        "statusCode": 200,
        "body": {
            "requests_made": num_requests,
            "responses": results
        }
    }
