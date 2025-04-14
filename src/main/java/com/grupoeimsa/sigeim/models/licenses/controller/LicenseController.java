package com.grupoeimsa.sigeim.models.licenses.controller;


import com.grupoeimsa.sigeim.models.licenses.controller.dto.DeleteLicenseDto;
import com.grupoeimsa.sigeim.models.licenses.controller.dto.EditLicenseDTO;
import com.grupoeimsa.sigeim.models.licenses.controller.dto.RegisterLicenseDTO;
import com.grupoeimsa.sigeim.models.licenses.controller.dto.RequestLicensesDTO;
import com.grupoeimsa.sigeim.models.licenses.controller.dto.ResponseLicenseDTO;
import com.grupoeimsa.sigeim.models.licenses.service.LicenseService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/sigeim/licenses")
@CrossOrigin(origins = {"*"})
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(final LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping("/")
    public ResponseEntity<Page<ResponseLicenseDTO>> findAll(@Valid @RequestBody RequestLicensesDTO requestLicensesDTO){
        Page<ResponseLicenseDTO> licenses = licenseService.findAll(requestLicensesDTO.getSearch(), requestLicensesDTO.getPage(), requestLicensesDTO.getSize(), requestLicensesDTO.getStatus(), requestLicensesDTO.getEnterprise(), requestLicensesDTO.getDepartament());
        return new ResponseEntity<>(
                licenses,
                HttpStatus.OK
        );
    }

    @PostMapping("/one")
    public ResponseEntity<ResponseLicenseDTO> findOne(@Valid @RequestBody Map<String, String> requestBody){
        Long id = Long.valueOf(requestBody.get("id"));
        ResponseLicenseDTO licenseDTO = licenseService.findById(id);
        return new ResponseEntity<>(licenseDTO, HttpStatus.OK);
    }

    @PostMapping("/assign")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterLicenseDTO responseLicenseDTO){
        try {
            licenseService.assignLicense(responseLicenseDTO);
            return new ResponseEntity<>("Licencias asignadas correctamente", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("❌ Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("❌ Error inesperado al asignar licencia", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/update-assign")
    public ResponseEntity<String> updateAssign(@Valid @RequestBody EditLicenseDTO responseUpdateLicenseDTO){
        licenseService.editAssignLicense(responseUpdateLicenseDTO);
        return new ResponseEntity<>(
                "Licenses updated",
                HttpStatus.OK
        );
    }

    @GetMapping("/export-to-excel")
    public ResponseEntity<InputStreamResource> exportToExcel() throws IOException {

        byte[] excelData = licenseService.generateExcelFile();

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(excelData));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteLicense(@RequestBody DeleteLicenseDto dto) {
        licenseService.deleteLicense(dto);
        System.out.println(dto.getLicenseId());
        return ResponseEntity.ok().build();
    }

}
