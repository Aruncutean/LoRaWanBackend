package com.LoRaWan.LoRaWan.DataAccessLayer.Repository;

import com.LoRaWan.LoRaWan.DataAccessLayer.Date.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatewayRepository extends JpaRepository<Gateway, Integer> {
}
