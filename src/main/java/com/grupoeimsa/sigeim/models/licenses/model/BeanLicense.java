package com.grupoeimsa.sigeim.models.licenses.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveLicenses;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "licenses")
public class BeanLicense {

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

    @Column(name = "mailbox", nullable = false)
    private String mailbox;

    @Column(name = "comments_outlook", nullable = false)
    private String commentsOutlook;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Column(name = "two_factor_authentication_name", nullable = false)
    private String twoFactorAuthenticationName;

    @Column(name = "departament", nullable = false)
    private String departament;

    @Column(name = "crm", nullable = false)
    private boolean crm;

    @Column(name = "user_crm", nullable = false)
    private String userCrm;

    @Column(name = "type_crm", nullable = false)
    private String typeCrm;

    @Column(name = "comments_crm", nullable = false)
    private String commentsCrm;

    @Column(name = "bc", nullable = false)
    private boolean bc;

    @Column(name = "user_bc", nullable = false)
    private String userBc;

    @Column(name = "id_user_bc", nullable = false)
    private String idUserBc;

    @Column(name = "type_bc", nullable = false)
    private String typeBc;

    @Column(name = "enterprise_bc", nullable = false)
    private String enterpriseBc;

    @Column(name = "purecloud", nullable = false)
    private boolean purecloud;

    @Column(name = "user_purecloud", nullable = false)
    private String userPureCloud;

    @Column(name = "id_user_purecloud", nullable = false)
    private String idUserPureCloud;

    @Column(name = "rpa", nullable = false)
    private boolean rpa;

    @Column(name = "user_rpa", nullable = false)
    private String userRpa;

    @Column(name = "module_rpa", nullable = false)
    private String moduleRpa;

    @Column(name = "enterprise_rpa", nullable = false)
    private String enterpriseRpa;

    @Column(name = "tactical", nullable = false)
    private boolean tactical;

    @Column(name = "instagram", nullable = false)
    private boolean instagram;

    @Column(name = "user_instagram", nullable = false)
    private String userInstagram;

    @Column(name = "facebook", nullable = false)
    private boolean facebook;

    @Column(name = "user_facebook", nullable = false)
    private String userFacebook;

    @Column(name = "tiktok", nullable = false)
    private boolean tiktok;

    @Column(name = "user_tiktok", nullable = false)
    private String userTiktok;

    @Column(name = "linkedin", nullable = false)
    private boolean linkedin;

    @Column(name = "user_linkedin", nullable = false)
    private String userLinkedin;

    @Column(name = "youtube", nullable = false)
    private boolean youtube;

    @Column(name = "user_youtube", nullable = false)
    private String userYoutube;

    @Column(name = "adobe", nullable = false)
    private boolean adobe;

    @Column(name = "mailchimp", nullable = false)
    private boolean mailchimp;

    @Column(name = "linktree", nullable = false)
    private boolean linktree;

    @Column(name = "magento", nullable = false)
    private boolean magento;

    @Column(name = "magento_user", nullable = false)
    private String magentoUser;

    @Column(name = "shopify", nullable = false)
    private boolean shopify;

    @Column(name = "user_shopify", nullable = false)
    private String userShopify;

    @Column(name = "mercado_libre", nullable = false)
    private boolean mercadoLibre;

    @Column(name = "amazon", nullable = false)
    private boolean amazon;

    @Column(name = "conekta", nullable = false)
    private boolean conekta;

    @Column(name = "open_pay", nullable = false)
    private boolean openPay;

    @Column(name = "kuesky", nullable = false)
    private boolean kuesky;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false)
    @JsonBackReference("person-license")
    private BeanPerson person;

    @OneToOne(mappedBy = "license", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private BeanResponsiveLicenses responsiveLicenses;


}
