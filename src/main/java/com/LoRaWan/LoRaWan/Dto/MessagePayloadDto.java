package com.LoRaWan.LoRaWan.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessagePayloadDto {

    private float temperature;
    private float humidity;
    private float airQuality;
    private Date date;

}
