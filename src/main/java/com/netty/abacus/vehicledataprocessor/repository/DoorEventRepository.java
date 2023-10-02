package com.netty.abacus.vehicledataprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.netty.abacus.vehicledataprocessor.model.DoorOpenEvent;

@Repository
public interface DoorEventRepository extends JpaRepository<DoorOpenEvent, Long> {
} 