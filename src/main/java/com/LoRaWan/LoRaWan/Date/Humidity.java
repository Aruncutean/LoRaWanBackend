package com.LoRaWan.LoRaWan.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Humidity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Integer id;

    private Float value;

    @OneToOne(mappedBy = "humidity",orphanRemoval = true,fetch = FetchType.LAZY)
    private Payload payload;
}
