package com.grupoeimsa.sigeim.models.acess_cards.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "access_card")
public class BeanAccessCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_card_id", updatable = false, nullable = false)
    private Long accessCardId;

    @Column(name = "access_between_building", nullable = false)
    private boolean accessBetweenBuildings;

    @Column(name = "main_door", nullable = false)
    private boolean mainDoor;

    @Column(name = "access_technicalService", nullable = false)
    private boolean accessTechnicalService;

    @Column(name = "main_warehouse", nullable = false)
    private boolean mainWarehouse;

    @Column(name = "warehouse_basement", nullable = false)
    private boolean WarehouseBasement;

    @Column(name = "technical_service_warehouses", nullable = false)
    private boolean TechnicalServiceWarehouses;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    @JsonBackReference
    private BeanPerson person;
}
