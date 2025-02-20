package com.grupoeimsa.sigeim.models.person.service;

import com.grupoeimsa.sigeim.models.person.controller.dto.ResponsePersonDTO;
import com.grupoeimsa.sigeim.models.person.controller.dto.ResponseRegisterPersonDTO;
import com.grupoeimsa.sigeim.models.person.controller.dto.ResponseUpdatePersonDTO;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@Transactional
public class PersonService {

    public final IPerson personRepository;

    public PersonService(IPerson personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public Page<ResponsePersonDTO> findAll(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BeanPerson> person = personRepository.findAllBySearch(
                search,
                true,
                pageable
        );
        if (person.isEmpty()) {
            throw new CustomException("No persons were found");
        }
        return person.map(ResponsePersonDTO::new);
    }

    @Transactional(readOnly = true)
    public ResponsePersonDTO findById(Long id) {
        BeanPerson person = personRepository.findById(id).orElseThrow(() -> new CustomException("The user was not found"));
        return new ResponsePersonDTO(person);

    }

    @Transactional(rollbackFor = {SQLException.class})
    public void registerPersonal(ResponseRegisterPersonDTO responsePersonDTO) {
        BeanPerson person = new BeanPerson();
        person.setName(responsePersonDTO.getName());
        person.setSurname(responsePersonDTO.getSurname());
        person.setLastname(responsePersonDTO.getLastname());
        person.setEmail(responsePersonDTO.getEmail());
        if (personRepository.existsByEmail(person.getEmail())) {
            throw new CustomException("email already exists");
        }
        person.setPhoneNumber(responsePersonDTO.getPhoneNumber());
        person.setStatus(true);
        personRepository.save(person);
    }

    public void enableDisable(Long id){
        BeanPerson person = personRepository.findById(id)
                .orElseThrow(() -> new CustomException("Person not found"));
        person.setStatus(!person.getStatus());
        personRepository.save(person);
    }

    public void update(ResponseUpdatePersonDTO updatePersonDTO) {
        personRepository.findById(updatePersonDTO.getPersonId())
                .orElseThrow(() -> new CustomException("Person not found"));
        BeanPerson person = new BeanPerson();
        person.setName(updatePersonDTO.getName());
        person.setSurname(updatePersonDTO.getSurname());
        person.setLastname(updatePersonDTO.getLastname());
        person.setEmail(updatePersonDTO.getEmail());
        person.setPhoneNumber(updatePersonDTO.getPhoneNumber());

        personRepository.save(person);
    }

}
