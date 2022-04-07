package com.LoRaWan.LoRaWan.DataAccessLayer.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Gateway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Integer id;

    private String keyGateway;
    private float rssi;
    private float snr;
    private float esp;
    private float sf;
    private float signalLevel;

    @OneToOne(mappedBy = "gateway",orphanRemoval = true,fetch = FetchType.LAZY)
    private Message message;
}
