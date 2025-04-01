package com.grupoeimsa.sigeim.models.responsives.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.DownloadResponsiveDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.GenerateResponsiveDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.RequestSearchResponsiveEquipmentsDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseEditResponsiveEquipmentDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseResponsiveEquipmentsDto;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveEquipaments;
import com.grupoeimsa.sigeim.models.responsives.model.EStatus;
import com.grupoeimsa.sigeim.models.responsives.model.IResponsiveEquipments;
import com.grupoeimsa.sigeim.models.template_responsives.model.BeanTemplateResponsive;
import com.grupoeimsa.sigeim.models.template_responsives.model.ITemplate;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ResponsiveService {
    private final ITemplate templateRepository;
    private final IResponsiveEquipments responsiveEquipmentRepository;
    private final IComputerEquipament equipamentRepository;

    public ResponsiveService(ITemplate templateRepository, IResponsiveEquipments responsiveEquipmentRepository, IComputerEquipament equipamentRepository) {
        this.templateRepository = templateRepository;
        this.responsiveEquipmentRepository = responsiveEquipmentRepository;
        this.equipamentRepository = equipamentRepository;
    }


    public void generateResponsive(GenerateResponsiveDto dto) throws Exception {

        BeanTemplateResponsive template = templateRepository.findByTemplateName(dto.getTemplateName())
                .orElseThrow(() -> new RuntimeException("Plantilla no encontrada"));

        BeanComputerEquipament computerEquipament = equipamentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Equipo de cómputo no encontrado"));

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

    public Page<ResponseResponsiveEquipmentsDto> getResponsivesEquipments(RequestSearchResponsiveEquipmentsDto dto) {
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize());
        EStatus statusEnum = null;

        if (dto.getEstado() != null && !dto.getEstado().equalsIgnoreCase("Todos")) {
            statusEnum = switch (dto.getEstado()) {
                case "Activa y firmada" -> EStatus.ACTIVA_FIRMADA;
                case "Activa por firmar" -> EStatus.ACTIVA_POR_FIRMAR;
                case "Cancelada" -> EStatus.CANCELADA;
                default -> null;
            };
        }

        Page<BeanResponsiveEquipaments> responsives = responsiveEquipmentRepository.searchResponsives(
                dto.getSearch(),
                statusEnum,
                dto.getSort(),
                pageable
        );

        return responsives.map(r -> new ResponseResponsiveEquipmentsDto(
                r.getResponsiveEquipamentId(),
                r.getCreationDate().toString(),
                r.getResponsibleName(),
                r.getComputerEquipament().getSerialNumber(),
                r.getStatus().name().replace("_", " ")
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
}
