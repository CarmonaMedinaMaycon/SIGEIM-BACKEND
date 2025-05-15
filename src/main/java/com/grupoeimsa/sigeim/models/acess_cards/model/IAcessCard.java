package com.grupoeimsa.sigeim.models.acess_cards.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAcessCard extends JpaRepository<BeanAccessCard, Long>, JpaSpecificationExecutor<BeanAccessCard> {

    @Query("SELECT a FROM BeanAccessCard a " +
            "JOIN a.person p " +
            "WHERE (:search IS NULL OR " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.surname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.lastname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.accessCardNumber) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
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



    @Query("""
    SELECT t FROM BeanAccessCard t
    WHERE t.person.personId NOT IN (
        SELECT rt.accessCard.person.personId
        FROM BeanResponsiveCards rt
        WHERE rt.status <> com.grupoeimsa.sigeim.models.responsives.model.EStatus.CANCELADA
    )
""")
    List<BeanAccessCard> findAvailableForAccessCardResponsive();

    Optional<BeanAccessCard> findByPersonPersonId(Long personId);

}
