package com.grupoeimsa.sigeim.models.computing_equipaments.controller;

import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestRegisterComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.service.ComputingEquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sigeim/computing-equipment")
public class ComputingEquipmentController {
    private final ComputingEquipmentService computingEquipmentService;
    public ComputingEquipmentController(ComputingEquipmentService computingEquipmentService) {
        this.computingEquipmentService = computingEquipmentService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerComputingEquipment(@RequestBody RequestRegisterComputingEquipmentDto dto) {
        String response = computingEquipmentService.createComputingEquipment(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
