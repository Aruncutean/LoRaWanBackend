package com.LoRaWan.LoRaWan.DataAccessLayer.Repository;

import com.LoRaWan.LoRaWan.DataAccessLayer.Date.GraphRequest;
import com.LoRaWan.LoRaWan.DataAccessLayer.Date.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface TemperatureRepository extends JpaRepository<Temperature, Integer> {
    @Transactional
    @Query(value = " select temperature.value from temperature, payload, message, node\n" +
            "where temperature.payload=payload.id  and payload.id=message.payload_id and  message.node_id=(select node.id where   node.name=:Name) ", nativeQuery = true)
    public List<Float> getTemperatureByNameNode(@Param("Name") String name);


    @Transactional
    @Query(value = "select value from temperature as t, payload as p, message as m,node as n where t.id=p.humidity and p.id=m.payload_id and n.id=m.node_id and n.user_id=(select id from user where email=:Email);", nativeQuery = true)
    public List<Float> getTemperatureByAllNode(@Param("Email") String email);

    @Modifying
    @Transactional
    @Query(value = "select DATE(payload.recv_payload) as date,AVG(t.value) as val from temperature as t, payload, message, node\n" +
            "where t.id=payload.temperature  AND payload.id=message.payload_id  AND Date(payload.recv_payload)>:Date\n" +
            "AND message.node_id=(select node.id where  node.name=:Node)\n" +
            "group by DATE(payload.recv_payload);", nativeQuery = true)
    public List<GraphRequest> getTemperatureForDate(@Param("Node") String node, @Param("Date") Date date);

    @Modifying
    @Transactional
    @Query(value = "select MONTH(payload.recv_payload) as month,AVG(t.value) as val from temperature as t, payload, message, node\n" +
            "where t.id=payload.temperature  and payload.id=message.payload_id  and Date(payload.recv_payload)>:Date\n" +
            "and message.node_id=(select node.id where  node.name=:Node)\n" +
            "group by MONTH(payload.recv_payload);", nativeQuery = true)
    public List<GraphRequest> getTemperatureForYear(@Param("Node") String node, @Param("Date") Date date);
}
