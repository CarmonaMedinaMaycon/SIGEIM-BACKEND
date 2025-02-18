package com.grupoeimsa.sigeim.models.person.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface IPerson extends JpaRepository<BeanPerson, Long> {
        boolean findByName(String name);
        boolean findByEmail(String email);
        boolean existsByEmail(String email);
        boolean existsByName(String name);


        @Query("SELECT p FROM BeanPerson p WHERE " +
                "(:search IS NULL OR " +
                "p.name LIKE %:search% OR " +
                "p.surname LIKE %:search% OR " +
                "p.lastname LIKE %:search% OR " +
                "p.email LIKE %:search%) AND " +
                "(:status IS NULL OR p.status = :status)")
        Page<BeanPerson> findAllBySearch(
                @Param("search") String search,
                @Param("status") Boolean status,
                Pageable pageable
        );

}
