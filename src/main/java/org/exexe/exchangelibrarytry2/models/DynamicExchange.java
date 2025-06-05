package org.exexe.exchangelibrarytry2.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DynamicExchange {
    @JsonProperty("Date")
    private String date;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Nominal")
    private int nominal;
    @JsonProperty("Value")
    private String value;
    @JsonProperty("VunitRate")
    private String vunitRate;

    @Override
    public String toString(){
        return date + " - " + Float.parseFloat(value.replace(",", ".")) / nominal + "\n";
    }
}
