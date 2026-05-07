package com.joaze.estoqueapi.repository;

import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.MovementStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    Page<Movement> findByStatus(MovementStatus status, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Movement m WHERE m.id = :id")
    Optional<Movement> findByIdWithLock(@Param("id") Long id);
}
