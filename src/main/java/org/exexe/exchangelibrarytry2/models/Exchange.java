package org.exexe.exchangelibrarytry2.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class Exchange {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("NumCode")
    private String numCode;
    @JsonProperty("CharCode")
    private String charCode;
    @JsonProperty("Nominal")
    private int nominal;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Value")
    private String value;
    @JsonProperty("VunitRate")
    private String vunitRate;

    @Override
    public String toString() {
        return String.format(
                "name ='%s', value = '%s'",
                 name, Float.parseFloat(value)/nominal
        );
    }

    public String getValuebyString(){
        return String.valueOf(Float.
                parseFloat(value.replace(",", "."))/ nominal);
    }
}
