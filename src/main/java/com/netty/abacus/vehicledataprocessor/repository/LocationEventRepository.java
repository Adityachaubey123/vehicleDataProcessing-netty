package com.netty.abacus.vehicledataprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.netty.abacus.vehicledataprocessor.model.LocationEvent;

@Repository
public interface LocationEventRepository extends JpaRepository<LocationEvent, Long> {
} 