package org.exexe.exchangelibrarytry2.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DynamicExchangeRates {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("DateRange1")
    private String data1;
    @JsonProperty("DateRange2")
    private String data2;
    @JsonProperty("name")
    private String name;
    @JsonProperty("Record")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<DynamicExchange> exchangeList;

    @Override
    public String toString() {
        List<String> fullString = exchangeList.stream().
                map(DynamicExchange::toString).toList();
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("\n");
        for (String s : fullString) {
            sb.append(s);
        }
        return sb.toString();
    }
}
