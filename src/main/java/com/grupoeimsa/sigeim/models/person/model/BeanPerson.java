package com.grupoeimsa.sigeim.models.person.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.grupoeimsa.sigeim.models.acess_cards.model.BeanAccessCard;
import com.grupoeimsa.sigeim.models.assets.model.BeanAssets;
import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.licenses.model.BeanLicenses;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "person")
public class BeanPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id", updatable = false, nullable = false)
    private Long personId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "surname", nullable = false)
    private String surname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "status", nullable = false)
    private Boolean status;

    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private BeanUser user;

    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private BeanCellphone cellphone;

    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private BeanComputerEquipament computerEquipament;

    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private BeanLicenses licenses;

    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private BeanAccessCard accessCard;

    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private BeanAssets assets;


}
