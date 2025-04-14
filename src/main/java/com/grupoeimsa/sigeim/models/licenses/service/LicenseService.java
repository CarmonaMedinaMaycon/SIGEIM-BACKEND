package com.grupoeimsa.sigeim.models.licenses.service;


import com.grupoeimsa.sigeim.models.licenses.controller.dto.DeleteLicenseDto;
import com.grupoeimsa.sigeim.models.licenses.controller.dto.EditLicenseDTO;
import com.grupoeimsa.sigeim.models.licenses.controller.dto.RegisterLicenseDTO;
import com.grupoeimsa.sigeim.models.licenses.controller.dto.ResponseLicenseDTO;
import com.grupoeimsa.sigeim.models.licenses.model.BeanLicense;
import com.grupoeimsa.sigeim.models.licenses.model.ILicense;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveLicenses;
import com.grupoeimsa.sigeim.models.responsives.model.EStatus;
import com.grupoeimsa.sigeim.models.responsives.model.IResponsiveLicenses;
import com.grupoeimsa.sigeim.utils.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class LicenseService {
    public final ILicense licensesRepository;
    public final IPerson personsRepository;
    public final IResponsiveLicenses responsiveLicensesRepository;

    public LicenseService(ILicense licenseRepository, IPerson personsRepository
            , IResponsiveLicenses responsiveLicensesRepository) {
        this.licensesRepository = licenseRepository;
        this.personsRepository = personsRepository;
        this.responsiveLicensesRepository = responsiveLicensesRepository;
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
        license.setSupplierOutlook(licenseDTO.getSupplierOutlook());
        license.setAliasOutlook(licenseDTO.getAliasOutlook());
        license.setMailboxOutlook(licenseDTO.getMailboxOutlook());
        license.setCommentsOutlook(licenseDTO.getCommentsOutlook());
        license.setAuthPhoneNumber(licenseDTO.getAuthPhoneNumber());
        license.setAuthTwoFactorAuthenticationName(licenseDTO.getAuthTwoFactorAuthenticationName());
        license.setAuthDepartament(licenseDTO.getAuthDepartament());
        license.setCrm(licenseDTO.isCrm());
        license.setUserCrm(licenseDTO.getUserCrm());
        license.setTypeCrm(licenseDTO.getTypeCrm());
        license.setSupplierCrm(licenseDTO.getSupplierCrm());
        license.setCommentsCrm(licenseDTO.getCommentsCrm());
        license.setBc(licenseDTO.isBc());
        license.setUserBc(licenseDTO.getUserBc());
        license.setIdUserBc(licenseDTO.getIdUserBc());
        license.setTypeBc(licenseDTO.getTypeBc());
        license.setSupplierBc(licenseDTO.getSupplierBc());
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
        license.setHasUsb(licenseDTO.isHasUsb());
        BeanPerson person = personsRepository.findById(licenseDTO.getPersonId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + licenseDTO.getPersonId()));
        license.setPerson(person);
        licensesRepository.save(license);
    }


    @Transactional
    public void editAssignLicense(EditLicenseDTO licenseDTO) {
        BeanLicense license = licensesRepository.findById(licenseDTO.getLicenseId())
                .orElseThrow(() -> new CustomException("License not found"));

        license.setOutlook(licenseDTO.isOutlook());
        license.setAccountOutlook(licenseDTO.getAccountOutlook());
        license.setTypeOutlook(licenseDTO.getTypeOutlook());
        license.setSupplierOutlook(licenseDTO.getSupplierOutlook());
        license.setAliasOutlook(licenseDTO.getAliasOutlook());
        license.setMailboxOutlook(licenseDTO.getMailboxOutlook());
        license.setCommentsOutlook(licenseDTO.getCommentsOutlook());
        license.setAuthPhoneNumber(licenseDTO.getAuthPhoneNumber());
        license.setAuthTwoFactorAuthenticationName(licenseDTO.getAuthTwoFactorAuthenticationName());
        license.setAuthDepartament(licenseDTO.getAuthDepartament());
        license.setCrm(licenseDTO.isCrm());
        license.setUserCrm(licenseDTO.getUserCrm());
        license.setTypeCrm(licenseDTO.getTypeCrm());
        license.setSupplierCrm(licenseDTO.getSupplierCrm());
        license.setCommentsCrm(licenseDTO.getCommentsCrm());
        license.setBc(licenseDTO.isBc());
        license.setUserBc(licenseDTO.getUserBc());
        license.setIdUserBc(licenseDTO.getIdUserBc());
        license.setTypeBc(licenseDTO.getTypeBc());
        license.setSupplierBc(licenseDTO.getSupplierBc());
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
        license.setHasUsb(licenseDTO.isHasUsb());
        BeanPerson person = personsRepository.findById(licenseDTO.getPersonId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + licenseDTO.getPersonId()));
        license.setPerson(person);
        licensesRepository.save(license);
    }

    public byte[] generateExcelFile() throws IOException {
        List<BeanLicense> licenses = licensesRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Licencias");

        // Definir los encabezados
        String[] headers = {
                "Núm.",
                "Persona",
                "Empresa",
                "Departamento",
                "Puesto",
                // Outlook
                "Outlook", "Cuenta Outlook", "Tipo Outlook", "Proveedor Outlook", "Alias", "Mailbox", "Comentarios Outlook",
                // Autenticación
                "Teléfono Auth", "Nombre 2FA", "Departamento Auth",
                // CRM
                "CRM", "Usuario CRM", "Tipo CRM", "Proveedor CRM", "Comentarios CRM",
                // Business Central
                "BC", "Usuario BC", "ID Usuario BC", "Tipo BC", "Proveedor BC", "Empresa BC",
                // PureCloud
                "PureCloud", "Usuario PureCloud", "ID PureCloud",
                // RPA
                "RPA", "Usuario RPA", "Módulo RPA", "Empresa RPA",
                // Otros
                "Power BI", "Copilot", "Táctico",
                // Redes Sociales
                "Instagram", "Usuario Instagram",
                "Facebook", "Usuario Facebook",
                "TikTok", "Usuario TikTok",
                "LinkedIn", "Usuario LinkedIn",
                "YouTube", "Usuario YouTube",
                // Plataformas
                "Adobe", "Mailchimp", "Linktree",
                // E-commerce
                "Magento", "Usuario Magento",
                "Shopify", "Usuario Shopify",
                "Mercado Libre",
                "Amazon",
                "Conekta",
                "OpenPay",
                "Kuesky",
                "USB"
        };

        // Crear estilo para encabezados en negrita
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Crear la fila de cabecera con estilo en negrita
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle); // Aplicar estilo negrita
        }

        // Congelar paneles (fila de encabezados)
        sheet.createFreezePane(0, 1, 0, 1);

        // Ajustar el tamaño de las columnas de la cabecera
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        int rowNum = 1;
        for (BeanLicense license : licenses) {
            Row row = sheet.createRow(rowNum++);

            // Información básica
            row.createCell(0).setCellValue(rowNum - 1);
            if (license.getPerson() != null) {
                row.createCell(1).setCellValue(getSafeValue(
                        license.getPerson().getName() + " " +
                                license.getPerson().getSurname() + " " +
                                license.getPerson().getLastname()
                ));
                row.createCell(2).setCellValue(getSafeValue(license.getPerson().getEnterprise()));
                row.createCell(3).setCellValue(getSafeValue(license.getPerson().getDepartament()));
                row.createCell(4).setCellValue(getSafeValue(license.getPerson().getPosition()));
            } else {
                for (int i = 1; i <= 4; i++) {
                    row.createCell(i).setCellValue("SIN-INF");
                }
            }

            // Outlook
            row.createCell(5).setCellValue(license.isOutlook() ? "Sí" : "No");
            row.createCell(6).setCellValue(getSafeValue(license.getAccountOutlook()));
            row.createCell(7).setCellValue(getSafeValue(license.getTypeOutlook()));
            row.createCell(8).setCellValue(getSafeValue(license.getSupplierOutlook()));
            row.createCell(9).setCellValue(getSafeValue(license.getAliasOutlook()));
            row.createCell(10).setCellValue(getSafeValue(license.getMailboxOutlook()));
            row.createCell(11).setCellValue(getSafeValue(license.getCommentsOutlook()));

            // Autenticación
            row.createCell(12).setCellValue(getSafeValue(license.getAuthPhoneNumber()));
            row.createCell(13).setCellValue(getSafeValue(license.getAuthTwoFactorAuthenticationName()));
            row.createCell(14).setCellValue(getSafeValue(license.getAuthDepartament()));

            // CRM
            row.createCell(15).setCellValue(license.isCrm() ? "Sí" : "No");
            row.createCell(16).setCellValue(getSafeValue(license.getUserCrm()));
            row.createCell(17).setCellValue(getSafeValue(license.getTypeCrm()));
            row.createCell(18).setCellValue(getSafeValue(license.getSupplierCrm()));
            row.createCell(19).setCellValue(getSafeValue(license.getCommentsCrm()));

            // Business Central
            row.createCell(20).setCellValue(license.isBc() ? "Sí" : "No");
            row.createCell(21).setCellValue(getSafeValue(license.getUserBc()));
            row.createCell(22).setCellValue(getSafeValue(license.getIdUserBc()));
            row.createCell(23).setCellValue(getSafeValue(license.getTypeBc()));
            row.createCell(24).setCellValue(getSafeValue(license.getSupplierBc()));
            row.createCell(25).setCellValue(getSafeValue(license.getEnterpriseBc()));

            // PureCloud
            row.createCell(26).setCellValue(license.isPurecloud() ? "Sí" : "No");
            row.createCell(27).setCellValue(getSafeValue(license.getUserPureCloud()));
            row.createCell(28).setCellValue(getSafeValue(license.getIdUserPureCloud()));

            // RPA
            row.createCell(29).setCellValue(license.isRpa() ? "Sí" : "No");
            row.createCell(30).setCellValue(getSafeValue(license.getUserRpa()));
            row.createCell(31).setCellValue(getSafeValue(license.getModuleRpa()));
            row.createCell(32).setCellValue(getSafeValue(license.getEnterpriseRpa()));

            // Otros
            row.createCell(33).setCellValue(license.isPowerbi() ? "Sí" : "No");
            row.createCell(34).setCellValue(license.isCopilot() ? "Sí" : "No");
            row.createCell(35).setCellValue(license.isTactical() ? "Sí" : "No");

            // Redes Sociales
            row.createCell(36).setCellValue(license.isInstagram() ? "Sí" : "No");
            row.createCell(37).setCellValue(getSafeValue(license.getUserInstagram()));
            row.createCell(38).setCellValue(license.isFacebook() ? "Sí" : "No");
            row.createCell(39).setCellValue(getSafeValue(license.getUserFacebook()));
            row.createCell(40).setCellValue(license.isTiktok() ? "Sí" : "No");
            row.createCell(41).setCellValue(getSafeValue(license.getUserTiktok()));
            row.createCell(42).setCellValue(license.isLinkedin() ? "Sí" : "No");
            row.createCell(43).setCellValue(getSafeValue(license.getUserLinkedin()));
            row.createCell(44).setCellValue(license.isYoutube() ? "Sí" : "No");
            row.createCell(45).setCellValue(getSafeValue(license.getUserYoutube()));

            // Plataformas
            row.createCell(46).setCellValue(license.isAdobe() ? "Sí" : "No");
            row.createCell(47).setCellValue(license.isMailchimp() ? "Sí" : "No");
            row.createCell(48).setCellValue(license.isLinktree() ? "Sí" : "No");

            // E-commerce
            row.createCell(49).setCellValue(license.isMagento() ? "Sí" : "No");
            row.createCell(50).setCellValue(getSafeValue(license.getMagentoUser()));
            row.createCell(51).setCellValue(license.isShopify() ? "Sí" : "No");
            row.createCell(52).setCellValue(getSafeValue(license.getUserShopify()));
            row.createCell(53).setCellValue(license.isMercadoLibre() ? "Sí" : "No");
            row.createCell(54).setCellValue(license.isAmazon() ? "Sí" : "No");
            row.createCell(55).setCellValue(license.isConekta() ? "Sí" : "No");
            row.createCell(56).setCellValue(license.isOpenPay() ? "Sí" : "No");
            row.createCell(57).setCellValue(license.isKuesky() ? "Sí" : "No");
            row.createCell(58).setCellValue(license.isHasUsb() ? "Sí" : "No");
        }

        // Ajustar tamaño de columnas para todas las filas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            // Asegurar un ancho mínimo para columnas importantes
            if (i == 1 || i == 2 || i == 6 || i == 7 || i == 8) {
                if (sheet.getColumnWidth(i) < 4000) {
                    sheet.setColumnWidth(i, 4000);
                }
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }

    @Transactional
    public void deleteLicense(DeleteLicenseDto dto) {
        BeanLicense license = licensesRepository.findById(dto.getLicenseId())
                .orElseThrow(() -> new RuntimeException("Licencia no encontrada"));

        List<BeanResponsiveLicenses> responsives = responsiveLicensesRepository
                .findByLicense_LicensesId(dto.getLicenseId());

        if (responsives != null && !responsives.isEmpty()) {
            for (BeanResponsiveLicenses resp : responsives) {
                resp.setStatus(EStatus.CANCELADA);
            }
            responsiveLicensesRepository.saveAll(responsives);
        } else {
            // ⚠️ desvincular desde el lado de la persona
            BeanPerson person = license.getPerson();
            if (person != null) {
                person.setLicense(null);
            }

            // eliminar la licencia
            licensesRepository.delete(license);
        }
    }


    private String getSafeValue(String value) {
        return value != null ? value : "";
    }
}
