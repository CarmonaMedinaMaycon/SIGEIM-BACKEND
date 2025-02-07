package com.grupoeimsa.sigeim.models.person.model;

import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "status", nullable = false)
    private Boolean status;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private BeanUser user;


}
