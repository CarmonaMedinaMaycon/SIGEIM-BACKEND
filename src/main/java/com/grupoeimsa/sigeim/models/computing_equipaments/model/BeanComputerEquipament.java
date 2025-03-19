package com.grupoeimsa.sigeim.models.computing_equipaments.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.grupoeimsa.sigeim.models.history_photos.model.BeanHistoryPhotosEquipament;
import com.grupoeimsa.sigeim.models.invoices.model.BeanInvoice;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveEquipaments;
import com.grupoeimsa.sigeim.models.responsives.model.EStatus;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "computing_equipament")
public class BeanComputerEquipament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "computer_equipament_id", updatable = false, nullable = false)
    private Long computerEquipamentId;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;
    
    @Column(name="id_esset", nullable = false)
    private String idEsset;

    @Column(name = "departament", nullable = false)
    private String departament;

    @Column(name = "enterprise", nullable = false)
    private String enterprise;

    @Column(name = "work_modality", nullable = false)
    private String workModality;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "ram_memory_capacity", nullable = false)
    private Long ramMemoryCapacity;

    @Column(name = "memory_capacity", nullable = false)
    private Long memoryCapacity;

    @Column(name = "processor", nullable = false)
    private String processor;

    @Column(name = "purchasing_company", nullable = false)
    private String purchasingCompany;

    @Column(name = "supplier", nullable = false)
    private String supplier;

    @Column(name = "status", nullable = false)
    private CEStatus status;

    @Column(name = "has_invoice", nullable = false)
    private Boolean hasInvoice;

    @Column(name = "invoice_folio", nullable = false)
    private Long invoiceFolio;

    @Column(name = "system_observations", nullable = false)
    private String systemObservations;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "last_update_date")
    private LocalDate lastUpdateDate;

    @Column(name = "asset_number", nullable = false)
    private String assetNumber;

    @Column(name = "price", nullable = false)
    private Double price;

    @OneToMany(mappedBy = "computerEquipament", fetch = FetchType.LAZY)
    private List<BeanResponsiveEquipaments> responsiveEquipaments;

    @OneToMany(mappedBy = "computerEquipament", fetch = FetchType.LAZY)
    private List<BeanHistoryPhotosEquipament> historyPhotosEquipament;

    @OneToOne
    @JoinColumn(name = "invoice_id")
    private BeanInvoice invoice;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    @JsonBackReference
    private BeanPerson person;
}
