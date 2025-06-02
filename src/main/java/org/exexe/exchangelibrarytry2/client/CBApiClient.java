package org.exexe.exchangelibrarytry2.client;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.exexe.exchangelibrarytry2.models.DynamicExchangeRates;
import org.exexe.exchangelibrarytry2.models.Exchange;
import org.exexe.exchangelibrarytry2.models.ExchangeRates;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class CBApiClient {
    private static final String API_BASE_URL = "http://localhost:8080/api/exchange";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, String> GetNameofExchanges() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(API_BASE_URL + "/now")).GET().build();
        HttpResponse<String> response = client.
                send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), ExchangeRates.class)
                .getExchanges().stream().
                collect(Collectors.toMap(Exchange::getName, Exchange::getId));
    }

    public static List<Exchange> GetExchangeRatesbyDate(String date) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.
                create(API_BASE_URL + "/byDate?date=" + date)).GET().build();
        HttpResponse<String> response = client.
                send(request, HttpResponse.BodyHandlers.ofString());
        ExchangeRates rates = mapper.readValue(response.body(), ExchangeRates.class);
            return rates.getExchanges();
    }

    public static Exchange GetExchangebyDate(String date, String rate) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.
                create(API_BASE_URL + "/valueByDate?date=" + date + "&rate=" + rate)).GET().build();
        HttpResponse<String> response = client.
                send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), Exchange.class);
    }

    public static DynamicExchangeRates GetDynamicExchange(String date1, String date2, String rate) throws IOException, InterruptedException {
        String strRequest = API_BASE_URL + "/dynamicRate?date1=" +
                date1 + "&date2=" + date2 + "&rate=" + rate;
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(strRequest)).GET().build();
        HttpResponse<String> response = client.
                send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), DynamicExchangeRates.class);
    }

    public static List<ExchangeRates> GetDynamicExchangesRates(String date1, String date2) throws IOException, InterruptedException {
        String strRequest = API_BASE_URL + "/dynamicRates?date1=" +
                date1 + "&date2=" + date2;
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(strRequest)).GET().build();
        HttpResponse<String> response = client.
                send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<ExchangeRates>>() {});
    }
}
