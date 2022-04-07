package com.LoRaWan.LoRaWan.PresentationLayer.Dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DateValue {

    private List<Float> humidity;
    private List<Float> airQuality;
    private List<Float> temperature;

    private List<Date> dates;
    private List<Integer> month;

}
