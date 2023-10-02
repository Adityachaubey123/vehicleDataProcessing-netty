package com.netty.abacus.vehicledataprocessor.repository;

import com.netty.abacus.vehicledataprocessor.model.SpeedAlertEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeedEventRepository extends JpaRepository<SpeedAlertEvent, Long> {
}
