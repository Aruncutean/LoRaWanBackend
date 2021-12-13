package com.LoRaWan.LoRaWan.MessageGateway;

import lombok.Data;

@Data
public class MessageGateway {
    private String id;
    private String streamId;
    private String timestamp;
    private String model;
    private Value value;
    private Metadata metadata;



}
