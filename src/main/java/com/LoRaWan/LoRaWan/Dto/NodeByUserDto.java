package com.LoRaWan.LoRaWan.Dto;

import lombok.Data;

@Data
public class NodeByUserDto {

    private String name;
    private String devEui;
    private String appEui;
    private String battery;
    private boolean active;


}
