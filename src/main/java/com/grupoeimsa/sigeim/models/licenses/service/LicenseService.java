package com.grupoeimsa.sigeim.models.licenses.service;


import com.grupoeimsa.sigeim.models.licenses.controller.dto.RegisterLicenseDTO;
import com.grupoeimsa.sigeim.models.licenses.controller.dto.ResponseLicenseDTO;
import com.grupoeimsa.sigeim.models.licenses.model.BeanLicense;
import com.grupoeimsa.sigeim.models.licenses.model.ILicense;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
public class LicenseService {
    public final ILicense licensesRepository;
    public final IPerson personsRepository;

    public LicenseService(ILicense licenseRepository, IPerson personsRepository) {
        this.licensesRepository = licenseRepository;
        this.personsRepository = personsRepository;
    }

    @Transactional(readOnly = true)
    public Page<ResponseLicenseDTO> findAll(String search, int page, int size, Boolean status, String enterprise, String departament){
        Pageable pageable = PageRequest.of(page, size);
        Page<BeanLicense> licenses = licensesRepository.findAllBySearch(
                search,
                departament,
                enterprise,
                status,
                pageable
        );
        if (licenses.isEmpty()){
            throw  new CustomException("No licences were found");
        }
        return licenses.map(ResponseLicenseDTO::new);
    }

    @Transactional(readOnly = true)
    public ResponseLicenseDTO findById(Long id){
        BeanLicense license = licensesRepository.findById(id).orElseThrow(() -> new CustomException("The user was not found"));
        return new ResponseLicenseDTO(license);
    }

    @Transactional
    public void assignLicense(RegisterLicenseDTO licenseDTO) {
        BeanLicense license = new BeanLicense();
        license.setOutlook(licenseDTO.isOutlook());
        license.setAccountOutlook(licenseDTO.getAccountOutlook());
        license.setTypeOutlook(licenseDTO.getTypeOutlook());
        license.setAlias(licenseDTO.getAlias());
        license.setMailbox(licenseDTO.getMailbox());
        license.setCommentsOutlook(licenseDTO.getCommentsOutlook());
        license.setPhoneAuthenticator(licenseDTO.getPhoneAuthenticator());
        license.setTwoFactorAuthenticationName(licenseDTO.getTwoFactorAuthenticationName());
        license.setCrm(licenseDTO.isCrm());
        license.setUserCrm(licenseDTO.getUserCrm());
        license.setTypeCrm(licenseDTO.getTypeCrm());
        license.setCommentsCrm(licenseDTO.getCommentsCrm());
        license.setBc(licenseDTO.isBc());
        license.setUserBc(licenseDTO.getUserBc());
        license.setIdUserBc(licenseDTO.getIdUserBc());
        license.setTypeBc(licenseDTO.getTypeBc());
        license.setEnterpriseBc(licenseDTO.getEnterpriseBc());
        license.setPurecloud(licenseDTO.isPurecloud());
        license.setUserPureCloud(licenseDTO.getUserPureCloud());
        license.setIdUserPureCloud(licenseDTO.getIdUserPureCloud());
        license.setRpa(licenseDTO.isRpa());
        license.setUserRpa(licenseDTO.getUserRpa());
        license.setModuleRpa(licenseDTO.getModuleRpa());
        license.setEnterpriseRpa(licenseDTO.getEnterpriseRpa());
        license.setTactical(licenseDTO.isTactical());
        license.setInstagram(licenseDTO.isInstagram());
        license.setUserInstagram(licenseDTO.getUserInstagram());
        license.setFacebook(licenseDTO.isFacebook());
        license.setUserFacebook(licenseDTO.getUserFacebook());
        license.setTiktok(licenseDTO.isTiktok());
        license.setUserTiktok(licenseDTO.getUserTiktok());
        license.setLinkedin(licenseDTO.isLinkedin());
        license.setUserLinkedin(licenseDTO.getUserLinkedin());
        license.setYoutube(licenseDTO.isYoutube());
        license.setUserYoutube(licenseDTO.getUserYoutube());
        license.setAdobe(licenseDTO.isAdobe());
        license.setMailchimp(licenseDTO.isMailchimp());
        license.setLinktree(licenseDTO.isLinktree());
        license.setMagento(licenseDTO.isMagento());
        license.setMagentoUser(licenseDTO.getMagentoUser());
        license.setShopify(licenseDTO.isShopify());
        license.setUserShopify(licenseDTO.getUserShopify());
        license.setMercadoLibre(licenseDTO.isMercadoLibre());
        license.setAmazon(licenseDTO.isAmazon());
        license.setConekta(licenseDTO.isConekta());
        license.setOpenPay(licenseDTO.isOpenPay());
        license.setKuesky(licenseDTO.isKuesky());
        license.setPerson(licenseDTO.getPerson());
        licensesRepository.save(license);
    }


    @Transactional
    public void editAssignLicense(RegisterLicenseDTO licenseDTO) {
        BeanLicense license = licensesRepository.findById(licenseDTO.getLicenseId())
                .orElseThrow(() -> new CustomException("License not found"));

        license.setOutlook(licenseDTO.isOutlook());
        license.setAccountOutlook(licenseDTO.getAccountOutlook());
        license.setTypeOutlook(licenseDTO.getTypeOutlook());
        license.setAlias(licenseDTO.getAlias());
        license.setMailbox(licenseDTO.getMailbox());
        license.setCommentsOutlook(licenseDTO.getCommentsOutlook());
        license.setPhoneAuthenticator(licenseDTO.getPhoneAuthenticator());
        license.setTwoFactorAuthenticationName(licenseDTO.getTwoFactorAuthenticationName());
        license.setCrm(licenseDTO.isCrm());
        license.setUserCrm(licenseDTO.getUserCrm());
        license.setTypeCrm(licenseDTO.getTypeCrm());
        license.setCommentsCrm(licenseDTO.getCommentsCrm());
        license.setBc(licenseDTO.isBc());
        license.setUserBc(licenseDTO.getUserBc());
        license.setIdUserBc(licenseDTO.getIdUserBc());
        license.setTypeBc(licenseDTO.getTypeBc());
        license.setEnterpriseBc(licenseDTO.getEnterpriseBc());
        license.setPurecloud(licenseDTO.isPurecloud());
        license.setUserPureCloud(licenseDTO.getUserPureCloud());
        license.setIdUserPureCloud(licenseDTO.getIdUserPureCloud());
        license.setRpa(licenseDTO.isRpa());
        license.setUserRpa(licenseDTO.getUserRpa());
        license.setModuleRpa(licenseDTO.getModuleRpa());
        license.setEnterpriseRpa(licenseDTO.getEnterpriseRpa());
        license.setTactical(licenseDTO.isTactical());
        license.setInstagram(licenseDTO.isInstagram());
        license.setUserInstagram(licenseDTO.getUserInstagram());
        license.setFacebook(licenseDTO.isFacebook());
        license.setUserFacebook(licenseDTO.getUserFacebook());
        license.setTiktok(licenseDTO.isTiktok());
        license.setUserTiktok(licenseDTO.getUserTiktok());
        license.setLinkedin(licenseDTO.isLinkedin());
        license.setUserLinkedin(licenseDTO.getUserLinkedin());
        license.setYoutube(licenseDTO.isYoutube());
        license.setUserYoutube(licenseDTO.getUserYoutube());
        license.setAdobe(licenseDTO.isAdobe());
        license.setMailchimp(licenseDTO.isMailchimp());
        license.setLinktree(licenseDTO.isLinktree());
        license.setMagento(licenseDTO.isMagento());
        license.setMagentoUser(licenseDTO.getMagentoUser());
        license.setShopify(licenseDTO.isShopify());
        license.setUserShopify(licenseDTO.getUserShopify());
        license.setMercadoLibre(licenseDTO.isMercadoLibre());
        license.setAmazon(licenseDTO.isAmazon());
        license.setConekta(licenseDTO.isConekta());
        license.setOpenPay(licenseDTO.isOpenPay());
        license.setKuesky(licenseDTO.isKuesky());
        licensesRepository.save(license);
    }
}
