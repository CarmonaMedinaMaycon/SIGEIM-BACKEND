package com.grupoeimsa.sigeim.models.responsives.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.DownloadResponsiveDto;
import com.grupoeimsa.sigeim.models.responsives.controller.dto.GenerateResponsiveDto;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.time.LocalDate;
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




//    public void generateResponsive(GenerateResponsiveDto dto) throws Exception {
//        BeanTemplateResponsive template = templateRepository.findById(dto.getTemplateId())
//                .orElseThrow(() -> new RuntimeException("Plantilla no encontrada"));
//
//        BeanComputerEquipament computerEquipament = equipamentRepository.findById(dto.getEquipmentId())
//                .orElseThrow(() -> new RuntimeException("Equipo de cómputo no encontrado"));
//
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(template.getTemplateFile());
//        XWPFDocument document = new XWPFDocument(inputStream);
//
//        // Buscar y reemplazar marcadores en
//        replaceTextInDocument(document, dto.getPlaceholders());
//
//        // Llenar la tabla de equipos (segunda tabla en el documento)
//        fillEquipmentTable(document, dto.getEquipaments());
//
//        // Guardar el documento generado
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        document.write(outputStream);
//        byte[] generatedBytes = outputStream.toByteArray();
//
//        BeanResponsiveEquipaments responsive = new BeanResponsiveEquipaments();
//        responsive.setDate(LocalDate.now());
//        responsive.setEquipaments(new ObjectMapper().writeValueAsString(dto.getEquipaments()));
//        responsive.setGeneratedDoc(generatedBytes);
//        responsive.setStatus(EStatus.ACTIVA);
//        responsive.setComputerEquipament(computerEquipament);
//
//        responsiveEquipmentRepository.save(responsive);
//
//        document.close();
//        inputStream.close();
//        outputStream.close();
//    }
//
//    private void replaceTextInDocument(XWPFDocument document, Map<String, String> placeholders) {
//        // Buscar y reemplazar en párrafos del documento
//        for (XWPFParagraph paragraph : document.getParagraphs()) {
//            replaceTextInParagraph(paragraph, placeholders);
//        }
//
//        // Buscar y reemplazar en tablas
//        for (XWPFTable table : document.getTables()) {
//            for (XWPFTableRow row : table.getRows()) {
//                for (XWPFTableCell cell : row.getTableCells()) {
//                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
//                        replaceTextInParagraph(paragraph, placeholders);
//                    }
//                }
//            }
//        }
//
//        // Buscar y reemplazar en encabezados
//        for (XWPFHeader header : document.getHeaderList()) {
//            for (XWPFParagraph paragraph : header.getParagraphs()) {
//                replaceTextInParagraph(paragraph, placeholders);
//            }
//        }
//
//        // Buscar y reemplazar en pies de página
//        for (XWPFFooter footer : document.getFooterList()) {
//            for (XWPFParagraph paragraph : footer.getParagraphs()) {
//                replaceTextInParagraph(paragraph, placeholders);
//            }
//        }
//    }
//
//    private void replaceTextInParagraph(XWPFParagraph paragraph, Map<String, String> placeholders) {
//        for (XWPFRun run : paragraph.getRuns()) {
//            String text = run.getText(0);
//            if (text != null) {
//                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
//                    text = text.replace("<" + entry.getKey() + ">", entry.getValue());
//                }
//                run.setText(text, 0);
//            }
//        }
//    }
//
//    private void fillEquipmentTable(XWPFDocument document, List<Map<String, String>> equipaments) {
//        int tableIndex = 0;
//        for (XWPFTable table : document.getTables()) {
//            if (tableIndex == 1) { // Modificar solo la segunda tabla
//                if (table.getRows().size() > 1) {
//                    List<XWPFTableRow> rows = table.getRows();
//                    int rowIndex = 1;
//
//                    for (Map<String, String> equip : equipaments) {
//                        XWPFTableRow row;
//
//                        if (rowIndex < rows.size()) {
//                            row = rows.get(rowIndex);
//                        } else {
//                            row = table.createRow();
//                        }
//
//                        int cellIndex = 0;
//                        for (String header : new String[]{"Tipo", "Marca", "Modelo", "No. Serie", "No. Inventario", "Fecha"}) {
//                            XWPFTableCell cell;
//
//                            if (cellIndex < row.getTableCells().size()) {
//                                cell = row.getCell(cellIndex);
//                            } else {
//                                cell = row.createCell();
//                            }
//
//                            for (int i = cell.getParagraphs().size() - 1; i >= 0; i--) {
//                                cell.removeParagraph(i);
//                            }
//
//                            XWPFParagraph paragraph = cell.addParagraph();
//                            XWPFRun run = paragraph.createRun();
//                            run.setText(equip.get(header));
//
//                            cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
//                            paragraph.setAlignment(ParagraphAlignment.CENTER);
//
//                            for (XWPFParagraph p : cell.getParagraphs()) {
//                                p.setAlignment(ParagraphAlignment.CENTER);
//                            }
//
//                            cellIndex++;
//                        }
//
//                        rowIndex++;
//                    }
//
//                    CTTblPr tblPr = table.getCTTbl().getTblPr();
//                    if (tblPr == null) {
//                        tblPr = table.getCTTbl().addNewTblPr();
//                    }
//
//                    CTTblPPr tblpPr = tblPr.isSetTblpPr() ? tblPr.getTblpPr() : tblPr.addNewTblpPr();
//                    tblpPr.setTblpX(BigInteger.valueOf(0));
//                    tblpPr.setTblpY(BigInteger.valueOf(0));
//
//                    if (!tblPr.isSetTblOverlap()) {
//                        tblPr.addNewTblOverlap();
//                    }
//                    tblPr.getTblOverlap().setVal(STTblOverlap.NEVER);
//
//                    CTTblLayoutType layoutType = tblPr.isSetTblLayout()
//                            ? tblPr.getTblLayout()
//                            : tblPr.addNewTblLayout();
//                    layoutType.setType(STTblLayoutType.FIXED);
//                    break;
//                }
//            }
//            tableIndex++;
//        }
//    }


    public void generateResponsive(GenerateResponsiveDto dto) throws Exception {
        BeanTemplateResponsive template = templateRepository.findById(dto.getTemplateId())
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
        responsive.setDate(LocalDate.now());
        responsive.setEquipaments(new ObjectMapper().writeValueAsString(dto.getEquipaments()));
        responsive.setGeneratedDoc(generatedBytes);
        responsive.setStatus(EStatus.ACTIVA);
        responsive.setComputerEquipament(computerEquipament);

        responsiveEquipmentRepository.save(responsive);

        document.close();
        inputStream.close();
        outputStream.close();
    }

    private void replaceTextInParagraph(XWPFParagraph paragraph, Map<String, String> placeholders) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                    text = text.replace("<<" + entry.getKey() + ">>", entry.getValue());
                }
                run.setText(text, 0);
            }
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
}
