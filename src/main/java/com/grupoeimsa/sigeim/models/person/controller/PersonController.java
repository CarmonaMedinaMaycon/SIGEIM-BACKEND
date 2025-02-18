package com.grupoeimsa.sigeim.models.person.controller;

import com.grupoeimsa.sigeim.models.person.controller.dto.RequestPersonDTO;
import com.grupoeimsa.sigeim.models.person.controller.dto.ResponsePersonDTO;
import com.grupoeimsa.sigeim.models.person.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Page<ResponsePersonDTO> person = personService.findAll(requestPersonDTO.getSearch(), requestPersonDTO.getPage(), requestPersonDTO.getSize());
        return new ResponseEntity<>(
                person,
                HttpStatus.OK
        );
    }


}
