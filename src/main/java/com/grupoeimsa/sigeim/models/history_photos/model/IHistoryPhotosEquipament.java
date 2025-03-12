package com.grupoeimsa.sigeim.models.history_photos.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IHistoryPhotosEquipament extends JpaRepository<BeanHistoryPhotosEquipament, Integer> {
    @Query("SELECT h FROM BeanHistoryPhotosEquipament h WHERE h.computerEquipament.computerEquipamentId = :equipmentId")
    List<BeanHistoryPhotosEquipament> findByComputerEquipamentId(@Param("equipmentId") Long equipmentId);

}
