package com.grupoeimsa.sigeim.models.cellphones.controller;


import com.grupoeimsa.sigeim.models.cellphones.controller.dto.AvailablePersonCellphoneDto;
import com.grupoeimsa.sigeim.models.cellphones.controller.dto.CellphoneEditDto;
import com.grupoeimsa.sigeim.models.cellphones.controller.dto.CellphoneTableDto;
import com.grupoeimsa.sigeim.models.cellphones.controller.dto.RequestCellphoneDTO;
import com.grupoeimsa.sigeim.models.cellphones.controller.dto.ResponseCellphoneDTO;
import com.grupoeimsa.sigeim.models.cellphones.controller.dto.ResponseRegisterCellphone;
import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.cellphones.service.CellphoneService;
import com.grupoeimsa.sigeim.utils.CustomException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/sigeim/cellphone")
@CrossOrigin(origins = {"*"})
public class CellphoneController {

    private final CellphoneService cellphoneService;

    public CellphoneController(CellphoneService cellphoneService) {
        this.cellphoneService = cellphoneService;
    }

    @PostMapping("/")
    public ResponseEntity<Page<ResponseCellphoneDTO>> findAll(@Valid @RequestBody RequestCellphoneDTO requestCellphoneDTO){
        Page<ResponseCellphoneDTO> cellphone = cellphoneService.findAll(requestCellphoneDTO.getSearch(), requestCellphoneDTO.getPage(), requestCellphoneDTO.getSize(), requestCellphoneDTO.getStatus(), requestCellphoneDTO.getEnterprise(), requestCellphoneDTO.getDepartament());
        return new ResponseEntity<>(
                cellphone,
                HttpStatus.OK
                );
    }

    @PostMapping("/one")
    public ResponseEntity<ResponseCellphoneDTO> findOne(@Valid @RequestBody Map<String, String> requestBody){
        String id = requestBody.get("id");
        ResponseCellphoneDTO cellphone = cellphoneService.findById(Long.valueOf(id));
        return new ResponseEntity<>(cellphone, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody ResponseRegisterCellphone registerCellphone){
        cellphoneService.registerCellphone(registerCellphone);
        return new ResponseEntity<>("Cellphone registered", HttpStatus.OK);
    }

    @PutMapping("/enable-disable")
    public ResponseEntity<String> enableDisable(@RequestBody Map<String, Object> requestBody){
        String id = requestBody.get("id").toString();
        cellphoneService.enableDisable(Long.valueOf(id));
        return new ResponseEntity<>("Cellphone modified\n" +
                "modified", HttpStatus.OK);
    }

    @PutMapping("/update-cellphone")
    public ResponseEntity<String> updateCellphone(@RequestBody ResponseRegisterCellphone registerCellphone){
        cellphoneService.update(registerCellphone);
        return new ResponseEntity<>("Cellphone updated", HttpStatus.OK);
    }

    @PostMapping("/available-for-cellphone")
    public ResponseEntity<List<AvailablePersonCellphoneDto>> getAvailablePersonsForCellphone(@RequestBody Map<String, Long> payload) {
        Long currentPersonId = payload.get("currentPersonId"); // puede ser null si no hay persona asignada
        List<AvailablePersonCellphoneDto> availablePersons = cellphoneService.getAvailablePersonsForCellphone(currentPersonId);
        return ResponseEntity.ok(availablePersons);
    }

    @PostMapping("/table")
    public ResponseEntity<Page<CellphoneTableDto>> getCellphonesTable(@RequestBody RequestCellphoneDTO filter) {
        Page<CellphoneTableDto> result = cellphoneService.getAllCellphonesForTable(
                filter.getSearch(),
                filter.getPage(),
                filter.getSize(),
                filter.getStatus(),
                filter.getEnterprise(),
                filter.getDepartament()
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/edit-dto")
    public ResponseEntity<CellphoneEditDto> getCellphoneForEdit(@RequestBody Map<String, String> payload) {
        Long id = Long.valueOf(payload.get("id"));
        CellphoneEditDto dto = cellphoneService.getCellphoneEditDtoById(id);
        return ResponseEntity.ok(dto);
    }
}
