import os
import psycopg2
import json

RDS_HOST = os.getenv('RDS_HOST')
RDS_USER = os.getenv('RDS_USER')
RDS_PASSWORD = os.getenv('RDS_PASSWORD')
RDS_DB = os.getenv('RDS_DB')
RDS_PORT = os.getenv('RDS_PORT', 5432)
RDS_SSL = os.getenv('RDS_SSL', 'True').lower() == 'true'

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
        print("Received event: ", event)
        
        try:
            body = json.loads(event['body'])
        except Exception as e:
            return {
                'statusCode': 400,
                'body': f"Invalid JSON format: {str(e)}"
            }
        
        products_ids = body.get('productsIds', None)
        if not isinstance(products_ids, list):
            return {
                'statusCode': 400,
                'body': 'productsIds is required and must be a list of integers'
            }

        if not products_ids:
            return {
                'statusCode': 400,
                'body': 'No product IDs provided'
            }

        if not all(isinstance(i, int) for i in products_ids):
            return {
                'statusCode': 400,
                'body': 'All product IDs must be integers'
            }

        ids_placeholders = ', '.join(['%s'] * len(products_ids))

        query = f"SELECT id, name, price, description, link_image, sku FROM products WHERE id IN ({ids_placeholders})"
        
        connection = get_db_connection()
        cursor = connection.cursor()
        cursor.execute(query, tuple(products_ids))
        results = cursor.fetchall()

        if results:
            products = [
                {
                    'id': row[0],
                    'name': row[1],
                    'price': row[2],
                    'description': row[3],
                    'linkImage': row[4],
                    'sku': row[5]
                }
                for row in results
            ]
            return {
                'statusCode': 200,
                'body': json.dumps(products)
            }
        else:
            return {
                'statusCode': 404,
                'body': 'No products found'
            }
    except Exception as e:
        return {
            'statusCode': 500,
            'body': str(e)
        }
    finally:
        if cursor:
            cursor.close()
        if connection:
            connection.close()
