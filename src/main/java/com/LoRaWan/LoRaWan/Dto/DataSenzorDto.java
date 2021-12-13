package com.LoRaWan.LoRaWan.Dto;

import lombok.Data;

import java.util.List;

@Data
public class DataSenzorDto {

    List<Float> temperature;
    List<Float> humidity;
    List<Float> airQuality;

    public DataSenzorDto(List<Float> temperature, List<Float> humidity, List<Float> airQuality) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.airQuality = airQuality;
    }
}
