package com.grupoeimsa.sigeim.models.responsives.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupoeimsa.sigeim.models.acess_cards.model.BeanAccessCard;
import com.grupoeimsa.sigeim.models.acess_cards.model.IAcessCard;
import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.cellphones.model.ICellphone;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.licenses.model.BeanLicense;
import com.grupoeimsa.sigeim.models.licenses.model.ILicense;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.DownloadResponsiveDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.GenerateResponsiveCellphoneDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.GenerateResponsiveDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.RequestGenerateAccessResponsiveDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.RequestSearchResponsiveEquipmentsDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseAvailableAccessDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseAvailableUsersTarjetasDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseEditResponsiveCellphoneDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseEditResponsiveEquipmentDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseResponsiveAccessDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseResponsiveCardsDTO;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseResponsiveCellphonesDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseResponsiveEquipmentsDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.UpdateResponsiveCellphoneDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.UpdateResponsiveDto;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveCards;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveCellphone;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveEquipaments;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveLicenses;
import com.grupoeimsa.sigeim.models.responsives.model.EStatus;
import com.grupoeimsa.sigeim.models.responsives.model.IResponsiveCards;
import com.grupoeimsa.sigeim.models.responsives.model.IResponsiveCellphone;
import com.grupoeimsa.sigeim.models.responsives.model.IResponsiveEquipments;
import com.grupoeimsa.sigeim.models.responsives.model.IResponsiveLicenses;
import com.grupoeimsa.sigeim.models.template_responsives.model.BeanTemplateResponsive;
import com.grupoeimsa.sigeim.models.template_responsives.model.ITemplate;
import com.grupoeimsa.sigeim.utils.CustomException;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFHeaderFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblOverlap;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.persistence.criteria.Predicate;

@Service
public class ResponsiveService {
    private final ITemplate templateRepository;
    private final IResponsiveEquipments responsiveEquipmentRepository;
    private final IComputerEquipament equipamentRepository;
    private final ICellphone cellphoneRepository;
    private final IResponsiveCellphone responsiveCellphoneRepository;
    private final ILicense licenseRepository;
    private final IResponsiveLicenses responsiveLicensesRepository;
    private final IResponsiveCards responsiveCardsRepository;
    private final IAcessCard acessCardRepository;

    public ResponsiveService(ITemplate templateRepository, IAcessCard acessCardRepository, IResponsiveLicenses responsiveLicensesRepository, ILicense licenseRepository, IResponsiveCellphone responsiveCellphoneRepository, ICellphone cellphoneRepository, IResponsiveEquipments responsiveEquipmentRepository, IComputerEquipament equipamentRepository, IResponsiveCards responsiveCardsRepository) {
        this.templateRepository = templateRepository;
        this.responsiveEquipmentRepository = responsiveEquipmentRepository;
        this.equipamentRepository = equipamentRepository;
        this.cellphoneRepository = cellphoneRepository;
        this.responsiveCellphoneRepository = responsiveCellphoneRepository;
        this.licenseRepository = licenseRepository;
        this.responsiveLicensesRepository = responsiveLicensesRepository;
        this.responsiveCardsRepository = responsiveCardsRepository;
        this.acessCardRepository = acessCardRepository;
    }


    public void generateResponsive(GenerateResponsiveDto dto) throws Exception {

        BeanTemplateResponsive template = templateRepository.findByTemplateName(dto.getTemplateName())
                .orElseThrow(() -> new CustomException("Plantilla no encontrada"));

        BeanComputerEquipament computerEquipament = equipamentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new CustomException("Equipo de cómputo no encontrado"));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(template.getTemplateFile());
        XWPFDocument document = new XWPFDocument(inputStream);

        // Reemplazar los textos de los marcadores en los párrafos
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceTextInParagraph(paragraph, dto.getPlaceholders());
        }

        // Reemplazar textos en celdas dentro de tablas
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replaceTextInParagraph(paragraph, dto.getPlaceholders());
                    }
                }
            }
        }

        // Llenar la tabla de equipos
        int tableIndex = 0; // Para rastrear el índice de la tabla
        for (XWPFTable table : document.getTables()) {
            if (tableIndex == 1) { // Modificar solo la segunda tabla
                if (table.getRows().size() > 1) {
                    List<XWPFTableRow> rows = table.getRows();

                    int rowIndex = 1; // La primera fila de datos (después de los encabezados)

                    for (Map<String, String> equip : dto.getEquipaments()) {
                        XWPFTableRow row;

                        // Si la fila ya existe, úsala; si no, créala
                        if (rowIndex < rows.size()) {
                            row = rows.get(rowIndex);
                        } else {
                            row = table.createRow();
                        }

                        int cellIndex = 0;
                        for (String header : new String[]{"Tipo", "Marca", "Modelo", "No. Serie", "No. Inventario", "Fecha"}) {
                            XWPFTableCell cell;

                            // Si la celda ya existe, reutilizarla; si no, crear una nueva
                            if (cellIndex < row.getTableCells().size()) {
                                cell = row.getCell(cellIndex);
                            } else {
                                cell = row.createCell();
                            }

                            // Limpiar el contenido existente de la celda
                            for (int i = cell.getParagraphs().size() - 1; i >= 0; i--) {
                                cell.removeParagraph(i);
                            }

                            // Crear un nuevo párrafo en la celda
                            XWPFParagraph paragraph = cell.addParagraph();
                            XWPFRun run = paragraph.createRun();
                            run.setText(equip.get(header));

                            // **Centrar el texto en la celda**
                            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                            paragraph.setAlignment(ParagraphAlignment.CENTER);

                            // Asegurar que todas las celdas tengan el mismo estilo de alineación
                            for (XWPFParagraph p : cell.getParagraphs()) {
                                p.setAlignment(ParagraphAlignment.CENTER);
                            }

                            cellIndex++;
                        }

                        rowIndex++;
                    }

                    // **Evitar que la tabla empuje el contenido siguiente**
                    CTTblPr tblPr = table.getCTTbl().getTblPr();
                    if (tblPr == null) {
                        tblPr = table.getCTTbl().addNewTblPr();
                    }

                    CTTblPPr tblpPr = tblPr.isSetTblpPr() ? tblPr.getTblpPr() : tblPr.addNewTblpPr();
                    tblpPr.setTblpX(BigInteger.valueOf(0));
                    tblpPr.setTblpY(BigInteger.valueOf(0));

                    // Configurar tblOverlap en CTTblPr
                    if (!tblPr.isSetTblOverlap()) {
                        tblPr.addNewTblOverlap();
                    }
                    tblPr.getTblOverlap().setVal(STTblOverlap.NEVER);

                    // Mantener el layout fijo para anchos de columna
                    CTTblLayoutType layoutType = tblPr.isSetTblLayout()
                            ? tblPr.getTblLayout()
                            : tblPr.addNewTblLayout();
                    layoutType.setType(STTblLayoutType.FIXED);
                    break;
                }
            }
            tableIndex++;
        }

        // Guardar el documento generado
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        byte[] generatedBytes = outputStream.toByteArray();

        BeanResponsiveEquipaments responsive = new BeanResponsiveEquipaments();
        responsive.setCreationDate(LocalDate.now());
        responsive.setEquipaments(new ObjectMapper().writeValueAsString(dto.getEquipaments()));
        responsive.setGeneratedDoc(generatedBytes);
        responsive.setStatus(EStatus.ACTIVA_POR_FIRMAR);
        responsive.setComputerEquipament(computerEquipament);

        responsive.setResponsibleName(dto.getPlaceholders().get("nombre"));
        responsive.setResponsibleDepartament(dto.getPlaceholders().get("departamento"));
        responsive.setResponsiblePosition(dto.getPlaceholders().get("puesto"));
        responsive.setBranch(dto.getPlaceholders().get("sucursal"));
        responsive.setDescription(dto.getPlaceholders().get("descripcion"));
        responsive.setObservations(dto.getPlaceholders().get("observaciones"));
        responsive.setWhoGives(dto.getPlaceholders().get("sistemas"));

        responsiveEquipmentRepository.save(responsive);

        document.close();
        inputStream.close();
        outputStream.close();
    }

    public void generateResponsiveCellphone(GenerateResponsiveCellphoneDto dto) throws Exception {
        BeanTemplateResponsive template = templateRepository.findByTemplateName(dto.getTemplateName())
                .orElseThrow(() -> new CustomException("Plantilla no encontrada"));

        BeanCellphone cellphone = cellphoneRepository.findById(dto.getCellphoneId())
                .orElseThrow(() -> new CustomException("Celular no encontrado"));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(template.getTemplateFile());
        XWPFDocument document = new XWPFDocument(inputStream);

        // Reemplazo de textos en párrafos
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceTextInParagraph(paragraph, dto.getPlaceholders());
        }

        // Reemplazo de textos en tablas
        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replaceTextInParagraph(paragraph, dto.getPlaceholders());
                    }
                }
            }
        }

        // Guardar documento generado
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        byte[] generatedBytes = outputStream.toByteArray();

        BeanResponsiveCellphone responsive = new BeanResponsiveCellphone();
        responsive.setCreationDate(LocalDate.now());
        responsive.setModificationDate(null);
        responsive.setResponsibleName(dto.getPlaceholders().get("nombre"));
        responsive.setResponsiblePosition(dto.getPlaceholders().get("puesto"));
        responsive.setWhatsGiven(dto.getPlaceholders().get("entregables"));
        responsive.setBrand(dto.getPlaceholders().get("marca"));
        responsive.setColor(dto.getPlaceholders().get("color"));
        responsive.setNumber(dto.getPlaceholders().get("numero"));
        responsive.setImei(dto.getPlaceholders().get("imei"));
        responsive.setPhoneState(dto.getPlaceholders().get("estado"));
        responsive.setStatus(EStatus.ACTIVA_POR_FIRMAR);
        responsive.setUploadedDoc(generatedBytes);
        responsive.setSignedDoc(null);
        responsive.setCellphone(cellphone);

        responsiveCellphoneRepository.save(responsive);

        document.close();
        inputStream.close();
        outputStream.close();
    }

    public void updateResponsive(UpdateResponsiveDto dto) throws Exception {
        BeanResponsiveEquipaments responsive = responsiveEquipmentRepository.findById(dto.getResponsiveId())
                .orElseThrow(() -> new CustomException("Responsiva no encontrada"));

        BeanTemplateResponsive template = templateRepository.findByTemplateName(dto.getTemplateName())
                .orElseThrow(() -> new CustomException("Plantilla no encontrada"));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(template.getTemplateFile());
        XWPFDocument document = new XWPFDocument(inputStream);

        // Reemplazo de texto
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceTextInParagraph(paragraph, dto.getPlaceholders());
        }

        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replaceTextInParagraph(paragraph, dto.getPlaceholders());
                    }
                }
            }
        }

        // Reemplazo de tabla de equipos (segunda tabla)
        int tableIndex = 0;
        for (XWPFTable table : document.getTables()) {
            if (tableIndex == 1) {
                List<XWPFTableRow> rows = table.getRows();
                int rowIndex = 1;

                for (Map<String, String> equip : dto.getEquipaments()) {
                    XWPFTableRow row = (rowIndex < rows.size()) ? rows.get(rowIndex) : table.createRow();
                    int cellIndex = 0;

                    for (String header : new String[]{"Tipo", "Marca", "Modelo", "No. Serie", "No. Inventario", "Fecha"}) {
                        XWPFTableCell cell = (cellIndex < row.getTableCells().size()) ? row.getCell(cellIndex) : row.createCell();
                        for (int i = cell.getParagraphs().size() - 1; i >= 0; i--) {
                            cell.removeParagraph(i);
                        }
                        XWPFParagraph paragraph = cell.addParagraph();
                        paragraph.setAlignment(ParagraphAlignment.CENTER);
                        XWPFRun run = paragraph.createRun();
                        run.setText(equip.get(header));
                        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                        cellIndex++;
                    }

                    rowIndex++;
                }

                tableIndex++;
                break;
            }
            tableIndex++;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        byte[] generatedBytes = outputStream.toByteArray();

        // Actualiza campos de la responsiva
        responsive.setEquipaments(new ObjectMapper().writeValueAsString(dto.getEquipaments()));
        responsive.setGeneratedDoc(generatedBytes);
        responsive.setResponsibleName(dto.getPlaceholders().get("nombre"));
        responsive.setResponsibleDepartament(dto.getPlaceholders().get("departamento"));
        responsive.setResponsiblePosition(dto.getPlaceholders().get("puesto"));
        responsive.setBranch(dto.getPlaceholders().get("sucursal"));
        responsive.setDescription(dto.getPlaceholders().get("descripcion"));
        responsive.setObservations(dto.getPlaceholders().get("observaciones"));
        responsive.setWhoGives(dto.getPlaceholders().get("sistemas"));
        responsive.setModificationDate(LocalDate.now());
        responsive.setStatus(EStatus.ACTIVA_POR_FIRMAR);

        responsiveEquipmentRepository.save(responsive);

        document.close();
        inputStream.close();
        outputStream.close();
    }



    public Page<ResponseResponsiveEquipmentsDto> getResponsivesEquipments(RequestSearchResponsiveEquipmentsDto dto) {
        Sort sort = dto.getSort().equalsIgnoreCase("asc")
                ? Sort.by("creationDate").ascending()
                : Sort.by("creationDate").descending();

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), sort);

        if (dto.getSearch() != null && dto.getSearch().trim().isEmpty()) {
            dto.setSearch(null);
        }

        Specification<BeanResponsiveEquipaments> spec = Specification.where(null);

        if (dto.getEstado() != null && !dto.getEstado().equalsIgnoreCase("Todos")) {
            EStatus statusEnum = switch (dto.getEstado()) {
                case "Activa y firmada" -> EStatus.ACTIVA_FIRMADA;
                case "Activa por firmar" -> EStatus.ACTIVA_POR_FIRMAR;
                case "Cancelada" -> EStatus.CANCELADA;
                default -> null;
            };
            if (statusEnum != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), statusEnum));
            }
        }

        if (dto.getSearch() != null && !dto.getSearch().trim().isEmpty()) {
            String term = "%" + dto.getSearch().toLowerCase() + "%";

            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("responsibleName")), term)
            );
        }

        Page<BeanResponsiveEquipaments> responsives = responsiveEquipmentRepository.findAll(spec, pageable);

        return responsives.map(r -> new ResponseResponsiveEquipmentsDto(
                r.getResponsiveEquipamentId(),
                r.getCreationDate().toString(),
                r.getResponsibleName(),
                r.getComputerEquipament().getSerialNumber(),
                r.getStatus().name().replace("_", " "),
                r.getSignedDoc() != null
        ));
    }

    public Page<ResponseResponsiveCellphonesDto> getResponsivesCellphones(RequestSearchResponsiveEquipmentsDto dto) {
        Sort sort = dto.getSort().equalsIgnoreCase("asc")
                ? Sort.by("creationDate").ascending()
                : Sort.by("creationDate").descending();

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), sort);

        if (dto.getSearch() != null && dto.getSearch().trim().isEmpty()) {
            dto.setSearch(null);
        }

        Specification<BeanResponsiveCellphone> spec = Specification.where(null);

        if (dto.getEstado() != null && !dto.getEstado().equalsIgnoreCase("Todos")) {
            EStatus statusEnum = switch (dto.getEstado()) {
                case "Activa y firmada" -> EStatus.ACTIVA_FIRMADA;
                case "Activa por firmar" -> EStatus.ACTIVA_POR_FIRMAR;
                case "Cancelada" -> EStatus.CANCELADA;
                default -> null;
            };
            if (statusEnum != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), statusEnum));
            }
        }

        if (dto.getSearch() != null && !dto.getSearch().trim().isEmpty()) {
            String term = "%" + dto.getSearch().toLowerCase() + "%";

            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("responsibleName")), term)
            );
        }

        Page<BeanResponsiveCellphone> responsives = responsiveCellphoneRepository.findAll(spec, pageable);

        return responsives.map(r -> new ResponseResponsiveCellphonesDto(
                r.getResponsiveCellphoneId(),
                r.getCreationDate().toString(),
                r.getResponsibleName(),
                r.getImei(),
                r.getStatus().name().replace("_", " "),
                r.getSignedDoc() != null
        ));
    }



    private void replaceTextInParagraph(XWPFParagraph paragraph, Map<String, String> placeholders) {
        StringBuilder fullText = new StringBuilder();
        List<XWPFRun> runs = paragraph.getRuns();

        if (runs.isEmpty()) {
            return; // Si no hay texto, salir
        }

        // Construir el texto completo del párrafo
        for (XWPFRun run : runs) {
            fullText.append(run.getText(0) != null ? run.getText(0) : "");
        }

        String updatedText = fullText.toString();

        // Reemplazar los placeholders en el texto completo
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            updatedText = updatedText.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        // Guardar el formato del primer run
        XWPFRun firstRun = runs.get(0);
        boolean isBold = firstRun.isBold();
        boolean isItalic = firstRun.isItalic();
        String fontFamily = firstRun.getFontFamily();
        int fontSize = firstRun.getFontSize();
        int color = firstRun.getColor() != null ? Integer.parseInt(firstRun.getColor(), 16) : -1;

        // Limpiar los runs existentes
        while (!paragraph.getRuns().isEmpty()) {
            paragraph.removeRun(0);
        }

        // Crear un nuevo run con el formato original
        XWPFRun newRun = paragraph.createRun();
        newRun.setText(updatedText);
        newRun.setBold(isBold);
        newRun.setItalic(isItalic);
        if (fontFamily != null) newRun.setFontFamily(fontFamily);
        if (fontSize > 0) newRun.setFontSize(fontSize);
        if (color != -1) newRun.setColor(String.format("%06X", color));

        // Mantener la alineación del párrafo
        paragraph.setAlignment(paragraph.getAlignment());

        if (updatedText.contains("Acepto Teléfono")) {
            paragraph.setAlignment(ParagraphAlignment.CENTER);
        } else {
            paragraph.setAlignment(paragraph.getAlignment()); // conservar
        }
    }


    public ResponseEntity<byte[]> downloadResponsive(DownloadResponsiveDto dto) {
        Optional<BeanResponsiveEquipaments> responsive = responsiveEquipmentRepository.findById(dto.getResponsiveId());

        if (responsive.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        byte[] documentBytes = responsive.get().getGeneratedDoc();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documento.docx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(documentBytes);
    }

    public ResponseEntity<byte[]> downloadResponsiveCellphone(DownloadResponsiveDto dto) {
        Optional<BeanResponsiveCellphone> responsive = responsiveCellphoneRepository.findById(dto.getResponsiveId());

        if (responsive.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        byte[] documentBytes = responsive.get().getUploadedDoc();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documento_responsiva_celular.docx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(documentBytes);
    }

    public ResponseEntity<byte[]> downloadResponsiveCard(DownloadResponsiveDto dto) {
        Optional<BeanResponsiveCards> responsive = responsiveCardsRepository.findById(dto.getResponsiveId());

        if (responsive.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        byte[] documentBytes = responsive.get().getGeneratedDoc();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documento_responsiva_celular.docx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(documentBytes);
    }

    public ResponseEntity<byte[]> downloadResponsiveAccess(DownloadResponsiveDto dto) {
        Optional<BeanResponsiveLicenses> responsive = responsiveLicensesRepository.findById(dto.getResponsiveId());

        if (responsive.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        byte[] documentBytes = responsive.get().getGeneratedDoc();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documento_responsiva_celular.docx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(documentBytes);
    }


    public ResponseEntity<ResponseEditResponsiveEquipmentDto> getEditResponsiveData(Long id) {
        Optional<BeanResponsiveEquipaments> optional = responsiveEquipmentRepository.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BeanResponsiveEquipaments responsive = optional.get();

        // Convertir la cadena JSON de equipos a lista de mapas
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> equipos = new ArrayList<>();

        try {
            equipos = mapper.readValue(responsive.getEquipaments(), new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResponseEditResponsiveEquipmentDto dto = new ResponseEditResponsiveEquipmentDto(
                responsive.getResponsibleName(),
                responsive.getResponsibleDepartament(),
                responsive.getResponsiblePosition(),
                responsive.getBranch(),
                responsive.getDescription(),
                responsive.getObservations(),
                responsive.getWhoGives(),
                equipos
        );

        return ResponseEntity.ok(dto);
    }

    public void uploadSignedDoc(Long id, MultipartFile file) throws IOException {
        BeanResponsiveEquipaments responsive = responsiveEquipmentRepository.findById(id)
                .orElseThrow(() -> new CustomException("Responsiva no encontrada"));

        // Guardar el nuevo archivo firmado
        responsive.setSignedDoc(file.getBytes());

        // Actualizar estatus a "ACTIVA_FIRMADA"
        responsive.setStatus(EStatus.ACTIVA_FIRMADA);

        // Registrar fecha de modificación
        responsive.setModificationDate(LocalDate.now());

        // Guardar cambios
        responsiveEquipmentRepository.save(responsive);
    }

    public void uploadSignedDocCellphone(Long responsiveId, MultipartFile file) throws IOException {
        BeanResponsiveCellphone responsive = responsiveCellphoneRepository.findById(responsiveId)
                .orElseThrow(() -> new CustomException("Responsiva de celular no encontrada"));

        responsive.setSignedDoc(file.getBytes());
        responsive.setStatus(EStatus.ACTIVA_FIRMADA);
        responsive.setModificationDate(LocalDate.now());

        responsiveCellphoneRepository.save(responsive);
    }

    public void uploadSignedDocCard(Long responsiveId, MultipartFile file) throws IOException {
        BeanResponsiveCards responsive = responsiveCardsRepository.findById(responsiveId)
                .orElseThrow(() -> new CustomException("Responsiva de celular no encontrada"));

        responsive.setSignedDoc(file.getBytes());
        responsive.setStatus(EStatus.ACTIVA_FIRMADA);

        responsiveCardsRepository.save(responsive);
    }

    public void uploadSignedDocAccess(Long responsiveId, MultipartFile file) throws IOException {
        BeanResponsiveLicenses responsive = responsiveLicensesRepository.findById(responsiveId)
                .orElseThrow(() -> new CustomException("Responsiva de accesos no encontrada"));

        responsive.setSignedDoc(file.getBytes());
        responsive.setStatus(EStatus.ACTIVA_FIRMADA);

        responsiveLicensesRepository.save(responsive);
    }



    public byte[] getSignedDocCellphone(Long responsiveId) {
        BeanResponsiveCellphone responsive = responsiveCellphoneRepository.findById(responsiveId)
                .orElseThrow(() -> new CustomException("Responsiva no encontrada"));

        if (responsive.getSignedDoc() == null) {
            throw new CustomException("La responsiva no tiene documento firmado.");
        }

        return responsive.getSignedDoc();
    }

    public byte[] getSignedDocAccess(Long responsiveId) {
        BeanResponsiveLicenses responsive = responsiveLicensesRepository.findById(responsiveId)
                .orElseThrow(() -> new CustomException("Responsiva no encontrada"));

        if (responsive.getSignedDoc() == null) {
            throw new CustomException("La responsiva no tiene documento firmado.");
        }

        return responsive.getSignedDoc();
    }

    public byte[] getSignedDocCard(Long responsiveId) {
        BeanResponsiveCards responsive = responsiveCardsRepository.findById(responsiveId)
                .orElseThrow(() -> new CustomException("Responsiva no encontrada"));

        if (responsive.getSignedDoc() == null) {
            throw new CustomException("La responsiva no tiene documento firmado.");
        }

        return responsive.getSignedDoc();
    }

    public void cancelResponsiveCellphone(Long responsiveId) {
        BeanResponsiveCellphone responsive = responsiveCellphoneRepository.findById(responsiveId)
                .orElseThrow(() -> new CustomException("Responsiva no encontrada"));

        responsive.setStatus(EStatus.CANCELADA);
        responsive.setModificationDate(LocalDate.now());

        responsiveCellphoneRepository.save(responsive);
    }

    public void cancelResponsiveCard(Long responsiveId) {
        BeanResponsiveCards responsive = responsiveCardsRepository.findById(responsiveId)
                .orElseThrow(() -> new CustomException("Responsiva no encontrada"));

        responsive.setStatus(EStatus.CANCELADA);

        responsiveCardsRepository.save(responsive);
    }

    public void cancelResponsiveAccess(Long responsiveId) {
        BeanResponsiveLicenses responsive = responsiveLicensesRepository.findById(responsiveId)
                .orElseThrow(() -> new CustomException("Responsiva no encontrada"));

        responsive.setStatus(EStatus.CANCELADA);

        responsiveLicensesRepository.save(responsive);
    }

    public void cancelResponsive(Long responsiveId) {
        BeanResponsiveEquipaments responsive = responsiveEquipmentRepository.findById(responsiveId)
                .orElseThrow(() -> new CustomException("Responsiva no encontrada"));

        responsive.setStatus(EStatus.CANCELADA);
        responsive.setModificationDate(LocalDate.now());

        responsiveEquipmentRepository.save(responsive);
    }

    public ResponseEditResponsiveCellphoneDto getResponsiveCellphoneData(DownloadResponsiveDto dto) {
        BeanResponsiveCellphone responsive = responsiveCellphoneRepository.findById(dto.getResponsiveId())
                .orElseThrow(() -> new CustomException("Responsiva no encontrada"));

        ResponseEditResponsiveCellphoneDto response = new ResponseEditResponsiveCellphoneDto();
        response.setResponsiveId(responsive.getResponsiveCellphoneId());
        response.setNombre(responsive.getResponsibleName());
        response.setPuesto(responsive.getResponsiblePosition());
        response.setEntregables(responsive.getWhatsGiven());
        response.setMarca(responsive.getBrand());
        response.setColor(responsive.getColor());
        response.setNumero(responsive.getNumber());
        response.setImei(responsive.getImei());
        response.setEstado(responsive.getPhoneState());
        response.setFecha(responsive.getCreationDate().toString());
        response.setCellphoneId(responsive.getCellphone().getCellphoneId());

        return response;
    }

    public void updateResponsiveCellphone(UpdateResponsiveCellphoneDto dto) throws Exception {
        BeanResponsiveCellphone responsive = responsiveCellphoneRepository.findById(dto.getResponsiveId())
                .orElseThrow(() -> new CustomException("Responsiva no encontrada"));

        BeanTemplateResponsive template = templateRepository.findByTemplateName(dto.getTemplateName())
                .orElseThrow(() -> new CustomException("Plantilla no encontrada"));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(template.getTemplateFile());
        XWPFDocument document = new XWPFDocument(inputStream);

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceTextInParagraph(paragraph, dto.getPlaceholders());
        }

        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replaceTextInParagraph(paragraph, dto.getPlaceholders());
                    }
                }
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.write(outputStream);
        byte[] generatedBytes = outputStream.toByteArray();

        // Actualizar campos
        responsive.setModificationDate(LocalDate.now());
        responsive.setResponsibleName(dto.getPlaceholders().get("nombre"));
        responsive.setResponsiblePosition(dto.getPlaceholders().get("puesto"));
        responsive.setWhatsGiven(dto.getPlaceholders().get("entregables"));
        responsive.setBrand(dto.getPlaceholders().get("marca"));
        responsive.setColor(dto.getPlaceholders().get("color"));
        responsive.setNumber(dto.getPlaceholders().get("numero"));
        responsive.setImei(dto.getPlaceholders().get("imei"));
        responsive.setPhoneState(dto.getPlaceholders().get("estado"));
        responsive.setStatus(EStatus.ACTIVA_POR_FIRMAR);
        responsive.setUploadedDoc(generatedBytes);
        responsive.setSignedDoc(null);

        // Si el celular también puede cambiar:
        BeanCellphone cellphone = cellphoneRepository.findById(dto.getCellphoneId())
                .orElseThrow(() -> new CustomException("Celular no encontrado"));
        responsive.setCellphone(cellphone);

        responsiveCellphoneRepository.save(responsive);

        document.close();
        inputStream.close();
        outputStream.close();
    }

    public List<ResponseAvailableAccessDto> getAvailableEmployeesForResponsive() {
        return licenseRepository.findAvailableForAccessResponsive()
                .stream()
                .map(license -> {
                    ResponseAvailableAccessDto dto = new ResponseAvailableAccessDto();
                    dto.setPersonId(license.getPerson().getPersonId());
                    dto.setFullName(license.getPerson().getFullName());
                    dto.setLicenseId(license.getLicensesId());
                    return dto;
                }).toList();
    }

    public List<ResponseAvailableUsersTarjetasDto> getAvailableUsersForTarjetas() {
        return responsiveCardsRepository.findAvailableUsersForTarjetas();
    }

    public void generateAccessResponsive(RequestGenerateAccessResponsiveDto dto) throws Exception {
        BeanLicense license = licenseRepository.findById(dto.getLicenseId())
                .orElseThrow(() -> new CustomException("Licencia no encontrada con id: " + dto.getLicenseId()));

        BeanTemplateResponsive template = templateRepository.findByTemplateName("Plantilla de licencias")
                .orElseThrow(() -> new CustomException("Plantilla 'Plantilla de licencias' no encontrada"));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(template.getTemplateFile());
        XWPFDocument document;

        try {
            try {
                document = new XWPFDocument(inputStream);
            } catch (IOException e) {
                throw new CustomException("Error al leer la plantilla Word: " + e.getMessage());
            }

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("nombre", license.getPerson().getFullName());
            placeholders.put("puesto", license.getPerson().getPosition());
            placeholders.put("fecha", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            try {
                for (XWPFParagraph p : document.getParagraphs()) {
                    replaceTextInParagraph(p, placeholders);
                }

                for (XWPFTable table : document.getTables()) {
                    for (XWPFTableRow row : table.getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            for (XWPFParagraph p : cell.getParagraphs()) {
                                replaceTextInParagraph(p, placeholders);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new CustomException("Error al reemplazar texto en la plantilla: " + e.getMessage());
            }

            if (document.getTables().size() < 2) {
                throw new CustomException("La plantilla debe contener al menos dos tablas.");
            }

            try {
                fillAccessTable(document, license);
            } catch (Exception e) {
                throw new CustomException("Error al llenar la tabla de accesos: " + e.getMessage());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                document.write(outputStream);
            } catch (IOException e) {
                throw new CustomException("Error al escribir el documento Word: " + e.getMessage());
            }

            BeanResponsiveLicenses responsive = new BeanResponsiveLicenses();
            responsive.setLicense(license);
            responsive.setCreationDate(LocalDate.now());
            responsive.setStatus(EStatus.ACTIVA_POR_FIRMAR);
            responsive.setGeneratedDoc(outputStream.toByteArray());
            responsive.setSignedDoc(new byte[0]);

            try {
                responsiveLicensesRepository.save(responsive);
            } catch (Exception e) {
                throw new CustomException("Error al guardar la responsiva en la base de datos: " + e.getMessage());
            }

            try {
                outputStream.close();
            } catch (IOException e) {
                throw new CustomException("Error al cerrar el flujo de salida: " + e.getMessage());
            }

        } catch (CustomException e) {
            throw e; // volver a lanzar para mantener la traza original
        } catch (Exception ex) {
            throw new CustomException("Error general durante la generación de la responsiva de accesos: " + ex.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new CustomException("Error al cerrar el flujo de entrada: " + e.getMessage());
            }
        }
    }



    private void fillAccessTable(XWPFDocument doc, BeanLicense license) {
        XWPFTable table = doc.getTables().get(0); // Usar la única tabla

        LinkedHashMap<String, String[]> platforms = new LinkedHashMap<>();

        // Plataformas obligatorias
        platforms.put("Outlook", new String[]{license.isOutlook() ? "Sí" : "NA", emptyOrNA(license.getAccountOutlook())});
        platforms.put("CRM", new String[]{license.isCrm() ? "Sí" : "NA", emptyOrNA(license.getUserCrm())});
        platforms.put("BC365", new String[]{license.isBc() ? "Sí" : "NA", emptyOrNA(license.getUserBc())});
        platforms.put("Pure Cloud", new String[]{license.isPurecloud() ? "Sí" : "NA", emptyOrNA(license.getUserPureCloud())});
        platforms.put("RPA", new String[]{license.isRpa() ? "Sí" : "NA", emptyOrNA(license.getUserRpa())});
        platforms.put("Permiso de USB", new String[]{license.isHasUsb() ? "Sí" : "NA", "NA"});


        // Plataformas adicionales activas
        Map<String, Boolean> dynamic = Map.ofEntries(
                Map.entry("Power BI", license.isPowerbi()),
                Map.entry("Copilot", license.isCopilot()),
                Map.entry("Tactical", license.isTactical()),
                Map.entry("Instagram", license.isInstagram()),
                Map.entry("Facebook", license.isFacebook()),
                Map.entry("Tiktok", license.isTiktok()),
                Map.entry("Linkedin", license.isLinkedin()),
                Map.entry("YouTube", license.isYoutube()),
                Map.entry("Adobe", license.isAdobe()),
                Map.entry("Mailchimp", license.isMailchimp()),
                Map.entry("Linktree", license.isLinktree()),
                Map.entry("Magento", license.isMagento()),
                Map.entry("Shopify", license.isShopify()),
                Map.entry("Mercado Libre", license.isMercadoLibre()),
                Map.entry("Amazon", license.isAmazon()),
                Map.entry("Conekta", license.isConekta()),
                Map.entry("Open Pay", license.isOpenPay()),
                Map.entry("Kuesky", license.isKuesky())
        );

        for (Map.Entry<String, Boolean> entry : dynamic.entrySet()) {
            if (entry.getValue()) {
                String platform = entry.getKey();
                String user = switch (platform) {
                    case "Instagram" -> emptyOrNA(license.getUserInstagram());
                    case "Facebook" -> emptyOrNA(license.getUserFacebook());
                    case "Tiktok" -> emptyOrNA(license.getUserTiktok());
                    case "Linkedin" -> emptyOrNA(license.getUserLinkedin());
                    case "YouTube" -> emptyOrNA(license.getUserYoutube());
                    case "Magento" -> emptyOrNA(license.getMagentoUser());
                    case "Shopify" -> emptyOrNA(license.getUserShopify());
                    default -> "NA";
                };
                platforms.put(platform, new String[]{"Sí", user});
            }
        }

        // Eliminar filas existentes después del header
        while (table.getNumberOfRows() > 1) {
            table.removeRow(1);
        }

        // Estilo para cada celda
        for (Map.Entry<String, String[]> entry : platforms.entrySet()) {
            XWPFTableRow row = table.createRow();

            List<String> valores = List.of(entry.getKey(), entry.getValue()[0], entry.getValue()[1]);

            for (int i = 0; i < 3; i++) {
                XWPFTableCell cell = row.getCell(i);

                // Limpiar párrafos anteriores
                cell.removeParagraph(0);

                XWPFParagraph paragraph = cell.addParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                XWPFRun run = paragraph.createRun();
                run.setFontFamily("Arial");
                run.setFontSize(12);
                run.setText(valores.get(i));
            }
        }
    }

    private String emptyOrNA(String value) {
        return (value == null || value.trim().isEmpty() || value.equalsIgnoreCase("NA")) ? "NA" : value;
    }

    public Page<ResponseResponsiveAccessDto> getResponsivesAccess(RequestSearchResponsiveEquipmentsDto dto) {
        Sort sort = dto.getSort().equalsIgnoreCase("asc")
                ? Sort.by("creationDate").ascending()
                : Sort.by("creationDate").descending();

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), sort);

        if (dto.getSearch() != null && dto.getSearch().trim().isEmpty()) {
            dto.setSearch(null);
        }

        Specification<BeanResponsiveLicenses> spec = Specification.where(null);

        if (dto.getEstado() != null && !dto.getEstado().equalsIgnoreCase("Todos")) {
            EStatus statusEnum = switch (dto.getEstado()) {
                case "Activa y firmada" -> EStatus.ACTIVA_FIRMADA;
                case "Activa por firmar" -> EStatus.ACTIVA_POR_FIRMAR;
                case "Cancelada" -> EStatus.CANCELADA;
                default -> null;
            };
            if (statusEnum != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), statusEnum));
            }
        }

        if (dto.getSearch() != null && !dto.getSearch().trim().isEmpty()) {
            String term = "%" + dto.getSearch().toLowerCase() + "%";

            spec = spec.and((root, query, cb) -> {
                Join<Object, Object> license = root.join("license");
                Join<Object, Object> person = license.join("person");

                Expression<String> fullName = cb.lower(cb.concat(
                        cb.concat(
                                cb.concat(person.get("name"), " "),
                                cb.concat(person.get("lastname"), " ")
                        ),
                        person.get("surname")
                ));

                return cb.like(fullName, term);
            });
        }


        Page<BeanResponsiveLicenses> responsives = responsiveLicensesRepository.findAll(spec, pageable);

        return responsives.map(r -> new ResponseResponsiveAccessDto(
                r.getResponsiveCellphoneId(),
                r.getCreationDate().toString(),
                r.getLicense().getPerson().getFullName(),
                r.getStatus().name().replace("_", " "),
                r.getSignedDoc() != null && r.getSignedDoc().length > 0
        ));
    }


    public void generateCardResponsive(RequestGenerateAccessResponsiveDto dto) throws Exception {
        BeanAccessCard card = acessCardRepository.findByPersonPersonId(dto.getPersonId())
                .orElseThrow(() -> new CustomException("Tarjeta de acceso no encontrada para el empleado con ID: " + dto.getPersonId()));

        BeanTemplateResponsive template = templateRepository.findByTemplateName("Plantilla de tarjetas")
                .orElseThrow(() -> new CustomException("Plantilla 'Plantilla de tarjetas' no encontrada"));

        ByteArrayInputStream inputStream = new ByteArrayInputStream(template.getTemplateFile());
        XWPFDocument document;

        try {
            document = new XWPFDocument(inputStream);
        } catch (IOException e) {
            throw new CustomException("Error al leer la plantilla Word: " + e.getMessage());
        }

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("nombre", card.getPerson().getFullName());
        placeholders.put("puesto", card.getPerson().getPosition());
        placeholders.put("fecha", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Flags de acceso
        placeholders.put("accesoPuerta", toYesNo(card.isAccessBetweenBuildings()));
        placeholders.put("accesoPrincipal", toYesNo(card.isMainDoor()));
        placeholders.put("accesoServicio", toYesNo(card.isAccessTechnicalService()));
        placeholders.put("accesoServicio1", toYesNo(card.isTechnicalServiceWarehouses()));
        placeholders.put("accesoServicio2", toYesNo(card.isTechnicalServiceWarehousesTwo()));
        placeholders.put("accesoAlmacenP", toYesNo(card.isMainWarehouse()));
        placeholders.put("accesoAlmacenS", toYesNo(card.isWarehouseBasement()));

        try {
            // Reemplazar texto
            for (XWPFParagraph p : document.getParagraphs()) {
                replaceTextInParagraph(p, placeholders);
            }

            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            replaceTextInParagraph(p, placeholders);
                        }
                    }
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.write(outputStream);
            document.close();

            BeanResponsiveCards responsive = new BeanResponsiveCards();
            responsive.setAccessCard(card);
            responsive.setCreationDate(LocalDate.now());
            responsive.setStatus(EStatus.ACTIVA_POR_FIRMAR);
            responsive.setGeneratedDoc(outputStream.toByteArray());
            responsive.setSignedDoc(null);

            responsiveCardsRepository.save(responsive);
        } catch (Exception ex) {
            throw new CustomException("Error durante la generación de la responsiva de tarjetas: " + ex.getMessage());
        } finally {
            inputStream.close();
        }
    }

    private String toYesNo(boolean flag) {
        return flag ? "Sí" : "No";
    }


    public Page<ResponseResponsiveCardsDTO> getResponsiveCards(RequestSearchResponsiveEquipmentsDto dto) {
        Sort sort = dto.getSort().equalsIgnoreCase("asc")
                ? Sort.by("creationDate").ascending()
                : Sort.by("creationDate").descending();

        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), sort);

        if (dto.getSearch() != null && dto.getSearch().trim().isEmpty()) {
            dto.setSearch(null);
        }

        Specification<BeanResponsiveCards> spec = Specification.where(null);

        if (dto.getEstado() != null && !dto.getEstado().equalsIgnoreCase("Todos")) {
            EStatus statusEnum = switch (dto.getEstado()) {
                case "Activa y firmada" -> EStatus.ACTIVA_FIRMADA;
                case "Activa por firmar" -> EStatus.ACTIVA_POR_FIRMAR;
                case "Cancelada" -> EStatus.CANCELADA;
                default -> null;
            };
            if (statusEnum != null) {
                spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), statusEnum));
            }
        }

        if (dto.getSearch() != null && !dto.getSearch().trim().isEmpty()) {
            String term = "%" + dto.getSearch().toLowerCase() + "%";

            spec = spec.and((root, query, cb) -> {
                Join<Object, Object> accessCard = root.join("accessCard");
                Join<Object, Object> person = accessCard.join("person");

                Expression<String> fullName = cb.lower(
                        cb.concat(
                                cb.concat(
                                        cb.concat(person.get("name"), " "),
                                        cb.concat(person.get("lastname"), " ")
                                ),
                                person.get("surname")
                        )
                );

                return cb.like(fullName, term);
            });
        }


        Page<BeanResponsiveCards> responsives = responsiveCardsRepository.findAll(spec, pageable);

        return responsives.map(r -> {
            BeanPerson person = r.getAccessCard().getPerson();
            String fullName = person != null ? person.getFullName().replace(" NA NA", "") : "Desconocido";

            return new ResponseResponsiveCardsDTO(
                    r.getResponsiveCardId(),
                    r.getCreationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    fullName,
                    r.getStatus().name().replace("_", " "),
                    r.getSignedDoc() != null && r.getSignedDoc().length > 0
            );
        });
    }

}
