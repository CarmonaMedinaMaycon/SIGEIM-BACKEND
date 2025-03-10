package com.grupoeimsa.sigeim.models.users.model;

import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class BeanUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "attempts")
    private int attempts;

    @Column(name = "last_try")
    private LocalTime lastTry;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @Column(name = "status", nullable = false)
    private boolean status;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false)
    private BeanPerson person;
}
