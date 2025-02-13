package com.grupoeimsa.sigeim.models.responsives.model;

import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "responsives_cellphone")
public class BeanResponsiveCellphone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "responsive_cellphone_id", updatable = false, nullable = false)
    private Long responsiveCellphoneId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name="departament", nullable=false)
    private String departament;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "description")
    private String description;

    @Column(name = "comments")
    private String comments;

    @Column(name = "delivery_signature", nullable = false)
    private String deliverySignature;

    @Column(name = "receiver_signature", nullable = false)
    private String receiverSignature;

    @Column(name = "equipaments", nullable = false)
    private String equipaments;

    @Column(name = "status", nullable = false)
    private EStatus status;

    @Column(name = "generated_doc", nullable = false)
    private String generatedDoc;

    //Many-to-one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cellphone_id")
    private BeanCellphone cellphone;
}
