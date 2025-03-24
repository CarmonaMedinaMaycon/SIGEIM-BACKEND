package com.grupoeimsa.sigeim.models.licenses.controller;


import com.grupoeimsa.sigeim.models.licenses.controller.dto.RegisterLicenseDTO;
import com.grupoeimsa.sigeim.models.licenses.controller.dto.RequestLicensesDTO;
import com.grupoeimsa.sigeim.models.licenses.controller.dto.ResponseLicenseDTO;
import com.grupoeimsa.sigeim.models.licenses.service.LicenseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        licenseService.assignLicense(responseLicenseDTO);
        return new ResponseEntity<>(
                "Licenses assigned",
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update-assign")
    public ResponseEntity<String> updateAssign(@Valid @RequestBody RegisterLicenseDTO responseUpdateLicenseDTO){
        licenseService.editAssignLicense(responseUpdateLicenseDTO);
        return new ResponseEntity<>(
                "Licenses updated",
                HttpStatus.OK
        );
    }

}
