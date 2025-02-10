package com.grupoeimsa.sigeim.models.computing_equipaments.model;

import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveEquipaments;
import com.grupoeimsa.sigeim.models.responsives.model.EStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long idEsset;
    
    @Column(name="responsible", nullable = false)
    private String responsible;

    @Column(name = "departament", nullable = false)
    private String departament;

    @Column(name = "enterprise", nullable = false)
    private String enterprise;

    @Column(name = "mode", nullable = false)
    private String mode;

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
    private EStatus status;

    @Column(name = "system_observations", nullable = false)
    private String systemObservations;

    @OneToMany(mappedBy = "computerEquipament", fetch = FetchType.LAZY)
    private List<BeanResponsiveEquipaments> responsiveEquipaments;

}
