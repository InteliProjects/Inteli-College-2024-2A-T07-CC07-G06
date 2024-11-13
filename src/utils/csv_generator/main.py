import csv
import random
import string

def generate_id(n):
    return list(range(1, n + 1))

# example: ABCD1234E5678
def generate_sku():
    letters = ''.join(random.choices(string.ascii_uppercase, k=4))
    numbers1 = ''.join(random.choices(string.digits, k=4))
    letter = random.choice(string.ascii_uppercase)
    numbers2 = ''.join(random.choices(string.digits, k=4))
    return f"{letters}{numbers1}{letter}{numbers2}"

def generate_sku_list(n):
    return [generate_sku() for _ in range(n)]

def randomize_list(values, n):
    return [random.choice(values) for _ in range(n)]

def generate_image_link(name_list, image_dict, n):
    return [image_dict.get(name, 'default_image_link') for name in randomize_list(name_list, n)]

def generate_csv(filename, n, product_dict, price_list, description_list):
    ids = generate_id(n)
    skus = generate_sku_list(n)
    products = randomize_list(list(product_dict.keys()), n)
    prices = randomize_list(price_list, n)
    descriptions = randomize_list(description_list, n)
    
    with open(filename, mode='w', newline='', encoding='utf-8') as file:
        writer = csv.writer(file)
        writer.writerow(['id', 'sku', 'name', 'price', 'description', 'link_image'])
        for i in range(n):
            name = products[i]
            link_image = product_dict[name]
            writer.writerow([ids[i], skus[i], name, prices[i], descriptions[i], link_image])

product_dict = {
    'Smartphone Samsung Galaxy A05s': 'https://m.media-amazon.com/images/I/61YRRa3fGcL._AC_SX679_.jpg',
    'Smartphone Xiaomi Redmi': 'https://m.media-amazon.com/images/I/41Lo5ne-3SL._AC_SX679_.jpg',
    'Apple iPhone 16': 'https://m.media-amazon.com/images/I/51m8qUn-4tL._AC_SX679_.jpg',
    'Samsung Celular Galaxy A55': 'https://m.media-amazon.com/images/I/51WguaffFYL._AC_SX679_.jpg',
    'Smartphone POCO C65': 'https://m.media-amazon.com/images/I/51cHxUvz65L._AC_SX679_.jpg',
    'Apple iPhone 14': 'https://m.media-amazon.com/images/I/41m1oYOQiqL.__AC_SY445_SX342_QL70_ML2_.jpg',
    'Realme Note 50 Dual Sim': 'https://m.media-amazon.com/images/I/619Ri0rsYYL._AC_SX679_.jpg',
    'Celular Xiaomi Redmi 12C': 'https://m.media-amazon.com/images/I/61YQz2VIqKL._AC_SX679_.jpg',
    'Apple iPhone 13 (128 GB)': 'https://m.media-amazon.com/images/I/41w6af-VsIL._AC_SX679_.jpg',
    'Celular Xiaomi Redmi Note 12': 'https://m.media-amazon.com/images/I/61FQ61igtVL._AC_SY879_.jpg',
    'Samsung Galaxy M35 5G': 'https://m.media-amazon.com/images/I/51ucmFnJ9oL._AC_SX679_.jpg',
    'Smartphone Samsung Galaxy S24': 'https://m.media-amazon.com/images/I/61x53c15DqL._AC_SX679_.jpg'
}
price_list = [
    1299.99, 1499.99, 1699.99, 1899.99, 2099.99, 2299.99, 
    2499.99, 2699.99, 2999.99, 3299.99, 3599.99, 3899.99,
    4199.99, 4499.99, 4799.99, 5099.99, 5499.99, 5999.99,
    6499.99, 6999.99, 7499.99, 7999.99, 8499.99, 8999.99
]
description_list = [
    'Smartphone com tela de alta resolução e armazenamento expansível.',
    'Desempenho rápido e eficiente com câmera de alta qualidade.',
    'Celular moderno com bateria de longa duração e carregamento rápido.',
    'Tecnologia avançada para uma experiência móvel premium.',
    'Aparelho com design elegante e processamento ágil para multitarefas.',
    'Smartphone com grande capacidade de armazenamento e câmeras poderosas.',
    'Tela AMOLED vibrante com taxa de atualização suave.',
    'Desempenho sólido com recursos de segurança avançados.',
    'Celular compacto e eficiente, ideal para o dia a dia.',
    'Equipado com múltiplas câmeras para fotos detalhadas e vídeos em alta resolução.',
    'Aparelho com 5G para navegação rápida e desempenho fluido.',
    'Design moderno e resistente, ideal para qualquer situação.'
]

generate_csv('products.csv', 2000000, product_dict, price_list, description_list)