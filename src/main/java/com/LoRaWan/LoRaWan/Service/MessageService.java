package com.LoRaWan.LoRaWan.Service;

import com.LoRaWan.LoRaWan.Date.*;
import com.LoRaWan.LoRaWan.MessageGateway.MessageGateway;
import com.LoRaWan.LoRaWan.Repository.*;
import com.LoRaWan.LoRaWan.Util.Conversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private StatusRepository statusRepository;

    public void addMessage(MessageGateway messageGateway) {
        Message message = new Message();
        Node node = nodeRepository.getNodeByDevEui(messageGateway.getMetadata().getNetwork().getLora().getDevEUI());

        Gateway gateway = new Gateway();
        gateway.setKeyGateway(messageGateway.getMetadata().getNetwork().getLora().getBestGatewayId());
        gateway.setEsp(messageGateway.getMetadata().getNetwork().getLora().getEsp());
        gateway.setRssi(messageGateway.getMetadata().getNetwork().getLora().getRssi());
        gateway.setSf(messageGateway.getMetadata().getNetwork().getLora().getSf());
        gateway.setSignalLevel(messageGateway.getMetadata().getNetwork().getLora().getSignalLevel());
        gateway.setSnr(messageGateway.getMetadata().getNetwork().getLora().getSnr());


        Payload payload = new Payload();


        String messagePayload = Conversion.fromHexString(messageGateway.getValue().getPayload());

        String payloadString[] = messagePayload.split(";");


        Humidity humidity = new Humidity();
        humidity.setValue(Float.parseFloat(payloadString[0]));

        Temperature temperature = new Temperature();
        temperature.setValue(Float.parseFloat(payloadString[1]));

        AirQuality airQuality = new AirQuality();
        airQuality.setValue(Float.parseFloat(payloadString[2]));

        payload.setHumidity(humidity);
        payload.setAirQuality(airQuality);
        payload.setTemperature(temperature);

        message.setGateway(gateway);
        message.setPayload(payload);
        message.setNode(node);

        node.getStatus().setBatteryPower(Float.parseFloat(payloadString[3]));
        node.getStatus().setActive(true);

        messageRepository.save(message);

    }

    public void getHumidity() {
        List<Message> messages = messageRepository.findAll();
    }


}
