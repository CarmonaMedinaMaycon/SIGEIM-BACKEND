package com.grupoeimsa.sigeim.models.licenses.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "licenses")
public class BeanLicenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "licenses_id", updatable = false, nullable = false)
    private Long licensesId;

    @Column(name = "outlook", nullable = false)
    private boolean outlook;

    @Column(name = "account_outlook", nullable = false)
    private String accountOutlook;

    @Column(name = "type_outlook", nullable = false)
    private String typeOutlook;

    @Column(name = "alias", nullable = false)
    private String alias;

}
