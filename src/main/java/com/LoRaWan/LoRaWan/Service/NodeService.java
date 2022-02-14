package com.LoRaWan.LoRaWan.Service;

import com.LoRaWan.LoRaWan.Date.*;
import com.LoRaWan.LoRaWan.Dto.*;
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

    public List<Node> getNodesByDevEui(String devEui) {
        return nodeRepository.getNodesByDevEui(devEui);
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

            nodeDtos.add(new NodeDto(node.getName(), node.getDevEui(), node.getAppEui(), node.getLocation().getLatitude(),
                    node.getLocation().getLongitude()));
        }

        return nodeDtos;
    }

    public NodeDto getNode(String name) {
        Node node = nodeRepository.getNodeByName(name);
        return new NodeDto(node.getName(),node.getDevEui(),node.getAppEui(),node.getLocation().getLatitude(),node.getLocation().getLongitude());
    }

    public DataSenzorDto sendData(String name) {
        List<Float> temperature = nodeRepository.getTemperatureByNameNode(name);
        List<Float> humidity = nodeRepository.getHumidityByNameNode(name);
        List<Float> airQuality = nodeRepository.getAirQualityByNameNode(name);


        return new DataSenzorDto(humidity, temperature, airQuality);
    }


    public List<NodeByUserDto> getNodeByUser(String email) {
        List<NodeByUserDto> nodes = new ArrayList<>();

        List<Node> nodeList = nodeRepository.getNodeByUser(email);

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

        return nodes;
    }

    public MyStationDto getMyStation(String email) {
        MyStationDto myStationDto = new MyStationDto();

        myStationDto.setNumberOfStation(nodeRepository.getNodeByUser(email).size());
        myStationDto.setStationActive(nodeRepository.getNodeByStationActive(email).size());
        myStationDto.setStationInactive(nodeRepository.getNodeByStationInActive(email).size());

        List<Float> humidity = nodeRepository.getHumidityByAllNode(email);
        myStationDto.setHumidity((int) (sumList(humidity) / humidity.size()));
        List<Float> temperature = nodeRepository.getTemperatureByAllNode(email);
        myStationDto.setTemperature((int) (sumList(temperature) / temperature.size()));
        List<Float> airQuality = nodeRepository.getAirQualityByAllNode(email);
        myStationDto.setAirQuality((int) (sumList(airQuality) / airQuality.size()));
        List<Float> batteryPower = nodeRepository.getBatteryPowerNode(email);
        myStationDto.setBatteryLeve((int) (sumList(batteryPower) / batteryPower.size()));
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
    public List<MessagePayloadDto> getNodeByName(String name,int limSup,int limInf) {

        List<AcquisitionDate> messages = messageRepository.getPayloadByName(name,limSup,limInf);
        List<MessagePayloadDto> messagePayloadDtos = new ArrayList<>();
        for(AcquisitionDate date:messages)
        {
            MessagePayloadDto messagePayloadDto=new MessagePayloadDto();
            messagePayloadDto.setAirQuality(date.getAirQ());
            messagePayloadDto.setHumidity(date.getHum());
            messagePayloadDto.setTemperature(date.getTemp());
            messagePayloadDto.setDate(date.getDate());
            messagePayloadDtos.add(messagePayloadDto);
        }


        return messagePayloadDtos;
    }

    public String deleteNode(String name) {

        Node node = nodeRepository.getNodeByName(name);
        nodeRepository.foreignKey(0);

        List<Message> messages = node.getMessages();


        for (Message message : messages) {
            payloadRepository.delete(message.getPayload());
            gatewayRepository.delete(message.getGateway());

            messageRepository.delete(message);
        }
        nodeRepository.delete(node);

        return "da";
    }


    public void addNewLocation(NewLocationDto newLocationDto) throws Exception {

        Node node = nodeRepository.getNodeByName(newLocationDto.getName());

        if (node != null) {
            NewLocation newLocation = new NewLocation();
            newLocation.setNode(node);
            newLocation.setLongitude(newLocationDto.getLog());
            newLocation.setLatitude(newLocationDto.getLat());

            newLocation.setRssi(newLocationDto.getRssi());

            newLocationRepository.save(newLocation);
        } else {
            throw new Exception("Nu exista nod!!!");
        }

    }

    public List<NewLocationDto> selectAllNewLocation(String email) {

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
        }
        return list;
    }


    public DateValue getDateLast7Day(String name) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -17);

        Date dateC = c.getTime();

        List<GraphRequest> humidity = nodeRepository.getHumidityForDate(name, dateC);
        List<GraphRequest> temperature = nodeRepository.getTemperatureForDate(name, dateC);
        List<GraphRequest> airQuality = nodeRepository.getAirQualityForDate(name, dateC);
        DateValue dateValue = new DateValue();
        List<Float> humidityDate = new ArrayList<>();
        List<Float> temperatureDate = new ArrayList<>();
        List<Float> airQualityDate = new ArrayList<>();
        List<Date> integers = new ArrayList<>();
        for (int i = 0; i < humidity.size(); i++) {
            humidityDate.add(humidity.get(i).getVal());
            temperatureDate.add(temperature.get(i).getVal());
            airQualityDate.add(airQuality.get(i).getVal());
            integers.add(humidity.get(i).getDate());
        }
        dateValue.setHumidity(humidityDate);
        dateValue.setTemperature(temperatureDate);
        dateValue.setAirQuality(airQualityDate);
        dateValue.setDates(integers);

        return dateValue;
    }

    public DateValue getDateLastMount(String name) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -1);

        Date dateC = c.getTime();
        System.out.println(dateC.toString());
        System.out.println(name);
        List<GraphRequest> humidity = nodeRepository.getHumidityForDate(name, dateC);
        List<GraphRequest> temperature = nodeRepository.getTemperatureForDate(name, dateC);
        List<GraphRequest> airQuality = nodeRepository.getAirQualityForDate(name, dateC);
        DateValue dateValue = new DateValue();

        List<Float> humidityDate = new ArrayList<>();
        List<Float> temperatureDate = new ArrayList<>();
        List<Float> airQualityDate = new ArrayList<>();
        List<Date> integers = new ArrayList<>();
        for (int i = 0; i < humidity.size(); i++) {
            humidityDate.add(humidity.get(i).getVal());
            temperatureDate.add(temperature.get(i).getVal());
            airQualityDate.add(airQuality.get(i).getVal());
            integers.add(humidity.get(i).getDate());
        }
        dateValue.setHumidity(humidityDate);
        dateValue.setTemperature(temperatureDate);
        dateValue.setAirQuality(airQualityDate);
        dateValue.setDates(integers);

        return dateValue;
    }

    public DateValue getDateLastYear(String name) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, -1);


        Date dateC = c.getTime();
        System.out.println(dateC.toString());

        List<GraphRequest> humidity = nodeRepository.getHumidityForYear(name, dateC);
        List<GraphRequest> temperature = nodeRepository.getTemperatureForYear(name, dateC);
        List<GraphRequest> airQuality = nodeRepository.getAirQualityForYear(name, dateC);
        DateValue dateValue = new DateValue();

        List<Float> humidityDate = new ArrayList<>();
        List<Float> temperatureDate = new ArrayList<>();
        List<Float> airQualityDate = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < humidity.size(); i++) {
            humidityDate.add(humidity.get(i).getVal());
            temperatureDate.add(temperature.get(i).getVal());
            airQualityDate.add(airQuality.get(i).getVal());
            integers.add(humidity.get(i).getMonth());
        }
        dateValue.setHumidity(humidityDate);
        dateValue.setTemperature(temperatureDate);
        dateValue.setAirQuality(airQualityDate);
        dateValue.setMonth(integers);

        return dateValue;
    }


    public List<String> getNodeNameListByUser(String email) {
        List<Node> nodeList = nodeRepository.getNodeByUser(email);
        List<String> nodeNameList = new ArrayList<>();

        for (Node node : nodeList) {
            nodeNameList.add(node.getName());
        }

        return nodeNameList;
    }

    public List<String> last10Message(String name) {
        List<Message> messageList = messageRepository.getLast10Message(name);

        List<String> messageStringList = new ArrayList<>();
        for (Message message : messageList) {

            message.getGateway().getRssi();
            String mes = message.getPayload().getRecvPayload().toGMTString().toString() + " rssi:" + message.getGateway().getRssi();
            messageStringList.add(mes);
        }
        return messageStringList;
    }


}
