package com.grupoeimsa.sigeim.models.tickets.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "ticket")
public class BeanTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id", updatable = false, nullable = false)
    private Long ticketId;

    @Column(name = "help_topics", nullable = false)
    private String helpTopics;

    @Column(name = "area", nullable = false)
    private String area;

    @Column(name = "problem_title", nullable = false)
    private String problemTitle;

    @Column(name = "problem_description", nullable = false)
    private String problemDescription;

    @Column(name = "status", nullable = false)
    private TStatus status;

    @Column(name = "date_created", nullable = false)
    private String dateCreated;

    @Column(name = "date_update", nullable = true)
    private String dateUpdate;

    @Column(name = "priority", nullable = false)
    private String priority;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    @JsonBackReference("person-tickets")
    private BeanPerson person;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonBackReference("user-tickets")
    private BeanUser asignedTo;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonBackReference("ticket-messages")
    private List<BeanTicketMessage> ticketMessages;

}
