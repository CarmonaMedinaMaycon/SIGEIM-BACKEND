package com.grupoeimsa.sigeim.models.cellphones.service;


import com.grupoeimsa.sigeim.models.cellphones.controller.dto.AvailablePersonCellphoneDto;
import com.grupoeimsa.sigeim.models.cellphones.controller.dto.CellphoneEditDto;
import com.grupoeimsa.sigeim.models.cellphones.controller.dto.CellphoneTableDto;
import com.grupoeimsa.sigeim.models.cellphones.controller.dto.ResponseCellphoneDTO;
import com.grupoeimsa.sigeim.models.cellphones.controller.dto.ResponseRegisterCellphone;
import com.grupoeimsa.sigeim.models.cellphones.controller.dto.ResponseToGenerateResponsiveDto;
import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.cellphones.model.ICellphone;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveCellphone;
import com.grupoeimsa.sigeim.models.responsives.model.EStatus;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CellphoneService {
    public final ICellphone cellphoneRepository;
    public final IPerson personRepository;

    public CellphoneService(ICellphone cellphoneRepository, IPerson personRepository) {
        this.cellphoneRepository = cellphoneRepository;
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public Page<ResponseCellphoneDTO> findAll(String search, int page, int size, Boolean status, String legalName, String area) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<BeanCellphone> spec = Specification.where(null);

        if (search != null && !search.isBlank()) {
            spec = spec.and((root, query, cb) -> {
                String likeValue = "%" + search.toLowerCase() + "%";
                return cb.or(
                        cb.like(cb.lower(root.get("legalName")), likeValue),
                        cb.like(cb.lower(root.get("company")), likeValue),
                        cb.like(cb.lower(cb.function("CAST", String.class, root.get("shortDialing"))), likeValue),
                        cb.like(cb.lower(root.get("imei")), likeValue)
                );
            });
        }

        if (legalName != null && !legalName.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("legalName")), legalName.toLowerCase()));
        }

        if (area != null && !area.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("area")), area.toLowerCase()));
        }

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        Page<BeanCellphone> result = cellphoneRepository.findAll(spec, pageable);
        return result.map(ResponseCellphoneDTO::new);
    }

    @Transactional(readOnly = true)
    public ResponseCellphoneDTO findById(long id) {
        BeanCellphone cellphone = cellphoneRepository.findById(id).orElseThrow(() -> new CustomException("The cellphone was not found"));
        return new ResponseCellphoneDTO(cellphone);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void registerCellphone(ResponseRegisterCellphone registerCellphone) {
        BeanCellphone cellphone = new BeanCellphone();

        cellphone.setLegalName(registerCellphone.getLegalName());
        cellphone.setEquipamentName(registerCellphone.getEquipamentName());
        cellphone.setCompany(registerCellphone.getCompany());
        cellphone.setArea(registerCellphone.getArea());

        cellphone.setShortDialing( registerCellphone.getShortDialing() == null || registerCellphone.getShortDialing().trim().isEmpty() ? "NA" : registerCellphone.getShortDialing().trim());

        cellphone.setDateRenovation(registerCellphone.getDateRenovation());
        cellphone.setNumber(registerCellphone.getNumber());

        // IMEI: si viene null o vacío, poner "NA"
        cellphone.setImei(
                (registerCellphone.getImei() == null || registerCellphone.getImei().trim().isEmpty())
                        ? "NA"
                        : registerCellphone.getImei().trim()
        );

        // Comentarios: si viene null o vacío, poner "NA"
        cellphone.setComments(
                (registerCellphone.getComments() == null || registerCellphone.getComments().trim().isEmpty())
                        ? "NA"
                        : registerCellphone.getComments().trim()
        );

        cellphone.setStatus(true);
        cellphone.setWhatsappBussiness(registerCellphone.getWhatsappBussiness() != null && registerCellphone.getWhatsappBussiness());

        // Usuario asignado: si no se encuentra, lanza excepción
        cellphone.setPerson(
                personRepository.findById(registerCellphone.getPersonId())
                        .orElseThrow(() -> new CustomException("El usuario asignado no fue encontrado"))
        );

        cellphoneRepository.save(cellphone);
    }


    @Transactional
    public void enableDisable(Long id) {
        BeanCellphone cellphone = cellphoneRepository.findById(id)
                .orElseThrow(() -> new CustomException("Celular no encontrado"));

        boolean wasActive = Boolean.TRUE.equals(cellphone.getStatus()); // evitar null
        boolean willBeDisabled = wasActive; // ya que vamos a invertir el estado

        // Invertir estado
        cellphone.setStatus(!wasActive);

        // Si se va a desactivar, cancelar responsivas activas o por firmar
        if (willBeDisabled && cellphone.getResponsiveCellphones() != null) {
            for (BeanResponsiveCellphone resp : cellphone.getResponsiveCellphones()) {
                resp.setStatus(EStatus.CANCELADA);
            }
        }

        // Asignar persona default si se desactiva
        if (willBeDisabled) {
            BeanPerson defaultPerson = personRepository.findById(1L)
                    .orElseThrow(() -> new CustomException("Persona por defecto no encontrada"));
            cellphone.setPerson(defaultPerson);
        }

        // Guardar cambios (gracias a la relación bidireccional, puede guardar en cascada si está configurado)
        cellphoneRepository.save(cellphone);
    }


    @Transactional
    public void update(ResponseRegisterCellphone registerCellphone) {
        BeanCellphone cellphone = cellphoneRepository.findById(registerCellphone.getCellphoneId())
                .orElseThrow(() -> new CustomException("The cellphone was not found"));

        cellphone.setLegalName(registerCellphone.getLegalName());
        cellphone.setEquipamentName(registerCellphone.getEquipamentName());
        cellphone.setCompany(registerCellphone.getCompany());
        cellphone.setShortDialing(registerCellphone.getShortDialing());
        cellphone.setDateRenovation(registerCellphone.getDateRenovation());
        cellphone.setImei(registerCellphone.getImei());
        cellphone.setComments(registerCellphone.getComments());
        cellphone.setWhatsappBussiness(registerCellphone.getWhatsappBussiness());
        cellphone.setStatus(true);
        cellphone.setNumber(registerCellphone.getNumber());
        cellphone.setArea(registerCellphone.getArea());

        System.out.println("Usuario asignado" + registerCellphone.getPersonId());

        // 🔥 Aquí actualizas la persona asignada
        cellphone.setPerson(personRepository.findById(registerCellphone.getPersonId())
                .orElseThrow(() -> new CustomException("El usuario asignado no fue encontrado")));

        cellphoneRepository.save(cellphone);
    }



    public List<AvailablePersonCellphoneDto> getAvailablePersonsForCellphone(Long currentPersonId) {
        List<BeanPerson> persons = personRepository.findAllActivePersons(); // sin filtrar por celular

        return persons.stream()
                .map(p -> new AvailablePersonCellphoneDto(
                        p.getPersonId(),
                        p.getName() + " " + p.getLastname() + " " + p.getSurname(),
                        p.getDepartament(),
                        p.getCellphone() != null && !p.getCellphone().isEmpty()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<CellphoneTableDto> getAllCellphonesForTable(String search, int page, int size, String legalName, String area) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<BeanCellphone> spec = Specification.where(null);

        if (search != null && !search.isBlank()) {
            spec = spec.and((root, query, cb) -> {
                String likeValue = "%" + search.toLowerCase() + "%";
                return cb.or(
                        cb.like(cb.lower(root.get("legalName")), likeValue),
                        cb.like(cb.lower(root.get("area")), likeValue),
                        cb.like(cb.lower(root.get("company")), likeValue),
                        cb.like(cb.lower(root.get("imei")), likeValue)
                );
            });
        }

        if (legalName != null && !legalName.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("legalName")), legalName.toLowerCase()));
        }

        if (area != null && !area.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("area")), area.toLowerCase()));
        }

        Page<BeanCellphone> result = cellphoneRepository.findAll(spec, pageable);

        return result.map(c -> new CellphoneTableDto(
                c.getCellphoneId(),
                c.getEquipamentName(),
                c.getCompany(),
                c.getImei(),
                c.getShortDialing(),
                c.getLegalName(),
                c.getPerson() != null
                        ? c.getPerson().getName() + " " + c.getPerson().getLastname() + " " + c.getPerson().getSurname()
                        : "Sin asignar",
                c.getDateRenovation(),
                c.getComments(),
                c.getPerson() != null
                        ? c.getPerson().getName() + " " + c.getPerson().getLastname() + " " + c.getPerson().getSurname()
                        : "No ha sido asignado",
                c.getStatus(),
                c.getNumber(),
                c.getWhatsappBussiness() ? "Si" : "No",
                c.getArea()
        ));
    }

    @Transactional(readOnly = true)
    public CellphoneEditDto getCellphoneEditDtoById(Long id) {
        BeanCellphone cellphone = cellphoneRepository.findById(id)
                .orElseThrow(() -> new CustomException("Cellphone not found"));

        return new CellphoneEditDto(
                cellphone.getCellphoneId(),
                cellphone.getEquipamentName(),
                cellphone.getLegalName(),
                cellphone.getCompany(),
                cellphone.getShortDialing(),
                cellphone.getImei(),
                cellphone.getWhatsappBussiness(),
                cellphone.getDateRenovation(),
                cellphone.getComments(),
                cellphone.getPerson() != null ? cellphone.getPerson().getPersonId() : null,
                cellphone.getNumber(),
                cellphone.getArea()
        );
    }

    public List<ResponseToGenerateResponsiveDto> getCellphonesForResponsive() {
        return cellphoneRepository.findAvailableForResponsiva()
                .stream()
                .map(c -> new ResponseToGenerateResponsiveDto(
                        c.getCellphoneId(),
                        c.getImei(),
                        c.getNumber()
                ))
                .toList();
    }


    public byte[] generateExcelFile() throws IOException {
        List<BeanCellphone> cellphones = cellphoneRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Teléfonos Celulares");

        // Crear estilo para fechas
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

        // Estilo para encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Encabezados personalizados
        String[] headers = {
                "Razón Social",
                "Usuario",
                "Teléfono",
                "Compañía",
                "Área",
                "Marcación Corta",
                "Fecha de Última Renovación",
                "Equipo Asignado",
                "IMEI",
                "WhatsApp Bussiness",
                "COMENTARIO GENERAL"
        };

        // Crear fila de encabezado
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (BeanCellphone cellphone : cellphones) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(getSafeValue(cellphone.getLegalName()));
            row.createCell(1).setCellValue(
                    cellphone.getPerson() != null && !"Sistemas NA NA".equals(cellphone.getPerson().getFullName())
                            ? cellphone.getPerson().getFullName()
                            : "NA"
            );
            row.createCell(2).setCellValue(getSafeValue(cellphone.getNumber()));
            row.createCell(3).setCellValue(getSafeValue(cellphone.getCompany()));
            row.createCell(4).setCellValue(getSafeValue(cellphone.getArea()));
            row.createCell(5).setCellValue(getSafeValue(cellphone.getShortDialing()));

            // Fecha con formato
            Cell dateCell = row.createCell(6);
            if (cellphone.getDateRenovation() != null) {
                dateCell.setCellValue(java.sql.Date.valueOf(cellphone.getDateRenovation()));
                dateCell.setCellStyle(dateCellStyle);
            } else {
                dateCell.setCellValue("");
            }

            row.createCell(7).setCellValue(getSafeValue(cellphone.getEquipamentName()));
            row.createCell(8).setCellValue(getSafeValue(cellphone.getImei()));
            row.createCell(9).setCellValue(cellphone.getWhatsappBussiness() != null
                    ? (cellphone.getWhatsappBussiness() ? "Sí" : "No")
                    : "Sin info");
            row.createCell(10).setCellValue(getSafeValue(cellphone.getComments()));
        }

        // Ajuste de columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            if (sheet.getColumnWidth(i) < 4000) {
                sheet.setColumnWidth(i, 4000);
            }
        }

        sheet.createFreezePane(0, 1); // congelar encabezado

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        return baos.toByteArray();
    }

    private String getSafeValue(String value) {
        return (value != null && !value.trim().isEmpty()) ? value : "NA";
    }




}
