package com.grupoeimsa.sigeim.models.users.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.tickets.model.BeanTicket;
import com.grupoeimsa.sigeim.models.tickets.model.BeanTicketMessage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;


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

    @OneToMany(mappedBy = "asignedTo", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference("user-tickets")
    private List<BeanTicket> tickets;

    @OneToMany(mappedBy = "userAuthor", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<BeanTicketMessage> ticketMessages;
}
