package com.grupoeimsa.sigeim.models.invoices.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private double priceIva;

    @Column(name = "supplier", nullable = false)
    private String supplier;

    @Column(name = "invoice_date", nullable = false)
    private String invoiceDate;

    @Column(name = "invoice_folio", nullable = false)
    private String invoiceFolio;

    @Lob
    @Column(name = "invoice_file", columnDefinition = "LONGBLOB")
    private byte[] invoiceFile; // Se almacena el archivo PDF en formato binario

    @OneToOne(mappedBy = "invoice", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonBackReference
    private BeanComputerEquipament computerEquipament;

}
