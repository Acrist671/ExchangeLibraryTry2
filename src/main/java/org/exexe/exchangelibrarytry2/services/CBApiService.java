package org.exexe.exchangelibrarytry2.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class CBApiService {
    private final Logger logger = LoggerFactory.getLogger(CBApiService.class);
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final XmlMapper xmlMapper = new XmlMapper();
    private final String CB_API_URL = "https://www.cbr.ru/scripts/XML_daily.asp?date_req=";
    private final String API_URL_DINAMIC = "https://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=FIRST&date_req2=SECOND";

    public JsonNode GetRatesNow() throws Exception{
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(CB_API_URL)).GET().build();
        return getJsonNode(request, true);
    }

    public JsonNode GetRatebyDate(LocalDate date, String rate) throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(CB_API_URL + date.format(formatter))).GET().build();
        JsonNode allRates = getJsonNode(request, true);
        for (JsonNode jsonNode : allRates.get("Valute")) {
            if (jsonNode.get("ID").asText().equals(rate)) {
                logger.info("Вытащенный узел: {}", jsonNode);
                return jsonNode;
            }
        }
        throw new RuntimeException("Валюта с кодом " + rate + " не найдена");
    }

    public JsonNode GetRatesbyDate(LocalDate date) throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(CB_API_URL + date.format(formatter))).GET().build();
        return getJsonNode(request, true);
    }

    public JsonNode GetDynamicRate(LocalDate date1, LocalDate date2, String rate) throws Exception{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        HttpRequest request = HttpRequest.newBuilder().
                uri(URI.create(API_URL_DINAMIC.replace("FIRST", date1.format(formatter)).
                        replace("SECOND", date2.format(formatter)) + "&VAL_NM_RQ=" + rate)).GET().build();
        return getJsonNode(request, true);
    }

    public ArrayNode GetDynamicRates(LocalDate date1, LocalDate date2){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<LocalDate> dateList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode allRates = objectMapper.createArrayNode();
        for (LocalDate date = date1; !date.isAfter(date2);
             date = date.plusDays(1)) {
            dateList.add(date);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<JsonNode>> futures = new ArrayList<>();
        for (LocalDate date : dateList) {
            futures.add(executorService.submit(()-> {
                HttpRequest httpRequest = HttpRequest.newBuilder().
                        uri(URI.create(CB_API_URL + date.format(formatter))).
                        timeout(Duration.ofSeconds(10)).GET().build();
                return getJsonNode(httpRequest, false);
            }));
        }
        for (Future<JsonNode> future : futures) {
            try{
                allRates.add(future.get());
            } catch (Exception e) {
                logger.error("Ошибка при получении JsonArray: {}", e.getMessage());
            }
        }
        executorService.shutdown();
        return allRates;
    }


    private JsonNode getJsonNode(HttpRequest request, boolean isLog) throws IOException, InterruptedException {
        try {
            HttpResponse<String> response = httpClient.
                    send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode result = xmlMapper.readTree(response.body());
            if (isLog) {
                logger.info("Полученный ответ от ЦБ: {}", response.body());
                logger.info("Json Ответ: {}", result);
            }
            return result;
        } catch (Exception e) {
            logger.error("Ошибка при запросе к ЦБ: {}", e.getMessage());
            throw e;
        }
    }
}
