package com.LoRaWan.LoRaWan.Dto;

import lombok.Data;

@Data
public class MessageDTO {


    String message;

    public MessageDTO(String message) {
        this.message = message;
    }

}
