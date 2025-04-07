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

    @Column(name = "responsible_name", nullable = false)
    private String responsibleName;

    @Column(name = "responsible_departament", nullable = false)
    private String responsibleDepartament;

    @Column(name = "responsible_position", nullable = false)
    private String responsiblePosition;

    @Column(name = "branch", nullable = false)
    private String branch;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "observations", nullable = false)
    private String observations;

    @Column(name = "who_gives", nullable = false)
    private String whoGives;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "modification_date")
    private LocalDate modificationDate;

    @Lob
    @Column(name = "equipaments", nullable = false, columnDefinition = "TEXT")
    private String equipaments;

    @Column(name = "status", nullable = false)
    private EStatus status;

    @Lob
    @Column(name = "generated_doc", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] generatedDoc;

    @Lob
    @Column(name = "signed_doc", columnDefinition = "LONGBLOB")
    private byte[] signedDoc;

    //Many-to-one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "computer_equipament_id")
    private BeanComputerEquipament computerEquipament;

}
