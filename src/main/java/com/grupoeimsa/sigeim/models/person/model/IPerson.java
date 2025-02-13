package com.grupoeimsa.sigeim.models.person.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IPerson extends JpaRepository<BeanPerson, Long> {
        boolean findByName(String name);
        boolean findByEmail(String email);
        boolean existsByEmail(String email);
        boolean existsByName(String name);

        Page<BeanPerson> findAllByNameAndEmailAndStatus(String name, String email, boolean status, Pageable pageable);

}
