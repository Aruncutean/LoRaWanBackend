package com.LoRaWan.LoRaWan.Service;

import com.LoRaWan.LoRaWan.Date.GraphRequest;
import com.LoRaWan.LoRaWan.Date.Node;
import com.LoRaWan.LoRaWan.Dto.DateValue;
import com.LoRaWan.LoRaWan.Repository.AirQualityRepository;
import com.LoRaWan.LoRaWan.Repository.HumidityRepository;
import com.LoRaWan.LoRaWan.Repository.TemperatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DateService {

    @Autowired
    private HumidityRepository humidityRepository;

    @Autowired
    private AirQualityRepository airQualityRepository;

    @Autowired
    private TemperatureRepository temperatureRepository;

    public DateValue getDateLast7Day(String name) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -17);

        Date dateC = c.getTime();

        List<GraphRequest> humidity = humidityRepository.getHumidityForDate(name, dateC);
        List<GraphRequest> temperature = temperatureRepository.getTemperatureForDate(name, dateC);
        List<GraphRequest> airQuality = airQualityRepository.getAirQualityForDate(name, dateC);
        DateValue dateValue = new DateValue();
        List<Float> humidityDate = new ArrayList<>();
        List<Float> temperatureDate = new ArrayList<>();
        List<Float> airQualityDate = new ArrayList<>();
        List<Date> integers = new ArrayList<>();
        for (int i = 0; i < humidity.size(); i++) {
            humidityDate.add(humidity.get(i).getVal());
            temperatureDate.add(temperature.get(i).getVal());
            airQualityDate.add(airQuality.get(i).getVal());
            integers.add(humidity.get(i).getDate());
        }
        dateValue.setHumidity(humidityDate);
        dateValue.setTemperature(temperatureDate);
        dateValue.setAirQuality(airQualityDate);
        dateValue.setDates(integers);

        return dateValue;
    }

    public DateValue getDateLastMount(String name) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -1);

        Date dateC = c.getTime();
        System.out.println(dateC.toString());
        System.out.println(name);
        List<GraphRequest> humidity = humidityRepository.getHumidityForDate(name, dateC);
        List<GraphRequest> temperature = temperatureRepository.getTemperatureForDate(name, dateC);
        List<GraphRequest> airQuality = airQualityRepository.getAirQualityForDate(name, dateC);
        DateValue dateValue = new DateValue();

        List<Float> humidityDate = new ArrayList<>();
        List<Float> temperatureDate = new ArrayList<>();
        List<Float> airQualityDate = new ArrayList<>();
        List<Date> integers = new ArrayList<>();
        for (int i = 0; i < humidity.size(); i++) {
            humidityDate.add(humidity.get(i).getVal());
            temperatureDate.add(temperature.get(i).getVal());
            airQualityDate.add(airQuality.get(i).getVal());
            integers.add(humidity.get(i).getDate());
        }
        dateValue.setHumidity(humidityDate);
        dateValue.setTemperature(temperatureDate);
        dateValue.setAirQuality(airQualityDate);
        dateValue.setDates(integers);

        return dateValue;
    }

    public DateValue getDateLastYear(String name) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -1);


        Date dateC = c.getTime();
        System.out.println(dateC.toString());

        List<GraphRequest> humidity = humidityRepository.getHumidityForYear(name, dateC);
        List<GraphRequest> temperature = temperatureRepository.getTemperatureForYear(name, dateC);
        List<GraphRequest> airQuality = airQualityRepository.getAirQualityForYear(name, dateC);
        DateValue dateValue = new DateValue();

        List<Float> humidityDate = new ArrayList<>();
        List<Float> temperatureDate = new ArrayList<>();
        List<Float> airQualityDate = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < humidity.size(); i++) {
            humidityDate.add(humidity.get(i).getVal());
            temperatureDate.add(temperature.get(i).getVal());
            airQualityDate.add(airQuality.get(i).getVal());
            integers.add(humidity.get(i).getMonth());
        }
        dateValue.setHumidity(humidityDate);
        dateValue.setTemperature(temperatureDate);
        dateValue.setAirQuality(airQualityDate);
        dateValue.setMonth(integers);

        return dateValue;
    }



}
