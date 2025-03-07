package com.grupoeimsa.sigeim.models.cellphones.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ICellphone extends JpaRepository<BeanCellphone, Long> {
    @Query("SELECT c FROM BeanCellphone c WHERE " +
            "(:search IS NULL OR " +
            "c.legalName LIKE %:search% OR " +
            "c.company LIKE %:search% OR " +
            "CAST(c.shortDialing AS string) LIKE %:search% OR " +
            "c.imei LIKE %:search%) AND " +
            "(:status IS NULL OR c.status = :status)")
    Page<BeanCellphone> findAllBySearch(
            @Param("search") String search,
            @Param("status") Boolean status,
            Pageable pageable
    );

}
