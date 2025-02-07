package com.grupoeimsa.sigeim.models.person.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IPerson extends JpaRepository<BeanPerson, Long> {

}
