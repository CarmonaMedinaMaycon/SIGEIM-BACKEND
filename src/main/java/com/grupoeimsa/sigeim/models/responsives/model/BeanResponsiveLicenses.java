package com.grupoeimsa.sigeim.models.responsives.model;

import com.grupoeimsa.sigeim.models.licenses.model.BeanLicense;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "responsives_licenses")
public class BeanResponsiveLicenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "responsive_licenses_id", updatable = false, nullable = false)
    private Long responsiveCellphoneId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "status", nullable = false)
    private EStatus status;

    @Column(name = "generated_doc", nullable = false)
    private String generatedDoc;

    @OneToOne
    @JoinColumn(name = "licenses_id", nullable = false)
    private BeanLicense license;

}
