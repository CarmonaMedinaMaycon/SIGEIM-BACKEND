package com.grupoeimsa.sigeim.models.acess_cards.model;


import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "responsives_cellphone")
public class BeanAccessCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_card_id", updatable = false, nullable = false)
    private Long accessCardId;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false)
    private BeanPerson person;
}
