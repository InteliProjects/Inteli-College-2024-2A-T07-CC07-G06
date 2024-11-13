package com.nsync.service.cep;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeoData {

    public GeoResponse getGeoData(String cep) {
        String cepFormatted = cep.substring(0, 5) + '-' + cep.substring(5);
        String apiKey = "cd7c0c3596a34d75ab4de6d45147ac6f";
        GeoResponse geoResponse = new GeoResponse();

        try {
            String url = String.format(
                    "https://api.geoapify.com/v1/geocode/search?text=%s&apiKey=%s",
                    cepFormatted, apiKey
            );
            // Criando o cliente HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Criando a requisição
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            // Enviando a requisição e recebendo a resposta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Processando a resposta JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(response.body());

            if (jsonResponse.has("features") && jsonResponse.get("features").isArray()) {
                JsonNode firstFeature = jsonResponse.get("features").get(0);
                if (firstFeature.has("geometry")) {
                    JsonNode coordinates = firstFeature.get("geometry").get("coordinates");
                    double longitude = coordinates.get(0).asDouble();
                    double latitude = coordinates.get(1).asDouble();

                    geoResponse.setLatitude(latitude);
                    geoResponse.setLongitude(longitude);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return geoResponse;
    }
}
