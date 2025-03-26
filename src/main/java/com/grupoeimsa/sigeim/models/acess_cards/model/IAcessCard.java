package com.grupoeimsa.sigeim.models.acess_cards.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IAcessCard extends JpaRepository<BeanAccessCard, Long> {

    @Query("SELECT a FROM BeanAccessCard a " +
            "JOIN a.person p " + // Unir con la tabla person
            "WHERE (:search IS NULL  OR p.name LIKE %:search% OR " + // Búsqueda por nombre en BeanPerson
            "p.surname LIKE %:search% OR " + // Búsqueda por apellido paterno en BeanPerson
            "p.lastname LIKE %:search%) AND " +  // Búsqueda por lastname
            "(:departament IS NULL OR LOWER(p.departament) LIKE LOWER(CONCAT('%', :departament, '%'))) AND " +
            "(:enterprise IS NULL OR LOWER(p.enterprise) LIKE LOWER(CONCAT('%', :enterprise, '%'))) AND " +
            "(:status IS NULL OR p.status = :status)")
    Page<BeanAccessCard> findAllByPersonName(
            @Param("search") String search,
            @Param("departament") String departament,
            @Param("enterprise") String enterprise,
            @Param("status") Boolean status,
            Pageable pageable
    );



}
