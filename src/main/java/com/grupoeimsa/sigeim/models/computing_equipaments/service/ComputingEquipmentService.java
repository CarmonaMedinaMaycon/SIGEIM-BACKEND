package com.grupoeimsa.sigeim.models.computing_equipaments.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestRegisterComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestSearchByFilteringEquipmentsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestUpdateComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.ResponseSeeAllEquipmentsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.ResponseSeeDetailsEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.CEStatus;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.IComputerEquipament;
import com.grupoeimsa.sigeim.models.history_photos.model.BeanHistoryPhotosEquipament;
import com.grupoeimsa.sigeim.models.history_photos.model.controller.dto.HistoryEquipmentPhotosGroupDto;
import com.grupoeimsa.sigeim.models.invoices.controller.dto.InvoiceDto;
import com.grupoeimsa.sigeim.models.invoices.model.BeanInvoice;
import com.grupoeimsa.sigeim.models.invoices.service.InvoiceService;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComputingEquipmentService {
    private final IComputerEquipament repository;
    private final IPerson personRepository;
    private final BeanHistoryPhotosEquipament beanHistoryPhotosEquipament;
    private final InvoiceService invoiceService;

    public ComputingEquipmentService(IComputerEquipament repository, IPerson personRepository, BeanHistoryPhotosEquipament beanHistoryPhotosEquipament, InvoiceService invoiceService) {
        this.repository = repository;
        this.personRepository = personRepository;
        this.beanHistoryPhotosEquipament = beanHistoryPhotosEquipament;
        this.invoiceService = invoiceService;
    }

    @Transactional
    public String createComputingEquipment(RequestRegisterComputingEquipmentDto dto) throws IOException {

        // Crear equipo
        BeanComputerEquipament equipment = new BeanComputerEquipament();
        equipment.setSerialNumber(dto.getSerialNumber());
        equipment.setIdEsset(dto.getIdEsset());

        BeanPerson person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new CustomException("Person not found with ID: " + dto.getPersonId()));

        ComputerData(equipment, person, dto.getDepartament(), dto.getEnterprise(), dto.getWorkModality(),
                dto.getType(), dto.getBrand(), dto.getModel(), dto.getRamMemoryCapacity(), dto.getMemoryCapacity(),
                dto.getProcessor(), dto.getPurchasingCompany(), dto);

        equipment.setStatus(Objects.equals(person.getName(), "Sistemas") ? CEStatus.DISPONIBLE : CEStatus.OCUPADO);

        equipment.setHasInvoice(dto.getHasInvoice());
        equipment.setSupplier(dto.getSupplier());
        equipment.setInvoiceFolio(dto.getInvoiceFolio());
        equipment.setPurchaseDate(dto.getPurchaseDate());
        equipment.setAssetNumber(dto.getAssetNumber());
        equipment.setPrice(dto.getPrice());
        equipment.setCreationDate(LocalDate.now());
        equipment.setSystemObservations(dto.getSystemObservations());

        // Manejo de la factura solo si tiene factura
        if (Boolean.TRUE.equals(dto.getHasInvoice())) {
            Optional<BeanInvoice> existingInvoice = invoiceService.findByInvoiceFolio(dto.getInvoiceFolio());

            if (existingInvoice.isPresent()) {
                // Si la factura existe, la asociamos
                equipment.setInvoice(existingInvoice.get());
            } else {
                // Si no existe, la creamos
                BeanInvoice newInvoice = new BeanInvoice();
                newInvoice.setInvoiceFolio(dto.getInvoiceFolio());
                newInvoice.setSupplier(dto.getSupplier());
                newInvoice.setInvoiceDate(dto.getInvoiceDate());
                newInvoice.setPriceIva(dto.getPriceIva());

                // Guardar archivo si viene
                if (dto.getFile() != null && !dto.getFile().isEmpty()) {
                    newInvoice.setInvoiceFile(dto.getFile().getBytes());
                }

                // Guardar la nueva factura
                BeanInvoice savedInvoice = invoiceService.saveInvoice(newInvoice);
                equipment.setInvoice(savedInvoice);
            }
        }

        repository.save(equipment);
        return "Equipo registrado con éxito";
    }


    private void ComputerData(BeanComputerEquipament equipment, BeanPerson person, String departament, String enterprise, String workModality, String type, String brand, String model, Long ramMemoryCapacity, Long memoryCapacity, String processor, String purchasingCompany, Object dto) {
        equipment.setPerson(person);
        equipment.setDepartament(departament);
        equipment.setEnterprise(enterprise);
        equipment.setWorkModality(workModality);
        equipment.setType(type);
        equipment.setBrand(brand);
        equipment.setModel(model);
        equipment.setRamMemoryCapacity(ramMemoryCapacity);
        equipment.setMemoryCapacity(memoryCapacity);
        equipment.setProcessor(processor);
        equipment.setPurchasingCompany(purchasingCompany);

        if (dto instanceof RequestRegisterComputingEquipmentDto) {
            RequestRegisterComputingEquipmentDto registerDto = (RequestRegisterComputingEquipmentDto) dto;
        } else if (dto instanceof RequestUpdateComputingEquipmentDto) {
            RequestUpdateComputingEquipmentDto updateDto = (RequestUpdateComputingEquipmentDto) dto;
        }
    }


    @Transactional
    public String updateComputingEquipment(RequestUpdateComputingEquipmentDto dto) {
        BeanComputerEquipament equipment = repository.findById(dto.getId())
                .orElseThrow(() -> new CustomException("Equipo no encontrado con ID: " + dto.getId()));

        BeanPerson person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new CustomException("Responsable no encontrado con ID: " + dto.getPersonId()));

        equipment.setSerialNumber(dto.getSerialNumber());
        equipment.setIdEsset(dto.getIdEsset());
        ComputerData(equipment, person, dto.getDepartament(), dto.getEnterprise(), dto.getWorkModality(), dto.getType(), dto.getBrand(), dto.getModel(), dto.getRamMemoryCapacity(), dto.getMemoryCapacity(), dto.getProcessor(), dto.getPurchasingCompany(), dto);
        equipment.setSupplier(dto.getSupplier());
        equipment.setInvoiceFolio(dto.getInvoiceFolio());
        equipment.setPurchaseDate(dto.getPurchaseDate());
        equipment.setAssetNumber(dto.getAssetNumber());
        equipment.setPrice(dto.getPrice());
        equipment.setSystemObservations(dto.getSystemObservations());

        equipment.setLastUpdateDate(LocalDate.now());

        repository.save(equipment);
        return "Equipo actualizado con éxito";
    }

    public Page<ResponseSeeAllEquipmentsDto> getAllEquipments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<BeanComputerEquipament> equipmentPage = repository.findAll(pageable);

        return equipmentPage.map(equipment -> {
            ResponseSeeAllEquipmentsDto dto = new ResponseSeeAllEquipmentsDto();
            dto.setSerialNumber(equipment.getSerialNumber());
            dto.setIdEsset(equipment.getIdEsset());
            dto.setResponsibleName(equipment.getPerson().getName() + " " + equipment.getPerson().getSurname());
            dto.setDepartament(equipment.getDepartament());
            dto.setType(equipment.getType());
            dto.setBrand(equipment.getBrand());
            dto.setEquipmentStatus(equipment.getStatus().toString());
            return dto;
        });
    }

    public List<ResponseSeeAllEquipmentsDto> searchEquipments(String searchQuery) {
        return repository.findBySearchQuery(searchQuery)
                .stream()
                .map(this::getResponseSeeAllEquipmentsDto)
                .collect(Collectors.toList());
    }


    private ResponseSeeAllEquipmentsDto getResponseSeeAllEquipmentsDto(BeanComputerEquipament equipo) {
        ResponseSeeAllEquipmentsDto dto = new ResponseSeeAllEquipmentsDto();
        dto.setSerialNumber(equipo.getSerialNumber());
        dto.setIdEsset(equipo.getIdEsset());
        dto.setResponsibleName(equipo.getPerson() != null ? equipo.getPerson().getName() + " " + equipo.getPerson().getLastname() + " " + equipo.getPerson().getSurname() : "");
        dto.setDepartament(equipo.getDepartament());
        dto.setType(equipo.getType());
        dto.setBrand(equipo.getBrand());
        dto.setEquipmentStatus(equipo.getStatus().name());
        return dto;
    }

    public Map<String, List<String>> getAvailableFilters() {
        Map<String, List<String>> filters = new HashMap<>();

        filters.put("tipos", repository.findDistinctTypes());
        filters.put("proveedores", repository.findDistinctSuppliers());
        filters.put("estados", Arrays.stream(CEStatus.values()).map(Enum::name).toList());
        filters.put("marcas", repository.findDistinctBrands());

        return filters;
    }

    public Page<ResponseSeeAllEquipmentsDto> searchEquipmentsFiltering(RequestSearchByFilteringEquipmentsDto filtros) {
        CEStatus equipmentStatus = null;
        if (filtros.getEquipmentStatus() != null && !filtros.getEquipmentStatus().equalsIgnoreCase("Todos")) {
            try {
                equipmentStatus = CEStatus.valueOf(filtros.getEquipmentStatus());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status value: " + filtros.getEquipmentStatus());
            }
        }

        int page = (filtros.getPage() != null) ? filtros.getPage() : 0;
        int size = (filtros.getSize() != null) ? filtros.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "computerEquipamentId"));

        Page<BeanComputerEquipament> equipos = repository.findByFilters(
                Optional.ofNullable(filtros.getType()).filter(type -> !type.equalsIgnoreCase("Todos")).orElse(null),
                Optional.ofNullable(filtros.getSupplier()).filter(supplier -> !supplier.equalsIgnoreCase("Todos")).orElse(null),
                Optional.ofNullable(filtros.getBrand()).filter(brand -> !brand.equalsIgnoreCase("Todos")).orElse(null),
                equipmentStatus,
                pageable
        );

        return equipos.map(this::getResponseSeeAllEquipmentsDto);
    }

    public ResponseSeeDetailsEquipmentDto getEquipamentDetails(Long id) {
        BeanComputerEquipament equipo = repository.findById(id)
                .orElseThrow(() -> new CustomException("Equipment not found with id: " + id));

        Map<String, List<String>> groupedPhotos = equipo.getHistoryPhotosEquipament().stream()
                .collect(Collectors.groupingBy(
                        p -> p.getDate() + " - " + p.getPersonName(),
                        Collectors.mapping(p -> Base64.getEncoder().encodeToString(p.getPhoto()), Collectors.toList())
                ));


        List<HistoryEquipmentPhotosGroupDto> historyPhotos = groupedPhotos.entrySet().stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split(" - ");
                    return new HistoryEquipmentPhotosGroupDto(LocalDate.parse(parts[0]), parts[1], entry.getValue());
                })
                .toList();

        return new ResponseSeeDetailsEquipmentDto(
                equipo.getSerialNumber(),
                equipo.getIdEsset(),
                equipo.getPerson().getFullName(),
                equipo.getDepartament(),
                equipo.getEnterprise(),
                equipo.getWorkModality(),
                equipo.getType(),
                equipo.getBrand(),
                equipo.getModel(),
                equipo.getRamMemoryCapacity(),
                equipo.getMemoryCapacity(),
                equipo.getProcessor(),
                equipo.getPurchasingCompany(),
                equipo.getHasInvoice(),
                equipo.getSupplier(),
                equipo.getInvoiceFolio(),
                equipo.getPurchaseDate(),
                equipo.getAssetNumber(),
                equipo.getPrice(),
                equipo.getStatus().toString(),
                equipo.getSystemObservations(),
                equipo.getCreationDate(),
                equipo.getLastUpdateDate()
        );
    }

    @Transactional
    public String changeStatus(Long equipamentId, CEStatus newStatus) {
        BeanComputerEquipament equipament = repository.findById(equipamentId)
                .orElseThrow(() -> new CustomException("Equipo no encontrado con ID: " + equipamentId));

        if (equipament.getStatus().equals(newStatus)) {
            throw new CustomException("El estado ya es el mismo que el actual");
        }

        if (equipament.getPerson().getName().equals("Sistemas") && newStatus.equals(CEStatus.OCUPADO)){
            throw new CustomException("El estado no puede pasar a ocupado si el responsable es sistemas");
        }

        if (!equipament.getPerson().getName().equals("Sistemas") && newStatus.equals(CEStatus.DISPONIBLE)){
            throw new CustomException("El estado no puede pasar a disponible si tiene un responsable asignado");
        }

        if (!equipament.getPerson().getName().equals("Sistemas") && newStatus.equals(CEStatus.REPARACION)){
            throw new CustomException("El estado no puede pasar a reparacion si tiene un responsable asignado");
        }

        if (!equipament.getPerson().getName().equals("Sistemas") && newStatus.equals(CEStatus.CAMBIO_DISCO_AMPLIACION_RAM)){
            throw new CustomException("El estado no puede pasar a revisar para cambio de disco duro y/o ampliacion de RAM si tiene un responsable asignado");
        }

        equipament.setStatus(newStatus);
        repository.save(equipament);
        return "Estado cambiado con exito";
    }

    @Transactional
    public BeanComputerEquipament assignToSistemas(Long equipamentId, Long sistemasPersonId) {
        BeanComputerEquipament equipament = repository.findById(equipamentId)
                .orElseThrow(() -> new CustomException("Equipo no encontrado con ID: " + equipamentId));

        BeanPerson sistemasPerson = personRepository.findById(sistemasPersonId)
                .orElseThrow(() -> new CustomException("Persona de Sistemas no encontrada con ID: " + sistemasPersonId));

        equipament.setPerson(sistemasPerson);
        return repository.save(equipament);
    }

    public byte[] generateExcelFile() throws IOException {
        List<BeanComputerEquipament> equipments = repository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Equipos de Cómputo");

        String[] headers = {
                "Núm.", "Núm. Serie", "ID ESSET", "Responsable", "Depto", "Empresa", "Lugar", "Tipo", "Marca", "Modelo",
                "Núm. Serie", "Memoria RAM", "Disco Duro", "Procesador", "Empresa compradora", "Factura",
                "Proveedor", "Folio Factura", "Fecha de Adquisición", "Núm. Activo", "Costo", "Estado", "Observaciones"
        };

        // Crear la fila de cabecera
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Ajustar el tamaño de las columnas de la cabecera
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        int rowNum = 1;
        for (BeanComputerEquipament equipment : equipments) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(rowNum - 1);
            row.createCell(1).setCellValue(getSafeValue(equipment.getSerialNumber()));
            row.createCell(2).setCellValue(getSafeValue(equipment.getIdEsset()));
            row.createCell(3).setCellValue(getSafeValue(equipment.getPerson() != null ? equipment.getPerson().getName() : null));
            row.createCell(4).setCellValue(getSafeValue(equipment.getDepartament()));
            row.createCell(5).setCellValue(getSafeValue(equipment.getEnterprise()));
            row.createCell(6).setCellValue(getSafeValue(equipment.getWorkModality()));
            row.createCell(7).setCellValue(getSafeValue(equipment.getType()));
            row.createCell(8).setCellValue(getSafeValue(equipment.getBrand()));
            row.createCell(9).setCellValue(getSafeValue(equipment.getModel()));
            row.createCell(10).setCellValue(getSafeValue(equipment.getSerialNumber()));
            row.createCell(11).setCellValue(getSafeValue(equipment.getRamMemoryCapacity()));
            row.createCell(12).setCellValue(getSafeValue(equipment.getMemoryCapacity()));
            row.createCell(13).setCellValue(getSafeValue(equipment.getProcessor()));
            row.createCell(14).setCellValue(getSafeValue(equipment.getPurchasingCompany()));
            row.createCell(15).setCellValue(equipment.getHasInvoice() != null ? (equipment.getHasInvoice() ? "Sí" : "No") : "SIN-INF");
            row.createCell(16).setCellValue(getSafeValue(equipment.getSupplier()));
            row.createCell(17).setCellValue(getSafeValue(equipment.getInvoiceFolio()));
            row.createCell(18).setCellValue(getSafeValue(equipment.getPurchaseDate()));
            row.createCell(19).setCellValue(getSafeValue(equipment.getAssetNumber()));
            row.createCell(20).setCellValue(getSafeValue(equipment.getPrice()));
            row.createCell(21).setCellValue(equipment.getStatus() != null ? equipment.getStatus().name() : "SIN-INF");
            row.createCell(22).setCellValue(getSafeValue(equipment.getSystemObservations()));

            for (int i = 0; i < headers.length-1; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }


    private String getSafeValue(Object value) {
        return (value != null) ? value.toString() : "SIN-INF";
    }

    public byte[] generateQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

}
