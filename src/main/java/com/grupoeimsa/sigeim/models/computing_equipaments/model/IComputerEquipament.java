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
            "WHERE " +
            "(e.serialNumber LIKE %:serialNumber% OR :serialNumber IS NULL) AND " +
            "(e.idEsset LIKE %:idEsset% OR :idEsset IS NULL) AND " +
            "(CONCAT(p.lastname, ' ', p.surname, ' ', p.name) LIKE %:responsibleName% OR :responsibleName IS NULL) AND " +
            "(e.departament LIKE %:departament% OR :departament IS NULL) AND " +
            "(e.type LIKE %:type% OR :type IS NULL) AND " +
            "(e.brand LIKE %:brand% OR :brand IS NULL) AND " +
            "(e.status = :equipmentStatus OR :equipmentStatus IS NULL)")
    List<BeanComputerEquipament> findByFilters(String serialNumber, String idEsset, String responsibleName,
                                               String departament, String type, String brand, CEStatus equipmentStatus);

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

    Optional<BeanComputerEquipament> findById(Long id);

}