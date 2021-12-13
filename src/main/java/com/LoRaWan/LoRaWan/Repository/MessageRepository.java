package com.LoRaWan.LoRaWan.Repository;

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
    @Query(value = "select * from message   where node_id=(select id from node where name=:Name) ORDER BY id DESC LIMIT 10;", nativeQuery = true)
    public List<Message> getLast10Message(@Param("Name") String name);

}
