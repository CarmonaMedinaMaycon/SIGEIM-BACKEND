package com.grupoeimsa.sigeim.models.computing_equipaments.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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
}