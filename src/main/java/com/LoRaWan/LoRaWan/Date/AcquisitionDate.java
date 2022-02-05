package com.LoRaWan.LoRaWan.Date;

import java.util.Date;

public interface AcquisitionDate {
    Float getHum();
    Float getAirQ();
    Float getTemp();
    Date getDate();
}
