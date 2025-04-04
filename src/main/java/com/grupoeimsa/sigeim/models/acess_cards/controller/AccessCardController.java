package com.grupoeimsa.sigeim.models.acess_cards.controller;


import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.DeleteAccessCardDto;
import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.RequestAccessCardDTO;
import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.ResponseAccessCardDTO;
import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.ResponseAccessCardTableDto;
import com.grupoeimsa.sigeim.models.acess_cards.controller.dto.ResponseRegisterAccessCardDTO;
import com.grupoeimsa.sigeim.models.acess_cards.service.AccessCardService;
import com.grupoeimsa.sigeim.utils.CustomException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/sigeim/access-card")
@CrossOrigin(origins = {"*"})
public class AccessCardController {

    private final AccessCardService accessCardService;

    public AccessCardController(AccessCardService accessCardService) {
        this.accessCardService = accessCardService;
    }

    @PostMapping("/")
    public ResponseEntity<Page<ResponseAccessCardDTO>> findAll(@Valid @RequestBody RequestAccessCardDTO requestAccessCardDTO){
        Page<ResponseAccessCardDTO> accessCard = accessCardService.findAll(requestAccessCardDTO.getSearch(), requestAccessCardDTO.getPage(), requestAccessCardDTO.getSize(), requestAccessCardDTO.getStatus(),requestAccessCardDTO.getEnterprise(), requestAccessCardDTO.getDepartament());
        return new ResponseEntity<>(
                accessCard,
                HttpStatus.OK
        );
    }

    @PostMapping("/one")
    public ResponseEntity<ResponseAccessCardDTO> findOne(@Valid @RequestBody Map<String, String> requestBody){
        Long id = Long.valueOf(requestBody.get("id"));
        ResponseAccessCardDTO accessCardDTO = accessCardService.findById(id);
        return new ResponseEntity<>(accessCardDTO, HttpStatus.OK);
    }

    @PostMapping("/assign")
    public ResponseEntity<String> register(@Valid @RequestBody ResponseRegisterAccessCardDTO registerAccessCardDTO){
        accessCardService.registerAccessCard(registerAccessCardDTO);
        return new ResponseEntity<>(
                "access card assigned",
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update-assign")
    public ResponseEntity<String> updateAssign(@Valid @RequestBody ResponseRegisterAccessCardDTO responseUpdateAccessCardDTO){
        accessCardService.update(responseUpdateAccessCardDTO);
        return new ResponseEntity<>(
                "access card updated",
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody DeleteAccessCardDto dto) {
        if (dto.getId() == null) {
            throw new CustomException("ID cannot be null");
        }

        accessCardService.delete(dto.getId());
        return ResponseEntity.ok("Access card deleted");
    }



    @PostMapping("/summary")
    public ResponseEntity<Page<ResponseAccessCardTableDto>> getAccessCardSummaries(@RequestBody RequestAccessCardDTO request) {
        Page<ResponseAccessCardTableDto> summaries = accessCardService.getAccessCardSummaries(
                request.getSearch(),
                request.getPage(),
                request.getSize(),
                request.getStatus(),
                request.getEnterprise(),
                request.getDepartament()
        );
        return ResponseEntity.ok(summaries);
    }
}
