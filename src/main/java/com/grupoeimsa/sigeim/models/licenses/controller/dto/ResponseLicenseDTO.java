package com.grupoeimsa.sigeim.models.licenses.controller.dto;


import com.grupoeimsa.sigeim.models.licenses.model.BeanLicense;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveLicenses;
import com.grupoeimsa.sigeim.models.responsives.model.EStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseLicenseDTO {
    private Long licenseId;

    private String fullName;

    private String personDepartament;

    private boolean outlook;

    private String accountOutlook;

    private String typeOutlook;

    private String supplierOutlook;

    private String aliasOutlook;

    private String mailboxOutlook;

    private String commentsOutlook;

    private String authPhoneNumber;

    private String authTwoFactorAuthenticationName;

    private String authDepartament;

    private boolean crm;

    private String userCrm;

    private String typeCrm;

    private String supplierCrm;

    private String commentsCrm;

    private boolean bc;

    private String userBc;

    private String idUserBc;

    private String typeBc;

    private String supplierBc;

    private String enterpriseBc;

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

    private Long responsiveLicenseId;

    private boolean hasUsb;

    private boolean status;

    // CHANGE

    public ResponseLicenseDTO(BeanLicense license) {
        this.licenseId = license.getLicensesId();
        this.outlook = license.isOutlook();
        this.accountOutlook = license.getAccountOutlook();
        this.typeOutlook = license.getTypeOutlook();
        this.supplierOutlook = license.getSupplierOutlook();
        this.aliasOutlook = license.getAliasOutlook();
        this.mailboxOutlook = license.getMailboxOutlook();
        this.commentsOutlook = license.getCommentsOutlook();
        this.authPhoneNumber = license.getAuthPhoneNumber();
        this.authTwoFactorAuthenticationName = license.getAuthTwoFactorAuthenticationName();
        this.authDepartament = license.getAuthDepartament();
        this.crm = license.isCrm();
        this.userCrm = license.getUserCrm();
        this.typeCrm = license.getTypeCrm();
        this.supplierCrm = license.getSupplierCrm();
        this.commentsCrm = license.getCommentsCrm();
        this.bc = license.isBc();
        this.userBc = license.getUserBc();
        this.idUserBc = license.getIdUserBc();
        this.typeBc = license.getTypeBc();
        this.supplierBc = license.getSupplierBc();
        this.enterpriseBc = license.getEnterpriseBc();
        this.purecloud = license.isPurecloud();
        this.userPureCloud = license.getUserPureCloud();
        this.idUserPureCloud = license.getIdUserPureCloud();
        this.rpa = license.isRpa();
        this.userRpa = license.getUserRpa();
        this.moduleRpa = license.getModuleRpa();
        this.enterpriseRpa = license.getEnterpriseRpa();
        this.powerbi = license.isPowerbi();
        this.copilot = license.isCopilot();
        this.tactical = license.isTactical();
        this.instagram = license.isInstagram();
        this.userInstagram = license.getUserInstagram();
        this.facebook = license.isFacebook();
        this.userFacebook = license.getUserFacebook();
        this.tiktok = license.isTiktok();
        this.userTiktok = license.getUserTiktok();
        this.linkedin = license.isLinkedin();
        this.userLinkedin = license.getUserLinkedin();
        this.youtube = license.isYoutube();
        this.userYoutube = license.getUserYoutube();
        this.adobe = license.isAdobe();
        this.mailchimp = license.isMailchimp();
        this.linktree = license.isLinktree();
        this.magento = license.isMagento();
        this.magentoUser = license.getMagentoUser();
        this.shopify = license.isShopify();
        this.userShopify = license.getUserShopify();
        this.mercadoLibre = license.isMercadoLibre();
        this.amazon = license.isAmazon();
        this.conekta = license.isConekta();
        this.openPay = license.isOpenPay();
        this.kuesky = license.isKuesky();
        this.personId = license.getPerson() != null ? license.getPerson().getPersonId() : null;
        this.responsiveLicenseId =
                license.getResponsivesLicenses() != null && !license.getResponsivesLicenses().isEmpty()
                        ? license.getResponsivesLicenses().getFirst().getResponsiveCellphoneId()
                        : null;
        this.fullName = license.getPerson().getName() + " " +
                license.getPerson().getLastname() + " " +
                license.getPerson().getSurname();
        this.personDepartament = license.getPerson().getDepartament();
        this.hasUsb = license.isHasUsb();
        this.status = license.isStatus();
    }


}
