package com.LoRaWan.LoRaWan.Repository;

import com.LoRaWan.LoRaWan.Date.GraphRequest;
import com.LoRaWan.LoRaWan.Date.Humidity;
import com.LoRaWan.LoRaWan.Date.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface HumidityRepository extends JpaRepository<Humidity, Integer> {

    @Modifying
    @Transactional
    @Query(value = "select DATE(payload.recv_payload) as date,AVG(h.value) as val from humidity as h, payload, message, node\n" +
            "where h.id=payload.humidity  AND payload.id=message.payload_id AND Date(payload.recv_payload)>:Date\n" +
            "AND message.node_id=(select node.id where  node.name=:Node)\n" +
            "group by DATE(payload.recv_payload);", nativeQuery = true)
    public List<GraphRequest> getHumidityForDate(@Param("Node") String node, @Param("Date") Date date);

    @Transactional
    @Query(value = "select value from humidity as h, payload as p, message as m,node as n where h.id=p.humidity and p.id=m.payload_id and n.id=m.node_id and n.user_id=(select id from user where email=:Email);", nativeQuery = true)
    public List<Float> getHumidityByAllNode(@Param("Email") String email);

    @Modifying
    @Transactional
    @Query(value = "select MONTH(payload.recv_payload) as month,AVG(h.value) as val from humidity as h, payload, message, node\n" +
            "where h.id=payload.humidity  and payload.id=message.payload_id  and Date(payload.recv_payload)>:Date\n" +
            "and message.node_id=(select node.id where  node.name=:Node)\n" +
            "group by MONTH(payload.recv_payload);", nativeQuery = true)
    public List<GraphRequest> getHumidityForYear(@Param("Node") String node, @Param("Date") Date date);

    @Transactional
    @Query(value = " select humidity.value from humidity, payload, message, node\n" +
            "where humidity.payload=payload.id  and payload.id=message.payload_id and  message.node_id=(select node.id where  node.name=:Name) ", nativeQuery = true)
    public List<Float> getHumidityByNameNode(@Param("Name") String name);
}
