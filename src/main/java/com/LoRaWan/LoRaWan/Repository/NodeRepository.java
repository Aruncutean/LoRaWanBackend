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

    @Modifying
    @Transactional
    @Query(value = "SET FOREIGN_KEY_CHECKS=0", nativeQuery = true)
    public void foreignKey(@Param("Value") int key);

}
