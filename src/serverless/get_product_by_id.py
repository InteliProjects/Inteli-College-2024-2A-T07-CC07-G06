import os
import psycopg2

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
        product_id = event['pathParameters']['id']

        connection = get_db_connection()
        cursor = connection.cursor()
        cursor.execute("SELECT id, name, price, description, link_image, sku FROM products WHERE id = %s", (product_id,))
        result = cursor.fetchone()

        if result:
            product = {
                'id': result[0],
                'name': result[1],
                'price': result[2],
                'description': result[3],
                'linkImage': result[4],
                'sku': result[5]
            }
            return product
        else:
            return {
                'statusCode': 404,
                'body': 'Product not found'
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
