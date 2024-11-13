import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';
import { BASE_URL } from '../config.js';

// Configurações gerais do teste
export let options = {
    stages: [
        { duration: '1m', target: 500 },
        { duration: '2m', target: 1000 },
        { duration: '1m', target: 2000 },
    ],

    // Thresholds para avaliar se os requisitos não funcionais (RNFs) estão sendo cumpridos
    thresholds: {
        'http_req_duration': ['p(95)<1500'], // 95% das requisições devem ter duração inferior a 1,5 segundos
        'http_req_failed': ['rate<0.02'],    // Menos de 2% das requisições podem falhar
    },

    // Coleta de métricas personalizadas
    summaryTrendStats: ['avg', 'p(95)', 'p(99)', 'max'],
};

// Métricas personalizadas
const successRate = new Rate('success_rate');
const responseTimeTrend = new Trend('response_time');

// Parâmetros de entrada
const url = `${BASE_URL}/cep/calculate-days`;

// Função principal do teste
export default function () {
    // Gera um CEP aleatório para cada requisição
    const cep = Math.floor(Math.random() * 10000000).toString().padStart(8, '0');
    const urlWithCep = `${url}?cep=${cep}`;

    // Envia a requisição para o endpoint `POST /cep/calculate-days`
    const res = http.post(urlWithCep);

    // Verificações básicas para garantir que a resposta está correta
    const checkRes = check(res, {
        'status é 200': (r) => r.status === 200, // Verifica se o status da resposta é 200 (OK)
        'contém dias de entrega': (r) => r.body.includes('firstDay') && r.body.includes('lastDay'),
    });

    // Atualiza as métricas
    successRate.add(checkRes);
    responseTimeTrend.add(res.timings.duration);

    // Simula um tempo de espera entre as requisições para replicar um cenário real de usuários
    sleep(1);
}
