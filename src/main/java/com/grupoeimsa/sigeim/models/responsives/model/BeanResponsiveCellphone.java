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
    private LocalDate date;


    @Column(name = "status", nullable = false)
    private EStatus status;

    @Column(name = "uploaded_doc", nullable = false)
    private String uploadedDoc;

    //Many-to-one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cellphone_id")
    private BeanCellphone cellphone;
}
