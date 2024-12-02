package com.summonerscodex.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CampeonResponse {
    
    private String type;
    private String format;
    private String version;

    
    @JsonProperty("data")
    private Map<String, Campeon> data; 

    // Getters y setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Campeon> getData() {
        return data;
    }

    public void setData(Map<String, Campeon> data) {
        this.data = data;
    }
}
