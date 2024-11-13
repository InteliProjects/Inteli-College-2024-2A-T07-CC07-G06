import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';
import { BASE_URL } from '../config.js';

// Configurações gerais do teste
export let options = {
    stages: [
        { duration: '1m', target: 200 },
        { duration: '1m', target: 200 },
        { duration: '1m', target: 0 },
    ],

    thresholds: {
        'http_req_duration': ['p(95)<1000'], // 95% das requisições devem ter duração inferior a 1 segundo
        'http_req_failed': ['rate<0.01'],    // Menos de 1% das requisições podem falhar
    },

    summaryTrendStats: ['avg', 'p(90)', 'p(95)', 'p(99)', 'max'],
};

// Métrica personalizada para taxa de sucesso
const successRate = new Rate('success_rate');

// Parâmetros de entrada
const url = `${BASE_URL}/products`;

// Função principal do teste
export default function () {
    // Envia a requisição para o endpoint `GET /products`
    const res = http.get(url);

    // Verificações básicas para garantir que a resposta está correta
    const checkRes = check(res, {
        'status é 200': (r) => r.status === 200,
    });

    // Atualiza a métrica de taxa de sucesso
    successRate.add(checkRes);

    // Simula um tempo de espera entre as requisições para evitar sobrecarregar o sistema
    sleep(1);
}
