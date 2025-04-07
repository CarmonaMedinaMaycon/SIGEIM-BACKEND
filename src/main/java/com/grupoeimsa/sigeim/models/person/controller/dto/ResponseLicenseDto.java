package com.grupoeimsa.sigeim.models.person.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLicenseDto {
    // Office
    private boolean outlook;
    private String accountOutlook;
    private String typeOutlook;
    private String alias;
    private String mailbox;
    private String commentsOutlook;
    private String phoneNumber;
    private String twoFactorAuthenticationName;

    // CRM
    private boolean crm;
    private String userCrm;
    private String typeCrm;
    private String commentsCrm;

    // Business Central
    private boolean bc;
    private String userBc;
    private String idUserBc;
    private String typeBc;
    private String enterpriseBc;

    // PureCloud
    private boolean purecloud;
    private String userPureCloud;
    private String idUserPureCloud;

    // RPA
    private boolean rpa;
    private String userRpa;
    private String moduleRpa;
    private String enterpriseRpa;

    // Herramientas adicionales
    private boolean tactical;

    // Redes Sociales
    private boolean instagram;
    private String userInstagram;
    private boolean facebook;
    private String userFacebook;
    private boolean tiktok;
    private String userTiktok;
    private boolean linkedin;
    private String userLinkedin;
    private boolean youtube;
    private String userYoutube;

    // Herramientas digitales
    private boolean adobe;
    private boolean mailchimp;
    private boolean linktree;

    // E-commerce
    private boolean magento;
    private String magentoUser;
    private boolean shopify;
    private String userShopify;
    private boolean mercadoLibre;
    private boolean amazon;
    private boolean conekta;
    private boolean openPay;
    private boolean kuesky;

    // Autenticación (info adicional)
    private String authPhoneNumber;
    private String authTwoFactorAuthenticationName;
    private String authDepartament;
}