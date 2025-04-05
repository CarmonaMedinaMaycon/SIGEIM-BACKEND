package com.grupoeimsa.sigeim.models.responsives.model;

import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
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


    @Column(name = "date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "modification_date")
    private LocalDate modificationDate;

    @Column(name = "responsible_name", nullable = false)
    private String responsibleName;

    @Column(name = "responsible_position", nullable = false)
    private String responsiblePosition;

    @Column(name = "whats_given", nullable = false)
    private String whatsGiven;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "imei", nullable = false)
    private String imei;

    @Column(name = "state", nullable = false)
    private String phoneState;

    @Column(name = "status", nullable = false)
    private EStatus status;

    @Lob
    @Column(name = "uploaded_doc", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] uploadedDoc;

    @Lob
    @Column(name = "signed_doc", columnDefinition = "LONGBLOB")
    private byte[] signedDoc;

    //Many-to-one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cellphone_id")
    private BeanCellphone cellphone;
}
