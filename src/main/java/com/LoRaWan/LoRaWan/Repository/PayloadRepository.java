package com.LoRaWan.LoRaWan.Repository;

import com.LoRaWan.LoRaWan.Date.Payload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PayloadRepository  extends JpaRepository<Payload, Integer> {





}
