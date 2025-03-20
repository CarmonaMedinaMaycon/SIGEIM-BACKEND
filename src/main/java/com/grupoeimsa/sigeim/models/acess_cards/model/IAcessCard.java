package com.grupoeimsa.sigeim.models.acess_cards.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IAcessCard extends JpaRepository<BeanAccessCard, Long> {

    @Query("SELECT a FROM BeanAccessCard a WHERE " +
            "(:search IS NULL OR LOWER(a.person.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND a.person.status = true")
    Page<BeanAccessCard> findAllByPersonName(
            @Param("search") String search,
            Pageable pageable
    );



}
