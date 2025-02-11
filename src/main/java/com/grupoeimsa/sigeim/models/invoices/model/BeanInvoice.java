package com.grupoeimsa.sigeim.models.invoices.model;


import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "invoices")
public class BeanInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="invoice_id", updatable = false, nullable = false)
    private int invoice_id;

    @Column(name = "price", nullable = false )
    private double price;

    @Column(name = "supplier", nullable = false)
    private String supplier;

    @Column(name = "purchase_date")
    private Date purchase_date;

    @Column(name = "recipients_rfc", nullable = false)
    private String recipients_rfc;

    @Column(name = "issuers_rfc", nullable = false)
    private String issuers_rfc;

    @Column(name = "payment_method", nullable = false)
    private String payment_method;

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "cfdi", nullable = false)
    private String cfdi;

    @OneToOne(mappedBy = "invoice", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private BeanComputerEquipament computerEquipament;

}
