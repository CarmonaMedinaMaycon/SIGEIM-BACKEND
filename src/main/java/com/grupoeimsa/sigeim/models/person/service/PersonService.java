package com.grupoeimsa.sigeim.models.person.service;

import com.grupoeimsa.sigeim.models.person.controller.dto.ResponsePersonDTO;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonService {

    public final IPerson personRepository;

    public PersonService(IPerson personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public boolean existsEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsName(String name) {
        return personRepository.existsByName(name);
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

}
