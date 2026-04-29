package com.joaze.estoqueapi.repository;

import com.joaze.estoqueapi.model.Movement;
import com.joaze.estoqueapi.model.MovementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    Page<Movement> findByStatus(MovementStatus status, Pageable pageable);
}
