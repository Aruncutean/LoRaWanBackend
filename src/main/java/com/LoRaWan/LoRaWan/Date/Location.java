package com.LoRaWan.LoRaWan.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class Location {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Integer id;

    private String longitude;
    private String latitude;

    public Location() {
    }

    public Location(String longitude, String latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @OneToOne(mappedBy = "location",orphanRemoval = true,fetch = FetchType.LAZY)
    private Node node;


}
