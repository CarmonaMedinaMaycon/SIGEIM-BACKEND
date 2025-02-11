package com.grupoeimsa.sigeim.models.history_photos.model;


import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "history_photos_equipaments")
public class BeanHistoryPhotosEquipament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_equipament_id", updatable = false, nullable = false)
    private Long historyEquipamentId;

    @Column(name = "photo")
    private String photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "computer_equipament_id", nullable = false)
    private BeanComputerEquipament computerEquipament;


}
