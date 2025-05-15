package com.grupoeimsa.sigeim.models.licenses.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditLicenseDTO {
    private Long licenseId;

    private boolean outlook;

    private String accountOutlook;

    private String typeOutlook;

    private String supplierOutlook;

    private String aliasOutlook;

    private String mailboxOutlook;

    private String commentsOutlook;

    private Double importeOutlook;

    private String authPhoneNumber;

    private String authTwoFactorAuthenticationName;

    private String authDepartament;

    private boolean crm;

    private String userCrm;

    private String typeCrm;

    private String supplierCrm;

    private String commentsCrm;

    private Double importeCrm;

    private boolean bc;

    private String userBc;

    private String idUserBc;

    private String typeBc;

    private String supplierBc;

    private String enterpriseBc;

    private Double importeBc;

    private boolean purecloud;

    private String userPureCloud;

    private String idUserPureCloud;

    private boolean rpa;

    private String userRpa;

    private String moduleRpa;

    private String enterpriseRpa;

    private boolean powerbi;

    private boolean copilot;

    private boolean tactical;

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

    private boolean adobe;

    private boolean mailchimp;

    private boolean linktree;

    private boolean magento;

    private String magentoUser;

    private boolean shopify;

    private String userShopify;

    private boolean mercadoLibre;

    private boolean amazon;

    private boolean conekta;

    private boolean openPay;

    private boolean kuesky;

    private Long personId;

    private boolean hasUsb;
}
