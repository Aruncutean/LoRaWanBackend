package com.LoRaWan.LoRaWan.Repository;

import com.LoRaWan.LoRaWan.Date.AcquisitionDate;
import com.LoRaWan.LoRaWan.Date.Message;

import com.LoRaWan.LoRaWan.Date.Node;
import com.LoRaWan.LoRaWan.Date.Payload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {




    @Transactional
    @Query(value = "select * from message  where node_id=(select id from node where name=:Name);", nativeQuery = true)
    public List<Message> getPayloadByNameAi(@Param("Name") String name);

    @Transactional
    @Query(value = "select h.value as hum,a.value airQ,t.value as temp,p.recv_payload as date from message as m, payload as p, air_quality as a, humidity as h, temperature as t where  p.id=m.payload_id and p.air_quality=a.id and p.temperature=t.id and p.humidity=h.id and m.node_id=(select id from node where name=:Name) limit :LimInf,:LimSup", nativeQuery = true)
    public List<AcquisitionDate> getPayloadByName(@Param("Name") String name, @Param("LimSup") int limSup, @Param("LimInf") int limInf);



    @Transactional
    @Query(value = "select * from message   where node_id=(select id from node where name=:Name) ORDER BY id DESC LIMIT 10;", nativeQuery = true)
    public List<Message> getLast10Message(@Param("Name") String name);





}
