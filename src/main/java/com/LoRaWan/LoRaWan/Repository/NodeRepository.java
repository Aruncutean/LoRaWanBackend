package com.LoRaWan.LoRaWan.Repository;

import com.LoRaWan.LoRaWan.Date.GraphRequest;
import com.LoRaWan.LoRaWan.Date.Node;

import com.LoRaWan.LoRaWan.Date.User;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface NodeRepository extends JpaRepository<Node, Integer> {


    @Transactional
    @Query(value = " select humidity.value from humidity, payload, message, node\n" +
            "where humidity.payload=payload.id  and payload.id=message.payload_id and  message.node_id=(select node.id where  node.name=:Name) ", nativeQuery = true)
    public List<Float> getHumidityByNameNode(@Param("Name") String name);

    @Transactional
    @Query(value = " select temperature.value from temperature, payload, message, node\n" +
            "where temperature.payload=payload.id  and payload.id=message.payload_id and  message.node_id=(select node.id where   node.name=:Name) ", nativeQuery = true)
    public List<Float> getTemperatureByNameNode(@Param("Name") String name);

    @Transactional
    @Query(value = " select air_quality.value from air_quality, payload, message, node\n" +
            "where air_quality.payload=payload.id  and payload.id=message.payload_id and  message.node_id=(select node.id where   node.name=:Name) ", nativeQuery = true)
    public List<Float> getAirQualityByNameNode(@Param("Name") String name);

    @Transactional
    @Query(value = "select * from node where app_eui=:AppEui", nativeQuery = true)
    Node getNodeByAppEui(@Param("AppEui") String appEui);

    @Transactional
    @Query(value = "select * from node where dev_eui=:DevEui", nativeQuery = true)
    Node getNodeByDevEui(@Param("DevEui") String appEui);

    @Transactional
    @Query(value = "select * from node where dev_eui=:DevEui", nativeQuery = true)
    List<Node> getNodesByDevEui(@Param("DevEui") String appEui);

    @Transactional
    @Query(value = "select * from node where name=:Name", nativeQuery = true)
    public Node getNodeByName(@Param("Name") String name);

    @Transactional
    @Query(value = "select * from node where node.user_id=(select id from user where email=:Email);", nativeQuery = true)
    public List<Node> getNodeByUser(@Param("Email") String email);

    @Transactional
    @Query(value = "select * from node,status where node.id=status.node_id and status.battery_power>20 and node.user_id=(select id from user where email=:Email)", nativeQuery = true)
    public List<Node> getNodeByStationActive(@Param("Email") String email);

    @Transactional
    @Query(value = "select * from node,status where node.id=status.node_id and status.battery_power<20 and node.user_id=(select id from user where email=:Email)", nativeQuery = true)
    public List<Node> getNodeByStationInActive(@Param("Email") String email);


    @Transactional
    @Query(value = "select value from humidity as h, payload as p, message as m,node as n where h.id=p.humidity and p.id=m.payload_id and n.id=m.node_id and n.user_id=(select id from user where email=:Email);", nativeQuery = true)
    public List<Float> getHumidityByAllNode(@Param("Email") String email);

    @Transactional
    @Query(value = "select value from air_quality as a, payload as p, message as m,node as n where a.id=p.humidity and p.id=m.payload_id and n.id=m.node_id and n.user_id=(select id from user where  email=:Email);", nativeQuery = true)
    public List<Float> getAirQualityByAllNode(@Param("Email") String email);

    @Transactional
    @Query(value = "select value from temperature as t, payload as p, message as m,node as n where t.id=p.humidity and p.id=m.payload_id and n.id=m.node_id and n.user_id=(select id from user where email=:Email);", nativeQuery = true)
    public List<Float> getTemperatureByAllNode(@Param("Email") String email);

    @Transactional
    @Query(value = "select status.battery_power from node,status where node.id=status.node_id and node.user_id=(select id from user where email=:Email);", nativeQuery = true)
    public List<Float> getBatteryPowerNode(@Param("Email") String email);

    @Modifying
    @Transactional
    @Query(value = "SET FOREIGN_KEY_CHECKS=0", nativeQuery = true)
    public void foreignKey(@Param("Value") int key);

    @Modifying
    @Transactional
    @Query(value = "select DATE(payload.recv_payload) as date,AVG(h.value) as val from humidity as h, payload, message, node\n" +
            "where h.id=payload.humidity  AND payload.id=message.payload_id AND Date(payload.recv_payload)>:Date\n" +
            "AND message.node_id=(select node.id where  node.name=:Node)\n" +
            "group by DATE(payload.recv_payload);", nativeQuery = true)
    public List<GraphRequest> getHumidityForDate(@Param("Node") String node, @Param("Date") Date date);


    @Modifying
    @Transactional
    @Query(value = "select DATE(payload.recv_payload) as date,AVG(t.value) as val from temperature as t, payload, message, node\n" +
            "where t.id=payload.temperature  AND payload.id=message.payload_id  AND Date(payload.recv_payload)>:Date\n" +
            "AND message.node_id=(select node.id where  node.name=:Node)\n" +
            "group by DATE(payload.recv_payload);", nativeQuery = true)
    public List<GraphRequest> getTemperatureForDate(@Param("Node") String node, @Param("Date") Date date);


    @Modifying
    @Transactional
    @Query(value = "select DATE(payload.recv_payload) as date,AVG(a.value) as val from air_quality as a, payload, message, node\n" +
            "where a.id=payload.air_quality  AND payload.id=message.payload_id  AND Date(payload.recv_payload)>:Date\n" +
            "AND message.node_id=(select node.id where  node.name=:Node)\n" +
            "group by DATE(payload.recv_payload);", nativeQuery = true)
    public List<GraphRequest> getAirQualityForDate(@Param("Node") String node, @Param("Date") Date date);


    @Modifying
    @Transactional
    @Query(value = "select MONTH(payload.recv_payload) as month,AVG(h.value) as val from humidity as h, payload, message, node\n" +
            "where h.id=payload.humidity  and payload.id=message.payload_id  and Date(payload.recv_payload)>:Date\n" +
            "and message.node_id=(select node.id where  node.name=:Node)\n" +
            "group by MONTH(payload.recv_payload);", nativeQuery = true)
    public List<GraphRequest> getHumidityForYear(@Param("Node") String node, @Param("Date") Date date);


    @Modifying
    @Transactional
    @Query(value = "select MONTH(payload.recv_payload) as month,AVG(t.value) as val from temperature as t, payload, message, node\n" +
            "where t.id=payload.temperature  and payload.id=message.payload_id  and Date(payload.recv_payload)>:Date\n" +
            "and message.node_id=(select node.id where  node.name=:Node)\n" +
            "group by MONTH(payload.recv_payload);", nativeQuery = true)
    public List<GraphRequest> getTemperatureForYear(@Param("Node") String node, @Param("Date") Date date);


    @Modifying
    @Transactional
    @Query(value = "select MONTH(payload.recv_payload) as month,AVG(a.value) as val from air_quality as a, payload, message, node\n" +
            "where a.id=payload.air_quality  and payload.id=message.payload_id  and Date(payload.recv_payload)>:Date\n" +
            "and message.node_id=(select node.id where  node.name=:Node)\n" +
            "group by MONTH(payload.recv_payload);", nativeQuery = true)
    public List<GraphRequest> getAirQualityForYear(@Param("Node") String node, @Param("Date") Date date);


}
