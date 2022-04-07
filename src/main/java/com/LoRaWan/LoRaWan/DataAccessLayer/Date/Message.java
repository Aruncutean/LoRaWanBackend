package com.LoRaWan.LoRaWan.DataAccessLayer.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "node_id", referencedColumnName = "id")
    private Node node;


    @OneToOne(cascade = CascadeType.ALL ,orphanRemoval = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "gateway_id", referencedColumnName = "id")
    private Gateway gateway;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payload_id", referencedColumnName = "id")
    private Payload payload;



}
