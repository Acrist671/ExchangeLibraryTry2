package org.exexe.exchangelibrarytry2.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class ExchangeRates {
    @JsonProperty("Date")
    private String date;
    @JsonProperty("name")
    private String name;
    @JsonProperty("Valute")
    private List<Exchange> exchanges;
}
