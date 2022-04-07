package com.LoRaWan.LoRaWan.DataAccessLayer.Repository;

import com.LoRaWan.LoRaWan.DataAccessLayer.Date.NewLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewLocationRepository extends JpaRepository<NewLocation, Integer> {
}
