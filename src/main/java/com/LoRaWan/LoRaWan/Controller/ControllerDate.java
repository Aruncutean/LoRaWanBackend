package com.LoRaWan.LoRaWan.Controller;

import com.LoRaWan.LoRaWan.Dto.DateValue;
import com.LoRaWan.LoRaWan.Service.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@RequestMapping(path = "date")
public class ControllerDate {

    @Autowired
    private DateService dateService;

    @CrossOrigin
    @GetMapping("/getLast7Day/{name}")
    public DateValue getLast7Day(@PathVariable(value = "name") String name) {
        return dateService.getDateLast7Day(name);
    }

    @CrossOrigin
    @GetMapping("/getLastMonth/{name}")
    public DateValue getLastMonth(@PathVariable(value = "name") String name) {
        return dateService.getDateLastMount(name);
    }

    @CrossOrigin
    @GetMapping("/getLastYear/{name}")
    public DateValue getLastYear(@PathVariable(value = "name") String name, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        return dateService.getDateLastYear(name);
    }
}