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
        connection = get_db_connection()
        cursor = connection.cursor()
        
        cursor.execute("SELECT id, name, price, description, link_image, sku FROM products")
        result = cursor.fetchall()
        
        products = [{
            'id': row[0],
            'name': row[1],
            'price': row[2],
            'description': row[3],
            'linkImage': row[4],
            'sku': row[5]
        } for row in result]
        
        return products
    
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
