package com.grupoeimsa.sigeim.models.tickets.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "ticket_message")
public class BeanTicketMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    // Relación con ticket
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonBackReference("ticket-messages")
    private BeanTicket ticket;

    // Quién publicó el mensaje
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private BeanUser userAuthor;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = true)
    private BeanPerson personAuthor;

    // Opcional: campos para cambios de estado
    @Enumerated(EnumType.STRING)
    private TStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private TStatus newStatus;

    @Column(name = "is_system_action", nullable = false)
    private boolean systemAction = false;
}
