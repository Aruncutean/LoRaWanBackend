package com.LoRaWan.LoRaWan.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

public interface GraphRequest {


    public Date getDate();
    public Integer getMonth();
    public Float getVal();



}
