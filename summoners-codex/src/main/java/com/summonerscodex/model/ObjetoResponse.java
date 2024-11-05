package com.summonerscodex.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjetoResponse {
    private Map<String, Objeto> data; 

    public Map<String, Objeto> getData() {
        return data;
    }

    public void setData(Map<String, Objeto> data) {
        this.data = data;
    }
}
