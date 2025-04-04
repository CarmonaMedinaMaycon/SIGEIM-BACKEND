package com.grupoeimsa.sigeim.models.person.controller;

import com.grupoeimsa.sigeim.models.person.controller.dto.*;
import com.grupoeimsa.sigeim.models.person.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/sigeim/person")
@CrossOrigin(origins = {"*"})
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('RRHH')")
    public ResponseEntity<Page<ResponsePersonDTO>> findAll(@Valid @RequestBody RequestPersonDTO requestPersonDTO){
        Page<ResponsePersonDTO> person = personService.findAll(requestPersonDTO.getSearch(), requestPersonDTO.getPage(), requestPersonDTO.getSize(), requestPersonDTO.getStatus(), requestPersonDTO.getEnterprise(), requestPersonDTO.getDepartament());
        return new ResponseEntity<>(
                person,
                HttpStatus.OK
        );
    }

    @PostMapping("/table-data")
    public ResponseEntity<Page<ResponseTablePeopleDto>> getPeopleTableData(@RequestBody RequestPersonDTO request) {
        Page<ResponseTablePeopleDto> page = personService.getPeopleForTable(
                request.getSearch(),
                request.getDepartament(),
                request.getEnterprise(),
                request.getStatus(),
                request.getPage(),
                request.getSize()
        );
        return ResponseEntity.ok(page);
    }

    @PostMapping("/one")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('RRHH')")
    public ResponseEntity<ResponsePersonDTO> findOne(@Valid @RequestBody Map<String, String> requestBody){
        String id = requestBody.get("id");
        ResponsePersonDTO person = personService.findById(Long.valueOf(id));
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping("/register")
    //    @PreAuthorize("hasRole('ADMIN') or hasRole('RRHH')")
    public ResponseEntity<String> register(@Valid @RequestBody ResponseRegisterPersonDTO responseRegisterPersonDTO){
        personService.registerPersonal(responseRegisterPersonDTO);
        return new ResponseEntity<>(
                "personal registered",
                        HttpStatus.OK
        );
    }

    @PutMapping("/enable-disable")
    //    @PreAuthorize("hasRole('ADMIN') or hasRole('RRHH')")
    public ResponseEntity<String> enableDisable(@RequestBody Map < String, Object > requestBody){
        Long id = Long.valueOf(requestBody.get("id").toString());
        personService.enableDisable(id);
        return new ResponseEntity<>(
                "Person status modified",
                HttpStatus.OK
        );
    }


    @PutMapping("/update-person")
    //    @PreAuthorize("hasRole('ADMIN') or hasRole('RRHH')")
    public ResponseEntity<String> updatePerson(@Valid @RequestBody ResponseUpdatePersonDTO responseUpdatePersonDTO){
        personService.update(responseUpdatePersonDTO);
        return new ResponseEntity<>(
                "Person updated",
                HttpStatus.OK
        );
    }

    // Endpoint para obtener lista de personas
    @PostMapping("/select")
    public List<ResponseResponsibleSelectDto> getPersonsForSelect() {
        return personService.getAllPersonsForSelect();
    }


    @PostMapping("/select-for-licences")
    public List<ResponseLicencesPersonSelectDto> getPersonsForSelectInLicences() {
        return personService.getAllPersonsForSelectInLicenses();
    }


    @PostMapping("/select-responsive-equipment")
    public List<ResponsePersonSelectDto> getPersonsForResponsiveEquipment() {
        return personService.getAllPersonsForResponsiveEquipmentGeneration();
    }

}
