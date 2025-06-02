package org.exexe.exchangelibrarytry2.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.exexe.exchangelibrarytry2.models.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.exexe.exchangelibrarytry2.services.CBApiService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/exchange")
public class CBApiController {
    private final CBApiService cbApiService;

    public CBApiController(CBApiService cbApiService) {
        this.cbApiService = cbApiService;
    }

    @GetMapping("/now")
    public JsonNode GetRatesNow() throws Exception {
        return cbApiService.GetRatesNow();
    }

    @GetMapping("/byDate")
    public JsonNode GetRatesbyDate(@RequestParam String date) throws Exception {
        return cbApiService.GetRatesbyDate(LocalDate.parse(date));
    }

    @GetMapping("/valueByDate")
    public JsonNode GetRatebyDate(@RequestParam String date,
                                  @RequestParam String rate) throws Exception {
        return cbApiService.GetRatebyDate(LocalDate.parse(date), rate);
    }

    @GetMapping("/dynamicRate")
    public JsonNode GetDynamicRate(@RequestParam String date1,
                                   @RequestParam String date2,
                                   @RequestParam String rate) throws Exception {
        return cbApiService.GetDynamicRate(LocalDate.parse(date1),
                LocalDate.parse(date2), rate);
    }

    @GetMapping("/dynamicRates")
    public ArrayNode GetDynamicRates(@RequestParam String date1,
                                     @RequestParam String date2){
        return cbApiService.GetDynamicRates(LocalDate.parse(date1),
                LocalDate.parse(date2));
    }
}

