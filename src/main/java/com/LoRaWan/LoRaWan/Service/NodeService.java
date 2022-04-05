package com.LoRaWan.LoRaWan.Service;

import com.LoRaWan.LoRaWan.Date.*;
import com.LoRaWan.LoRaWan.Dto.*;
import com.LoRaWan.LoRaWan.Exception.ExceptionNodNotFound;
import com.LoRaWan.LoRaWan.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


import java.util.*;

@Service
public class NodeService {

    @Autowired
    private HumidityRepository humidityRepository;

    @Autowired
    private AirQualityRepository airQualityRepository;

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PayloadRepository payloadRepository;

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private NewLocationRepository newLocationRepository;

    public Node getNodeByDevEui(String devEui) {
        return nodeRepository.getNodeByDevEui(devEui);
    }

    public void saveNode(NodeDto nodeDto) throws Exception {

        Node nodeByName = nodeRepository.getNodeByName(nodeDto.getName());

        if (nodeByName == null) {
            Node nodeByAppEui = nodeRepository.getNodeByAppEui(nodeDto.getAppEui());
            System.out.println("Node name good!!");
            if (nodeByAppEui == null) {
                Node nodeByDevEui = nodeRepository.getNodeByDevEui(nodeDto.getDevEui());
                System.out.println("Node appEui good!!");
                if (nodeByDevEui == null) {
                    System.out.println("Node devEui good!!");
                    Node nodeSave = new Node();
                    nodeSave.setName(nodeDto.getName());
                    nodeSave.setAppEui(nodeDto.getAppEui());
                    nodeSave.setDevEui(nodeDto.getDevEui());
                    nodeSave.setUser(userRepository.getUserByEmail(nodeDto.getUserEmail()));
                    nodeSave.setLocation(new Location(nodeDto.getLog(), nodeDto.getLat()));

                    nodeRepository.save(nodeSave);
                    Status status = new Status();
                    status.setNode(nodeSave);
                    status.setActive(false);
                    status.setBatteryPower(0f);
                    statusRepository.save(status);

                } else {
                    throw new Exception("DEV_EUI_EXCEPTION");
                }
            } else {
                throw new Exception("APP_EUI_EXCEPTION");
            }
        } else {
            throw new Exception("NAME_EXCEPTION");
        }
    }

    public List<NodeDto> getAllNode() {
        List<NodeDto> nodeDtos = new ArrayList<>();
        List<Node> list = nodeRepository.findAll();

        for (Node node : list) {
            nodeDtos.add(new NodeDto(
                    node.getName(),
                    node.getDevEui(),
                    node.getAppEui(),
                    node.getLocation().getLatitude(),
                    node.getLocation().getLongitude())
            );
        }
        return nodeDtos;
    }

    public NodeDto getNode(String name) throws ExceptionNodNotFound {
        Node node = nodeRepository.getNodeByName(name);
        if (node == null) {
            throw new ExceptionNodNotFound("Get node not found!!");
        }
        return new NodeDto(node.getName(),
                node.getDevEui(),
                node.getAppEui(),
                node.getLocation().getLatitude(),
                node.getLocation().getLongitude()
        );
    }

    public DataSenzorDto sendData(String name) throws ExceptionNodNotFound {
        List<Float> temperature = temperatureRepository.getTemperatureByNameNode(name);
        List<Float> humidity = humidityRepository.getHumidityByNameNode(name);
        List<Float> airQuality = airQualityRepository.getAirQualityByNameNode(name);

        if (temperature == null || humidity == null || airQuality == null) {
            throw new ExceptionNodNotFound("Nod not found!!!");
        }

        return new DataSenzorDto(humidity, temperature, airQuality);
    }

    public List<NodeByUserDto> getNodeByUser(String email) throws ExceptionNodNotFound {
        List<NodeByUserDto> nodes = new ArrayList<>();

        List<Node> nodeList = nodeRepository.getNodeByUser(email);
        if (nodeList != null) {
            for (Node node : nodeList) {
                NodeByUserDto node1 = new NodeByUserDto();

                node1.setAppEui(node.getAppEui());
                node1.setDevEui(node.getDevEui());
                node1.setName(node.getName());
                node1.setBattery(node.getStatus().getBatteryPower().toString());
                if (node.getStatus().getBatteryPower() > 20) {
                    node1.setActive(true);
                } else {
                    node1.setActive(false);
                }
                nodes.add(node1);
            }
        } else {
            throw new ExceptionNodNotFound("Nod not found!!!");
        }

        return nodes;
    }

    public MyStationDto getMyStation(String email) throws ExceptionNodNotFound {
        MyStationDto myStationDto = new MyStationDto();
        if (nodeRepository.getNodeByUser(email) != null) {
            myStationDto.setNumberOfStation(nodeRepository.getNodeByUser(email).size());
            myStationDto.setStationActive(nodeRepository.getNodeByStationActive(email).size());
            myStationDto.setStationInactive(nodeRepository.getNodeByStationInActive(email).size());

            List<Float> humidity = humidityRepository.getHumidityByAllNode(email);
            myStationDto.setHumidity((int) (sumList(humidity) / humidity.size()));
            List<Float> temperature = temperatureRepository.getTemperatureByAllNode(email);
            myStationDto.setTemperature((int) (sumList(temperature) / temperature.size()));
            List<Float> airQuality = airQualityRepository.getAirQualityByAllNode(email);
            myStationDto.setAirQuality((int) (sumList(airQuality) / airQuality.size()));
            List<Float> batteryPower = statusRepository.getBatteryPowerNode(email);
            myStationDto.setBatteryLeve((int) (sumList(batteryPower) / batteryPower.size()));
        } else {
            throw new ExceptionNodNotFound("Nod not found!!!");
        }
        return myStationDto;
    }

    private float sumList(List<Float> list) {
        float sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum = sum + list.get(i);
        }
        return sum;

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<MessagePayloadDto> getNodeByName(String name, int limSup, int limInf) throws ExceptionNodNotFound {
        List<MessagePayloadDto> messagePayloadDtos = new ArrayList<>();
        if (nodeRepository.getNodeByName(name) != null) {
            List<AcquisitionDate> messages = messageRepository.getPayloadByName(name, limSup, limInf);

            for (AcquisitionDate date : messages) {
                MessagePayloadDto messagePayloadDto = new MessagePayloadDto();
                messagePayloadDto.setAirQuality(date.getAirQ());
                messagePayloadDto.setHumidity(date.getHum());
                messagePayloadDto.setTemperature(date.getTemp());
                messagePayloadDto.setDate(date.getDate());
                messagePayloadDtos.add(messagePayloadDto);
            }
        } else {
            throw new ExceptionNodNotFound("Nod not found!!!");
        }
        return messagePayloadDtos;
    }

    public void deleteNode(String name) throws ExceptionNodNotFound {
        Node node = nodeRepository.getNodeByName(name);
        nodeRepository.foreignKey(0);
        if (node != null) {
            try {
                List<Message> messages = node.getMessages();
                for (Message message : messages) {
                    payloadRepository.delete(message.getPayload());
                    gatewayRepository.delete(message.getGateway());
                    messageRepository.delete(message);
                }
            } catch (NullPointerException exception) {
                System.out.println("Nodul nu are mesaje");
            }
            nodeRepository.delete(node);
        } else {
            throw new ExceptionNodNotFound("Nod not found!!!");
        }
    }


    public void addNewLocation(NewLocationDto newLocationDto) throws ExceptionNodNotFound {

        Node node = nodeRepository.getNodeByName(newLocationDto.getName());

        if (node != null) {
            NewLocation newLocation = new NewLocation();
            newLocation.setNode(node);
            newLocation.setLongitude(newLocationDto.getLog());
            newLocation.setLatitude(newLocationDto.getLat());

            newLocation.setRssi(newLocationDto.getRssi());

            newLocationRepository.save(newLocation);
        } else {
            throw new ExceptionNodNotFound("Nod not found!!!");
        }

    }

    public List<NewLocationDto> selectAllNewLocation(String email) throws ExceptionNodNotFound {

        User user = userRepository.getUserByEmail(email);
        List<NewLocationDto> list = null;
        if (user != null) {
            list = new ArrayList<>();
            for (Node node : user.getNodeList()) {
                for (NewLocation location : node.getNewLocation()) {
                    NewLocationDto newLocationDto = new NewLocationDto();
                    newLocationDto.setName(node.getName());
                    newLocationDto.setRssi(location.getRssi());
                    newLocationDto.setLog(location.getLongitude());
                    newLocationDto.setLat(location.getLatitude());
                    list.add(newLocationDto);
                }
            }
        } else {
            throw new ExceptionNodNotFound("Nod not found!!!");
        }
        return list;
    }


    public List<String> getNodeNameListByUser(String email) throws ExceptionNodNotFound {
        List<Node> nodeList = nodeRepository.getNodeByUser(email);
        List<String> nodeNameList = new ArrayList<>();
        if (nodeList != null) {

            for (Node node : nodeList) {
                nodeNameList.add(node.getName());
            }
        } else {
            throw new ExceptionNodNotFound("Nod not found!!!");
        }
        return nodeNameList;
    }

    public List<String> last10Message(String name) throws ExceptionNodNotFound {
        List<Message> messageList = messageRepository.getLast10Message(name);
        List<String> messageStringList = new ArrayList<>();
        if (messageList != null) {
            for (Message message : messageList) {
                message.getGateway().getRssi();
                String mes = message.getPayload().getRecvPayload().toGMTString().toString() + " rssi:" + message.getGateway().getRssi();
                messageStringList.add(mes);
            }
        } else {
            throw new ExceptionNodNotFound("Nod not found!!!");
        }
        return messageStringList;
    }
}
