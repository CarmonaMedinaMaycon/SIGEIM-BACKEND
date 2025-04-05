package com.grupoeimsa.sigeim.models.cellphones.model;

import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ICellphone extends JpaRepository<BeanCellphone, Long> {
    @Query("SELECT c FROM BeanCellphone c JOIN c.person p WHERE " +
            "(:search IS NULL OR " +
            "c.legalName LIKE %:search% OR " +
            "c.company LIKE %:search% OR " +
            "CAST(c.shortDialing AS string) LIKE %:search% OR " +
            "c.imei LIKE %:search%) AND " +
            "(:departament IS NULL OR LOWER(p.departament) LIKE LOWER(CONCAT('%', :departament, '%'))) AND " +
            "(:enterprise IS NULL OR LOWER(p.enterprise) LIKE LOWER(CONCAT('%', :enterprise, '%'))) AND " +
            "(:status IS NULL OR c.status = :status)")
    Page<BeanCellphone> findAllBySearch(
            @Param("search") String search,
            @Param("departament") String departament,
            @Param("enterprise") String enterprise,
            @Param("status") Boolean status,
            Pageable pageable
    );

    Optional<BeanCellphone> findById(Long id);


}
