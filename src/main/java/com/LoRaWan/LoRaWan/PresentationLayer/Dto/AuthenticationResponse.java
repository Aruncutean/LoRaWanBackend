package com.LoRaWan.LoRaWan.PresentationLayer.Dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String jwt;
    private final String message;

    public AuthenticationResponse(String jwt, String message) {
        this.jwt = jwt;
        this.message = message;
    }

}
