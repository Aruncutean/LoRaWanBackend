package com.LoRaWan.LoRaWan.Repository;

import com.LoRaWan.LoRaWan.Date.Message;
import com.LoRaWan.LoRaWan.Date.NewLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewLocationRepository extends JpaRepository<NewLocation, Integer> {
}
