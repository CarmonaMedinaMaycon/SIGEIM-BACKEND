package com.grupoeimsa.sigeim.models.responsives.model;

import com.grupoeimsa.sigeim.models.acess_cards.model.BeanAccessCard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "responsives_cards")
public class BeanResponsiveCards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "responsive_card_id", updatable = false, nullable = false)
    private Long responsiveCardId;

    @Column(name = "date", nullable = false)
    private LocalDate creationDate;

    @Column(name = "status", nullable = false)
    private EStatus status;

    @Lob
    @Column(name = "generated_doc", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] generatedDoc;

    @Lob
    @Column(name = "signed_doc", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] signedDoc;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private BeanAccessCard accessCard;
}
