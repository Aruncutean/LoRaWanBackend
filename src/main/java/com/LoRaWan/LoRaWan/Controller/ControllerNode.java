package com.LoRaWan.LoRaWan.Controller;

import com.LoRaWan.LoRaWan.Date.Message;


import com.LoRaWan.LoRaWan.Dto.*;
import com.LoRaWan.LoRaWan.Service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "node")

public class ControllerNode {

    @Autowired
    private NodeService nodeService;

    @CrossOrigin
    @PostMapping("/addNewNode")
    public MessageDTO postAddNewNode(@RequestBody NodeDto nodeDto) {

        try {
            nodeService.saveNode(nodeDto);
        } catch (Exception e) {
            return new MessageDTO(e.getMessage().toString());
        }

        return new MessageDTO("Is OKI");
    }

    @CrossOrigin
    @DeleteMapping("/deleteNode/{name}")
    public Message deleteNode(@PathVariable(value = "name") String name) {

        nodeService.deleteNode(name);
        return new Message();
    }

    @CrossOrigin
    @GetMapping("/getAllNode")
    public List<NodeDto> getAllNode() {
        return nodeService.getAllNode();
    }

    @CrossOrigin
    @GetMapping("/getNode/{name}")
    public NodeDto getNode(@PathVariable(value = "name") String name) {
        return nodeService.getNode(name);
    }

    @CrossOrigin
    @GetMapping("/getData/{name}")
    public DataSenzorDto getData(@PathVariable(value = "name") String name) {
        return nodeService.sendData(name);
    }

    @CrossOrigin
    @GetMapping("/getNodeByUser/{email}")
    public List<NodeByUserDto> getNodeByUser(@PathVariable(value = "email") String email) {
        return nodeService.getNodeByUser(email);
    }

    @CrossOrigin
    @GetMapping("getMyStation/{email}")
    public MyStationDto getMyStationDto(@PathVariable(value = "email") String email) {
        return nodeService.getMyStation(email);
    }

    @CrossOrigin
    @GetMapping("getPayload/{name}/{limSup}/{limInf}")
    public List<MessagePayloadDto> getPayload(@PathVariable(value = "name") String name,
                                              @PathVariable(value = "limSup") int limSup,
                                              @PathVariable(value = "limInf") int limInf
    ) {

        return nodeService.getNodeByName(name, limSup, limInf);
    }

    @CrossOrigin
    @PostMapping("/newLocation")
    public MessageDTO newLocation(@RequestBody NewLocationDto newLocationDto) {


        try {
            nodeService.addNewLocation(newLocationDto);
        } catch (Exception e) {
            return new MessageDTO(e.getMessage().toString());
        }
        return new MessageDTO("Is oki!!");
    }

    @CrossOrigin
    @GetMapping("/newLocation/{email}")
    public List<NewLocationDto> getNewLocation(@PathVariable(value = "email") String email) {

        return nodeService.selectAllNewLocation(email);
    }

    @CrossOrigin
    @GetMapping("/getNodeName/{email}")
    public List<String> getNodeName(@PathVariable(value = "email") String email) {
        return nodeService.getNodeNameListByUser(email);
    }

    @CrossOrigin
    @GetMapping("/getLastMessage/{name}")
    public List<String> getLastMessage(@PathVariable(value = "name") String name) {
        return nodeService.last10Message(name);
    }

    @CrossOrigin
    @GetMapping("/getLast7Day/{name}")
    public DateValue getLast7Day(@PathVariable(value = "name") String name) {
        return nodeService.getDateLast7Day(name);
    }

    @CrossOrigin
    @GetMapping("/getLastMonth/{name}")
    public DateValue getLastMonth(@PathVariable(value = "name") String name) {
        return nodeService.getDateLastMount(name);
    }

    @CrossOrigin
    @GetMapping("/getLastYear/{name}")
    public DateValue getLastYear(@PathVariable(value = "name") String name) {
        return nodeService.getDateLastYear(name);
    }
}
