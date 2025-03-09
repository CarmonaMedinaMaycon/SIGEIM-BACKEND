package com.grupoeimsa.sigeim.models.computing_equipaments.controller;

import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestEquipmentDetailsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestEquipmentsPaginationDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestRegisterComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestSearchByFilteringEquipmentsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.RequestUpdateComputingEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.ResponseSeeAllEquipmentsDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.controller.dto.ResponseSeeDetailsEquipmentDto;
import com.grupoeimsa.sigeim.models.computing_equipaments.service.ComputingEquipmentService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/update")
    public ResponseEntity<String> updateComputingEquipment(@RequestBody RequestUpdateComputingEquipmentDto dto) {
        String response = computingEquipmentService.updateComputingEquipment(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/equipments")
    public Page<ResponseSeeAllEquipmentsDto> getEquipments(@RequestBody RequestEquipmentsPaginationDto paginationDto) {
        int page = paginationDto.getPage();
        int size = paginationDto.getSize();
        return computingEquipmentService.getAllEquipments(page, size);
    }

    @PostMapping("/search-equipment")
    public List<ResponseSeeAllEquipmentsDto> buscarEquipos(@RequestBody ResponseSeeAllEquipmentsDto searchCriteria) {
        return computingEquipmentService.searchEquipments(searchCriteria);
    }

    @PostMapping("/filters")
    public Map<String, List<String>> obtenerFiltrosDisponibles() {
        return computingEquipmentService.getAvailableFilters();
    }

    @PostMapping("/search-equipment-by-filtering")
    public Page<ResponseSeeAllEquipmentsDto> buscarEquipos(@RequestBody RequestSearchByFilteringEquipmentsDto filtros) {
        return computingEquipmentService.searchEquipments(filtros);
    }

    @PostMapping("/see-details")
    public ResponseSeeDetailsEquipmentDto verDetalles(@RequestBody RequestEquipmentDetailsDto requestDetails) {
        return computingEquipmentService.getEquipamentDetails(requestDetails.getId());
    }


}
