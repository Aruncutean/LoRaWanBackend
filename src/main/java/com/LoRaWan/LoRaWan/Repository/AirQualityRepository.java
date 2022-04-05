package com.LoRaWan.LoRaWan.Repository;

import com.LoRaWan.LoRaWan.Date.AirQuality;
import com.LoRaWan.LoRaWan.Date.GraphRequest;
import com.LoRaWan.LoRaWan.Date.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface AirQualityRepository extends JpaRepository<AirQuality, Integer> {
    @Modifying
    @Transactional
    @Query(value = "select MONTH(payload.recv_payload) as month,AVG(a.value) as val from air_quality as a, payload, message, node\n" +
            "where a.id=payload.air_quality  and payload.id=message.payload_id  and Date(payload.recv_payload)>:Date\n" +
            "and message.node_id=(select node.id where  node.name=:Node)\n" +
            "group by MONTH(payload.recv_payload);", nativeQuery = true)
    public List<GraphRequest> getAirQualityForYear(@Param("Node") String node, @Param("Date") Date date);

    @Modifying
    @Transactional
    @Query(value = "select DATE(payload.recv_payload) as date,AVG(a.value) as val from air_quality as a, payload, message, node\n" +
            "where a.id=payload.air_quality  AND payload.id=message.payload_id  AND Date(payload.recv_payload)>:Date\n" +
            "AND message.node_id=(select node.id where  node.name=:Node)\n" +
            "group by DATE(payload.recv_payload);", nativeQuery = true)
    public List<GraphRequest> getAirQualityForDate(@Param("Node") String node, @Param("Date") Date date);

    @Transactional
    @Query(value = "select value from air_quality as a, payload as p, message as m,node as n where a.id=p.humidity and p.id=m.payload_id and n.id=m.node_id and n.user_id=(select id from user where  email=:Email);", nativeQuery = true)
    public List<Float> getAirQualityByAllNode(@Param("Email") String email);

    @Transactional
    @Query(value = " select air_quality.value from air_quality, payload, message, node\n" +
            "where air_quality.payload=payload.id  and payload.id=message.payload_id and  message.node_id=(select node.id where   node.name=:Name) ", nativeQuery = true)
    public List<Float> getAirQualityByNameNode(@Param("Name") String name);
}
