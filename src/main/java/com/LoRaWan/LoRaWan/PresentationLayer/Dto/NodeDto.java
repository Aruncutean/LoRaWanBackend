package com.LoRaWan.LoRaWan.PresentationLayer.Dto;

import lombok.Data;

@Data
public class NodeDto {

    private String userEmail;
    private String name;
    private String devEui;
    private String appEui;
    private String lat;
    private String log;

    public NodeDto(String name, String devEui, String appEui, String lat, String log) {
        this.appEui = appEui;
        this.name = name;
        this.devEui = devEui;
        this.lat = lat;
        this.log = log;
    }
}
