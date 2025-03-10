package com.grupoeimsa.sigeim.models.history_photos.model;


import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Component
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

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "person_name", nullable = false)
    private String personName;

}
