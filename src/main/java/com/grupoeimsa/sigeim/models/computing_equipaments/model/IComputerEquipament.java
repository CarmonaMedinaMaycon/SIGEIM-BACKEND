package com.grupoeimsa.sigeim.models.computing_equipaments.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IComputerEquipament extends JpaRepository<BeanComputerEquipament, Long> {

    @Query("SELECT e FROM BeanComputerEquipament e " +
            "JOIN e.person p " +
            "WHERE (:searchQuery IS NULL OR :searchQuery = '' OR " +
            "LOWER(e.serialNumber) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.idEsset) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(CONCAT(p.lastname, ' ', p.surname, ' ', p.name)) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.departament) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.type) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(e.brand) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(CAST(e.status AS string)) LIKE LOWER(CONCAT('%', :searchQuery, '%')))")
    List<BeanComputerEquipament> findBySearchQuery(@Param("searchQuery") String searchQuery);

    @Query("SELECT DISTINCT e.type FROM BeanComputerEquipament e")
    List<String> findDistinctTypes();

    @Query("SELECT DISTINCT e.supplier FROM BeanComputerEquipament e")
    List<String> findDistinctSuppliers();

    @Query("SELECT DISTINCT e.brand FROM BeanComputerEquipament e")
    List<String> findDistinctBrands();

    @Query("SELECT e FROM BeanComputerEquipament e WHERE "
            + "(:type IS NULL OR e.type = :type) AND "
            + "(:supplier IS NULL OR e.supplier = :supplier) AND "
            + "(:brand IS NULL OR e.brand = :brand) AND "
            + "(:status IS NULL OR e.status = :status)")
    Page<BeanComputerEquipament> findByFilters(
            @Param("type") String type,
            @Param("supplier") String supplier,
            @Param("brand") String brand,
            @Param("status") CEStatus status,
            Pageable pageable
    );

    int countByInvoice_InvoiceId(Long invoiceId);

    Optional<BeanComputerEquipament> findById(Long id);

    List<BeanComputerEquipament> findByInvoiceIsNull();

}