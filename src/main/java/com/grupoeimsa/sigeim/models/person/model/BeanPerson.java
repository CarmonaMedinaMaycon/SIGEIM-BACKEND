package com.grupoeimsa.sigeim.models.person.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.grupoeimsa.sigeim.models.acess_cards.model.BeanAccessCard;
import com.grupoeimsa.sigeim.models.assets.model.BeanAssets;
import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.licenses.model.BeanLicense;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
    @Column(name = "who_registered")
    private String whoRegistered;
    @Column(name = "email_registered")
    private String emailRegistered;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name="departament", nullable=false)
    private String departament;
    @Column(name="enterprise", nullable=false)
    private String enterprise;
    @Column(name = "position", nullable = false)
    private String position;
    @Column(name="comments", nullable=false)
    private String comments;
    @Column(name="dateStart", nullable=false)
    private LocalDate dateStart ;
    @Column(name="comments_hardware_software", nullable=false)
    private String commentsHardwareSoftware;
    @Column(name="comments_email", nullable=false)
    private String commentsEmail;
    @Column(name="dateEnd", nullable=false)
    private LocalDate dateEnd ;
    @Column(name="entry_date", nullable=false)
    private LocalDate entryDate ;
    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("person-cellphone")
    private List<BeanCellphone> cellphone;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "person-equipment")
    private List<BeanComputerEquipament> computerEquipaments;

    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference("person-license")
    private BeanLicense license;

    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference("person-accesscard")
    private BeanAccessCard accessCard;

    @OneToOne(mappedBy = "person", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private BeanAssets assets;

    public String getFullName() {
        return name + " " + lastname + (surname != null ? " " + surname : "");
    }
}
