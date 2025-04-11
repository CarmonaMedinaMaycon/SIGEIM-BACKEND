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
import com.grupoeimsa.sigeim.utils.CustomException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<ResponseCellphoneDTO> findAll(String search, int page, int size, Boolean status, String enterprise, String departament) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BeanCellphone> cellphone = cellphoneRepository.findAllBySearch(
                search,
                departament,
                enterprise,
                status,
                pageable
        );
        if (cellphone.isEmpty()){
            throw new CustomException("No cellphone were found");
        }
        return cellphone.map(ResponseCellphoneDTO::new);
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
        cellphone.setShortDialing(registerCellphone.getShortDialing());
        cellphone.setDateRenovation(registerCellphone.getDateRenovation());
        cellphone.setImei(registerCellphone.getImei());
        cellphone.setComments(registerCellphone.getComments());
        cellphone.setStatus(true);
        cellphone.setWhatsappBussiness(registerCellphone.getWhatsappBussiness());
        cellphone.setPerson(personRepository.findById(registerCellphone.getPersonId())
                .orElseThrow(() -> new CustomException("El usuario asignado no fue encontrado")));
        cellphoneRepository.save(cellphone);
    }

    @Transactional
    public void enableDisable(Long id){
        BeanCellphone cellphone = cellphoneRepository.findById(id).orElseThrow(() -> new CustomException("Person not found"));
        cellphone.setStatus(!cellphone.getStatus());
        BeanPerson defaultPerson = personRepository.findById(1L)
                .orElseThrow(() -> new CustomException("Default person not found"));

        cellphone.setPerson(defaultPerson);
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
    public Page<CellphoneTableDto> getAllCellphonesForTable(String search, int page, int size, Boolean status, String enterprise, String departament) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BeanCellphone> cellphones = cellphoneRepository.findAllBySearch(
                search,
                departament,
                enterprise,
                status,
                pageable
        );

        if (cellphones.isEmpty()) {
            throw new CustomException("No cellphones were found");
        }

        // Mapeo al DTO ligero
        return cellphones.map(c -> new CellphoneTableDto(
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
                c.getStatus()
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
                cellphone.getPerson() != null ? cellphone.getPerson().getPersonId() : null
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

        // Definir los encabezados
        String[] headers = {
                "Núm.",
                "Nombre del Equipo",
                "Nombre Legal",
                "Empresa",
                "Persona Asignada",
                "Departamento",
                "Puesto",
                "Marcación Rápida",
                "Número Teléfono",
                "IMEI",
                "Fecha de Renovación",
                "Estado",
                "WhatsApp Business",
                "Comentarios"
        };

        // Crear la fila de cabecera
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Aplicar estilo de negrita a los encabezados
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        for (Cell cell : headerRow) {
            cell.setCellStyle(headerStyle);
        }

        // Ajustar el tamaño de las columnas de la cabecera
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        int rowNum = 1;
        for (BeanCellphone cellphone : cellphones) {
            Row row = sheet.createRow(rowNum++);

            // Información básica
            row.createCell(0).setCellValue(rowNum - 1); // Número de fila
            row.createCell(1).setCellValue(getSafeValue(cellphone.getEquipamentName()));
            row.createCell(2).setCellValue(getSafeValue(cellphone.getLegalName()));
            row.createCell(3).setCellValue(getSafeValue(cellphone.getCompany()));

            // Información de la persona asignada
            if (cellphone.getPerson() != null) {
                row.createCell(4).setCellValue(getSafeValue(
                        cellphone.getPerson().getName() + " " +
                                cellphone.getPerson().getSurname() + " " +
                                cellphone.getPerson().getLastname()
                ));
                row.createCell(5).setCellValue(getSafeValue(cellphone.getPerson().getDepartament()));
                row.createCell(6).setCellValue(getSafeValue(cellphone.getPerson().getPosition()));
            } else {
                row.createCell(4).setCellValue("SIN ASIGNAR");
                row.createCell(5).setCellValue("");
                row.createCell(6).setCellValue("");
            }

            // Datos del teléfono
            row.createCell(7).setCellValue(cellphone.getShortDialing());
            row.createCell(8).setCellValue(getSafeValue(cellphone.getNumber()));
            row.createCell(9).setCellValue(getSafeValue(cellphone.getImei()));

            // Formatear fecha de renovación
            Cell dateCell = row.createCell(10);
            if (cellphone.getDateRenovation() != null) {
                dateCell.setCellValue(cellphone.getDateRenovation());
                dateCell.setCellStyle(dateCellStyle);
            } else {
                dateCell.setCellValue("");
            }

            // Estado (con estilo condicional)
            Cell statusCell = row.createCell(11);
            if (cellphone.getStatus() != null) {
                statusCell.setCellValue(cellphone.getStatus() ? "Activo" : "Inactivo");

                // Aplicar color de fondo según el estado
                CellStyle statusStyle = workbook.createCellStyle();
                statusStyle.cloneStyleFrom(dateCellStyle);
                if (cellphone.getStatus()) {
                    statusStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
                } else {
                    statusStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
                }
                statusStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                statusCell.setCellStyle(statusStyle);
            } else {
                statusCell.setCellValue("Sin estado");
            }

            // WhatsApp Business
            row.createCell(12).setCellValue(cellphone.getWhatsappBussiness() != null ?
                    (cellphone.getWhatsappBussiness() ? "Sí" : "No") : "Sin info");

            // Comentarios
            row.createCell(13).setCellValue(getSafeValue(cellphone.getComments()));
        }

        // Congelar la fila de encabezados
        sheet.createFreezePane(0, 1, 0, 1);

        // Ajustar tamaño de columnas para todas las filas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            // Asegurar un ancho mínimo para columnas importantes
            if (i == 1 || i == 4 || i == 8 || i == 9) {
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

    private String getSafeValue(String value) {
        return value != null ? value : "";
    }



}
