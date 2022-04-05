package com.LoRaWan.LoRaWan.Controller;

import com.LoRaWan.LoRaWan.Date.Node;
import com.LoRaWan.LoRaWan.MessageGateway.MessageGateway;
import com.LoRaWan.LoRaWan.Service.MessageService;
import com.LoRaWan.LoRaWan.Service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    public void addMessage(@RequestBody MessageGateway messageGateway, HttpServletResponse response) {
        Node node = nodeService.getNodeByDevEui(messageGateway.getMetadata().getNetwork().getLora().getDevEUI());
        if (node != null) {
            messageService.addMessage(messageGateway);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
