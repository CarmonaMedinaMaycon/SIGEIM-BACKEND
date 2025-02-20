package com.grupoeimsa.sigeim.models.responsives.model;

import com.grupoeimsa.sigeim.models.licenses.model.BeanLicense;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


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


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name="departament", nullable=false)
    private String departament;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "description")
    private String description;

    @Column(name = "comments")
    private String comments;

    @Column(name = "delivery_signature", nullable = false)
    private String deliverySignature;

    @Column(name = "receiver_signature", nullable = false)
    private String receiverSignature;


    @Column(name = "status", nullable = false)
    private EStatus status;

    @Column(name = "generated_doc", nullable = false)
    private String generatedDoc;

    @OneToOne
    @JoinColumn(name = "licenses_id", nullable = false)
    private BeanLicense license;

}
