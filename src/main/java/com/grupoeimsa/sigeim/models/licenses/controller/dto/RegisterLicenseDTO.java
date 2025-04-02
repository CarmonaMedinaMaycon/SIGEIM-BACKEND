package com.grupoeimsa.sigeim.models.licenses.controller.dto;


import com.grupoeimsa.sigeim.models.licenses.model.BeanLicense;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterLicenseDTO {
    private Long licenseId;

    private boolean outlook;

    private String accountOutlook;

    private String typeOutlook;

    private String alias;

    private String mailbox;

    private String commentsOutlook;

    private String phoneAuthenticator;

    private String twoFactorAuthenticationName;

    private boolean crm;

    private String userCrm;

    private String typeCrm;

    private String commentsCrm;

    private boolean bc;

    private String userBc;

    private String idUserBc;

    private String typeBc;

    private String enterpriseBc;

    private boolean purecloud;

    private String userPureCloud;

    private String idUserPureCloud;

    private boolean rpa;

    private String userRpa;

    private String moduleRpa;

    private String enterpriseRpa;

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

    private BeanPerson person;

    public RegisterLicenseDTO(BeanLicense beanLicense) {
        this.licenseId = beanLicense.getLicensesId();
        this.outlook = beanLicense.isOutlook();
        this.accountOutlook = beanLicense.getAccountOutlook();
        this.typeOutlook = beanLicense.getTypeOutlook();
        this.alias = beanLicense.getAlias();
        this.mailbox = beanLicense.getMailbox();
        this.commentsOutlook = beanLicense.getCommentsOutlook();
        this.phoneAuthenticator = beanLicense.getPhoneAuthenticator();
        this.twoFactorAuthenticationName = beanLicense.getTwoFactorAuthenticationName();
        this.crm = beanLicense.isCrm();
        this.userCrm = beanLicense.getUserCrm();
        this.typeCrm = beanLicense.getTypeCrm();
        this.commentsCrm = beanLicense.getCommentsCrm();
        this.bc = beanLicense.isBc();
        this.userBc = beanLicense.getUserBc();
        this.idUserBc = beanLicense.getIdUserBc();
        this.typeBc = beanLicense.getTypeBc();
        this.enterpriseBc = beanLicense.getEnterpriseBc();
        this.purecloud = beanLicense.isPurecloud();
        this.userPureCloud = beanLicense.getUserPureCloud();
        this.idUserPureCloud = beanLicense.getIdUserPureCloud();
        this.rpa = beanLicense.isRpa();
        this.userRpa = beanLicense.getUserRpa();
        this.moduleRpa = beanLicense.getModuleRpa();
        this.enterpriseRpa = beanLicense.getEnterpriseRpa();
        this.tactical = beanLicense.isTactical();
        this.instagram = beanLicense.isInstagram();
        this.userInstagram = beanLicense.getUserInstagram();
        this.facebook = beanLicense.isFacebook();
        this.userFacebook = beanLicense.getUserFacebook();
        this.tiktok = beanLicense.isTiktok();
        this.userTiktok = beanLicense.getUserTiktok();
        this.linkedin = beanLicense.isLinkedin();
        this.userLinkedin = beanLicense.getUserLinkedin();
        this.youtube = beanLicense.isYoutube();
        this.userYoutube = beanLicense.getUserYoutube();
        this.adobe = beanLicense.isAdobe();
        this.mailchimp = beanLicense.isMailchimp();
        this.linktree = beanLicense.isLinktree();
        this.magento = beanLicense.isMagento();
        this.magentoUser = beanLicense.getMagentoUser();
        this.shopify = beanLicense.isShopify();
        this.userShopify = beanLicense.getUserShopify();
        this.mercadoLibre = beanLicense.isMercadoLibre();
        this.amazon = beanLicense.isAmazon();
        this.conekta = beanLicense.isConekta();
        this.openPay = beanLicense.isOpenPay();
        this.kuesky = beanLicense.isKuesky();
    }
}
