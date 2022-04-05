package com.LoRaWan.LoRaWan.Repository;

import com.LoRaWan.LoRaWan.Date.GraphRequest;
import com.LoRaWan.LoRaWan.Date.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Integer> {

    @Transactional
    @Query(value = "select status.battery_power from node,status where node.id=status.node_id and node.user_id=(select id from user where email=:Email);", nativeQuery = true)
    public List<Float> getBatteryPowerNode(@Param("Email") String email);
}
