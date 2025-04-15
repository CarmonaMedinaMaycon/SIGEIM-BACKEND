package com.grupoeimsa.sigeim.models.responsives.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface IResponsiveLicenses extends JpaRepository<BeanResponsiveLicenses, Long>, JpaSpecificationExecutor<BeanResponsiveLicenses> {
    List<BeanResponsiveLicenses> findByLicense_LicensesId(Long licensesId);

    @Query("""
    SELECT r FROM BeanResponsiveLicenses r
    JOIN r.license l
    JOIN l.person p
    WHERE (:estado IS NULL OR r.status = :estado)
      AND (:search IS NULL OR LOWER(CONCAT(p.name, ' ', p.lastname, ' ', p.surname)) LIKE LOWER(CONCAT('%', :search, '%')))
    ORDER BY 
        CASE WHEN :sort = 'asc' THEN r.creationDate END ASC,
        CASE WHEN :sort = 'desc' THEN r.creationDate END DESC
""")
    Page<BeanResponsiveLicenses> searchResponsivesAccess(
            @Param("search") String search,
            @Param("estado") EStatus estado,
            @Param("sort") String sort,
            Pageable pageable
    );

}
