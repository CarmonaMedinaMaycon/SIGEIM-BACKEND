package com.grupoeimsa.sigeim.models.responsives.model;

import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "responsives_equipaments")
public class BeanResponsiveEquipaments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "responsive_equipament_id", updatable = false, nullable = false)
    private Long responsiveEquipamentId;


    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "equipaments", nullable = false)
    private String equipaments;

    @Column(name = "status", nullable = false)
    private EStatus status;

    @Column(name = "generated_doc", nullable = false)
    private String generatedDoc;

    //Many-to-one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "computer_equipament_id")
    private BeanComputerEquipament computerEquipament;

}
