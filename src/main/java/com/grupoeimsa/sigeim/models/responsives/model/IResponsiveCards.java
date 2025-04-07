package com.grupoeimsa.sigeim.models.responsives.model;

import com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseAvailableUsersTarjetasDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IResponsiveCards extends JpaRepository<BeanResponsiveCards, Long>, JpaSpecificationExecutor<BeanResponsiveCards> {
    @Query("""
    SELECT new com.grupoeimsa.sigeim.models.responsives.controller.dto.ResponseAvailableUsersTarjetasDto(
        p.personId,
        CONCAT(p.name, ' ', p.surname, ' ', p.lastname)
    )
    FROM BeanAccessCard a
    JOIN a.person p
    WHERE NOT EXISTS (
        SELECT 1
        FROM BeanResponsiveCards r
        WHERE r.accessCard.person.personId = p.personId
        AND r.status != com.grupoeimsa.sigeim.models.responsives.model.EStatus.CANCELADA
    )
""")
    List<ResponseAvailableUsersTarjetasDto> findAvailableUsersForTarjetas();


    @Query("""
    SELECT r FROM BeanResponsiveCards r
    JOIN r.accessCard a
    JOIN a.person p
    WHERE (:estado IS NULL OR r.status = :estado)
      AND (:search IS NULL OR LOWER(CONCAT(p.name, ' ', p.lastname, ' ', p.surname)) LIKE LOWER(CONCAT('%', :search, '%')))
    ORDER BY 
        CASE WHEN :sort = 'asc' THEN r.creationDate END ASC,
        CASE WHEN :sort = 'desc' THEN r.creationDate END DESC
""")
    Page<BeanResponsiveCards> searchResponsivesCards(
            @Param("search") String search,
            @Param("estado") EStatus estado,
            @Param("sort") String sort,
            Pageable pageable
    );

}
