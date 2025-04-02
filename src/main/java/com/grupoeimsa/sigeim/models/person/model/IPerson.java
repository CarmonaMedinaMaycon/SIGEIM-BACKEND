package com.grupoeimsa.sigeim.models.person.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface IPerson extends JpaRepository<BeanPerson, Long> {
        boolean existsByEmail(String email);


        @Query("SELECT p FROM BeanPerson p WHERE " +
                "(:search IS NULL OR " +
                "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                "LOWER(p.surname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                "LOWER(p.lastname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                "LOWER(p.email) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
                "(:departament IS NULL OR LOWER(p.departament) LIKE LOWER(CONCAT('%', :departament, '%'))) AND " +
                "(:enterprise IS NULL OR LOWER(p.enterprise) LIKE LOWER(CONCAT('%', :enterprise, '%'))) AND " +
                "(:status IS NULL OR p.status = :status)")
        Page<BeanPerson> findAllByFilters(
                @Param("search") String search,
                @Param("departament") String departament,
                @Param("enterprise") String enterprise,
                @Param("status") Boolean status,
                Pageable pageable
        );

        @Query("""
    SELECT p FROM BeanPerson p
    WHERE\s
        (:search IS NULL OR CONCAT(p.name, ' ', p.surname, ' ', p.lastname) LIKE %:search%)
        AND (:departament = '' OR p.departament = :departament)
        AND (:enterprise = '' OR p.enterprise = :enterprise)
        AND (:status IS NULL OR p.status = :status)
""")
        Page<BeanPerson> findCustomFiltered(
                @Param("search") String search,
                @Param("departament") String departament,
                @Param("enterprise") String enterprise,
                @Param("status") Boolean status,
                Pageable pageable
        );


}
