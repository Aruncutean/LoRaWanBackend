package com.LoRaWan.LoRaWan.PresentationLayer.MessageGateway;

import lombok.Data;

@Data
public class Lora {

    private String devEUI;
    private Float port;
    private Float fcnt;
    private Float missingFcnt;
    private Float rssi;
    private Float snr;
    private Float esp;
    private Float sf;
    private Float frequency;
    private Float signalLevel;
    private Boolean ack;
    private String messageType;
    private Float gatewayCnt;
    private String bestGatewayId;

}
