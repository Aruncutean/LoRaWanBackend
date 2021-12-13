package com.LoRaWan.LoRaWan.MessageGateway;

import lombok.Data;

@Data
public class Metadata {

    private String source;
    private Group group;
    private String connector;
    private Network network;

}





