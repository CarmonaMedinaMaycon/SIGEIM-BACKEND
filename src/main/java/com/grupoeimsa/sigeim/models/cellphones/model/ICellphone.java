package com.grupoeimsa.sigeim.models.cellphones.model;

import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ICellphone extends JpaRepository<BeanCellphone, Long>, JpaSpecificationExecutor<BeanCellphone> {

    Optional<BeanCellphone> findById(Long id);

    @Query("""
        SELECT c FROM BeanCellphone c
        LEFT JOIN c.responsiveCellphones r
        WHERE r IS NULL OR r.status = com.grupoeimsa.sigeim.models.responsives.model.EStatus.CANCELADA
    """)
    List<BeanCellphone> findAvailableForResponsiva();

    Optional<BeanCellphone> findByPersonPersonId(Long personId);

}
