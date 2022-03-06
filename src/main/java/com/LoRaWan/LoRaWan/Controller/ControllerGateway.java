package com.LoRaWan.LoRaWan.Controller;

import com.LoRaWan.LoRaWan.Date.Node;
import com.LoRaWan.LoRaWan.MessageGateway.MessageGateway;
import com.LoRaWan.LoRaWan.Service.MessageService;
import com.LoRaWan.LoRaWan.Service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/message")

public class ControllerGateway {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/addMessage")
    public String addMessage(@RequestBody MessageGateway messageGateway) {
        Node node = nodeService.getNodeByDevEui(messageGateway.getMetadata().getNetwork().getLora().getDevEUI());
        if (node != null) {
            messageService.addMessage(messageGateway);
        }
        return "Da";
    }
}
