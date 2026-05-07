package com.example.demo.dto;

public class ChatResponse {

    private String response;
    private long responseTime;
    private String model;

    public ChatResponse() {
    }

    public ChatResponse(String response, long responseTime, String model) {
        this.response = response;
        this.responseTime = responseTime;
        this.model = model;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}