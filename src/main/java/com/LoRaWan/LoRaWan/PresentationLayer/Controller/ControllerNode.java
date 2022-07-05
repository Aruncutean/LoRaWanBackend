package com.LoRaWan.LoRaWan.PresentationLayer.Controller;

import com.LoRaWan.LoRaWan.BusinessLayer.Exception.ExceptionNodNotFound;
import com.LoRaWan.LoRaWan.BusinessLayer.Service.NodeService;
import com.LoRaWan.LoRaWan.PresentationLayer.Dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    @GetMapping("/deleteNode/{name}")
    public void deleteNode(@PathVariable(value = "name") String name, HttpServletResponse response) {
        try {
            nodeService.deleteNode(name);
        } catch (ExceptionNodNotFound exceptionNodNotFound) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @CrossOrigin
    @GetMapping("/getAllNode")
    public List<NodeDto> getAllNode() {
        return nodeService.getAllNode();
    }

    @CrossOrigin
    @GetMapping("/getNode/{name}")
    public NodeDto getNode(@PathVariable(value = "name") String name, HttpServletResponse response) {
        NodeDto nodeDto = null;
        try {
            nodeDto = nodeService.getNode(name);
        } catch (ExceptionNodNotFound e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return nodeDto;
    }

    @CrossOrigin
    @GetMapping("/getData/{name}")
    public DataSenzorDto getData(@PathVariable(value = "name") String name, HttpServletResponse response) {
        DataSenzorDto dataSenzorDto = null;
        try {
            dataSenzorDto = nodeService.sendData(name);
        } catch (ExceptionNodNotFound e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        return dataSenzorDto;
    }

    @CrossOrigin
    @GetMapping("/getNodeByUser/{email}")
    public List<NodeByUserDto> getNodeByUser(@PathVariable(value = "email") String email, HttpServletResponse response) {
        List<NodeByUserDto> list = null;
        try {
            list = nodeService.getNodeByUser(email);
        } catch (ExceptionNodNotFound e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return list;
    }

    @CrossOrigin
    @GetMapping("getMyStation/{email}")
    public MyStationDto getMyStationDto(@PathVariable(value = "email") String email, HttpServletResponse response) {
        MyStationDto myStationDto = null;

        try {
            myStationDto = nodeService.getMyStation(email);
        } catch (ExceptionNodNotFound e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return myStationDto;
    }

    @CrossOrigin
    @GetMapping("getPayload/{name}/{limSup}/{limInf}")
    public List<MessagePayloadDto> getPayload(@PathVariable(value = "name") String name,
                                              @PathVariable(value = "limSup") int limSup,
                                              @PathVariable(value = "limInf") int limInf,
                                              HttpServletResponse response
    ) {
        List<MessagePayloadDto> list = null;
        try {
            list = nodeService.getNodeByName(name, limSup, limInf);
        } catch (ExceptionNodNotFound e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return list;
    }

    @CrossOrigin
    @PostMapping("/newLocation")
    public MessageDTO newLocation(@RequestBody NewLocationDto newLocationDto, HttpServletResponse response) {
        try {
            nodeService.addNewLocation(newLocationDto);
        } catch (Exception e) {
            return new MessageDTO(e.getMessage().toString());
        }
        return new MessageDTO("Is oki!!");
    }

    @CrossOrigin
    @GetMapping("/newLocation/{email}")
    public List<NewLocationDto> getNewLocation(@PathVariable(value = "email") String email, HttpServletResponse response) {
        List<NewLocationDto> list = null;
        try {
            list = nodeService.selectAllNewLocation(email);
        } catch (ExceptionNodNotFound e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return list;

    }

    @CrossOrigin
    @GetMapping("/getNodeName/{email}")
    public List<String> getNodeName(@PathVariable(value = "email") String email, HttpServletResponse response) {
        List<String> list = null;
        try {
            list = nodeService.getNodeNameListByUser(email);
        } catch (ExceptionNodNotFound e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return list;
    }

    @CrossOrigin
    @GetMapping("/getLastMessage/{name}")
    public List<String> getLastMessage(@PathVariable(value = "name") String name, HttpServletResponse response) {
        List<String> list=null;
        try {
            return nodeService.last10Message(name);
        } catch (ExceptionNodNotFound e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return list;
    }


}
