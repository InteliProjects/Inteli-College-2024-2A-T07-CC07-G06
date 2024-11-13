import os
import json
import psycopg2
import requests
import math

RDS_HOST = os.getenv('RDS_HOST')
RDS_USER = os.getenv('RDS_USER')
RDS_PASSWORD = os.getenv('RDS_PASSWORD')
RDS_DB = os.getenv('RDS_DB')
RDS_PORT = os.getenv('RDS_PORT', 5432)
RDS_SSL = os.getenv('RDS_SSL', 'True').lower() == 'true'

def get_geo_data(cep):
    api_key = 'cd7c0c3596a34d75ab4de6d45147ac6f'
    url = f"https://api.geoapify.com/v1/geocode/search?text={cep}&apiKey={api_key}"
    try:
        response = requests.get(url)
        response.raise_for_status()
        data = response.json()
        if data['features']:
            coordinates = data['features'][0]['geometry']['coordinates']
            return {'latitude': coordinates[1], 'longitude': coordinates[0]}
        else:
            return None
    except Exception as e:
        print(f"Error fetching geo data: {e}")
        return None

def calculate_distance(lat1, lon1, lat2, lon2):
    R = 6371.0
    lat1_rad = math.radians(lat1)
    lon1_rad = math.radians(lon1)
    lat2_rad = math.radians(lat2)
    lon2_rad = math.radians(lon2)
    delta_lat = lat2_rad - lat1_rad
    delta_lon = lon2_rad - lon1_rad
    a = math.sin(delta_lat / 2)**2 + math.cos(lat1_rad) * math.cos(lat2_rad) * math.sin(delta_lon / 2)**2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
    distance = R * c
    return distance

def get_db_connection():
    connection = psycopg2.connect(
        host=RDS_HOST,
        user=RDS_USER,
        password=RDS_PASSWORD,
        dbname=RDS_DB,
        port=RDS_PORT,
        sslmode='require' if RDS_SSL else 'disable'
    )
    return connection

def lambda_handler(event, context):
    connection = None
    cursor = None
    try:
        body = json.loads(event.get('body', '{}'))
        products = body.get('products', [])
        cep = body.get('cep', '')

        if not isinstance(products, list) or not all(isinstance(p, dict) for p in products):
            return {'statusCode': 400, 'body': json.dumps({'message': 'Invalid products format'})}

        if not isinstance(cep, str) or not cep:
            return {'statusCode': 400, 'body': json.dumps({'message': 'Invalid CEP format'})}

        customer_geo_data = get_geo_data(cep)
        if not customer_geo_data:
            return {'statusCode': 400, 'body': json.dumps({'message': 'Invalid CEP'})}

        lat_customer = customer_geo_data['latitude']
        lon_customer = customer_geo_data['longitude']

        connection = get_db_connection()
        cursor = connection.cursor()

        for product in products:
            product_id = product['productId']
            quantity = product['quantity']

            cursor.execute('''
                SELECT d.id, d.cep, pd.quantity_available
                FROM distributors d
                JOIN products_distributors pd ON pd.distributor_id = d.id
                WHERE pd.product_id = %s AND pd.quantity_available >= %s
            ''', (product_id, quantity))

            distributors = cursor.fetchall()
            if not distributors:
                return {'statusCode': 404, 'body': json.dumps({'message': f'Product {product_id} not available in sufficient quantity'})}

            nearest_distributor = None
            shortest_distance = float('inf')

            for distributor in distributors:
                distributor_id, distributor_cep, quantity_available = distributor
                geo_data_distributor = get_geo_data(distributor_cep)
                if geo_data_distributor:
                    lat_distributor = geo_data_distributor['latitude']
                    lon_distributor = geo_data_distributor['longitude']
                    distance = calculate_distance(lat_customer, lon_customer, lat_distributor, lon_distributor)
                    if distance < shortest_distance:
                        shortest_distance = distance
                        nearest_distributor = {
                            'id': distributor_id,
                            'cep': distributor_cep,
                            'quantity_available': quantity_available,
                            'distance': distance
                        }

            if not nearest_distributor:
                return {'statusCode': 404, 'body': json.dumps({'message': f'No nearby distributor found for product {product_id}'})}

            new_quantity_available = nearest_distributor['quantity_available'] - quantity
            cursor.execute('''
                UPDATE products_distributors
                SET quantity_available = %s
                WHERE distributor_id = %s AND product_id = %s
            ''', (new_quantity_available, nearest_distributor['id'], product_id))

        connection.commit()

        return {
            'statusCode': 200,
            'body': json.dumps({'message': 'Successfully processed purchase'})
        }

    except Exception as e:
        return {
            'statusCode': 500,
            'body': json.dumps({'message': str(e)})
        }
    finally:
        if cursor:
            cursor.close()
        if connection:
            connection.close()
