package com.LoRaWan.LoRaWan.Dto;

import lombok.Data;

@Data
public class AuthenticationDTO {

    private String email;
    private String password;

    public AuthenticationDTO() {
    }

    public AuthenticationDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
