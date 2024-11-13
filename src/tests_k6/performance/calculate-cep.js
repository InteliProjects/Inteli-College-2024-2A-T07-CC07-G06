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

    // Thresholds para avaliar se os requisitos não funcionais (RNFs) estão sendo cumpridos
    thresholds: {
        'http_req_duration': ['p(95)<1000'], // 95% das requisições devem ter duração inferior a 1 segundo (RNF 2)
        'http_req_failed': ['rate<0.01'],    // Menos de 1% das requisições podem falhar (RNF 3)
    },

    // Coleta de métricas detalhadas
    summaryTrendStats: ['avg', 'p(95)', 'p(99)', 'max'],
};

// Métrica personalizada para taxa de sucesso
const successRate = new Rate('success_rate');

// Parâmetros de entrada
const url = `${BASE_URL}/cep/calculate-days`;

// Função principal do teste
export default function () {
    const urlWithCep = `${url}?cep=01433170`;

    // Envia a requisição para o endpoint `calculate-cep`
    const res = http.post(urlWithCep);

    // Verificações básicas para garantir que a resposta está correta
    const checkRes = check(res, {
        'status é 200': (r) => r.status === 200, // Verifica se o status da resposta é 200 (OK)
        // Verifica se a resposta contém firstDay e lastDay
        'contém dias de entrega': (r) => r.body.includes('firstDay') && r.body.includes('lastDay'),
    });

    // Atualiza a métrica de taxa de sucesso
    successRate.add(checkRes);

    // Simula um tempo de espera entre as requisições para replicar um cenário real de usuários
    sleep(1);
}

