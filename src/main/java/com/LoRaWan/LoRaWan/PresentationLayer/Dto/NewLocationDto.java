package com.LoRaWan.LoRaWan.PresentationLayer.Dto;

import lombok.Data;

@Data
public class NewLocationDto {

    private String name;
    private Float rssi;
    private String lat;
    private String log;
}
