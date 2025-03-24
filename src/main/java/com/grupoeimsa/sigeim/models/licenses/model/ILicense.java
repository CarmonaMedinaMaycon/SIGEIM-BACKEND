package com.grupoeimsa.sigeim.models.licenses.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ILicense extends JpaRepository<BeanLicense, Long> {

    @Query("SELECT l FROM BeanLicense l " +
            "JOIN l.person p " + // Unir con BeanPerson
            "WHERE (:search IS NULL OR " +
            "l.accountOutlook LIKE %:search% OR " +
            "l.typeOutlook LIKE %:search% OR " +
            "l.alias LIKE %:search% OR " +
            "l.mailbox LIKE %:search% OR " +
            "l.commentsOutlook LIKE %:search% OR " +
            "l.phoneNumber LIKE %:search% OR " +
            "l.twoFactorAuthenticationName LIKE %:search% OR " +
            "l.departament LIKE %:search% OR " +
            "l.userCrm LIKE %:search% OR " +
            "l.typeCrm LIKE %:search% OR " +
            "l.commentsCrm LIKE %:search% OR " +
            "l.userBc LIKE %:search% OR " +
            "l.idUserBc LIKE %:search% OR " +
            "l.typeBc LIKE %:search% OR " +
            "l.enterpriseBc LIKE %:search% OR " +
            "l.userPureCloud LIKE %:search% OR " +
            "l.idUserPureCloud LIKE %:search% OR " +
            "l.userRpa LIKE %:search% OR " +
            "l.moduleRpa LIKE %:search% OR " +
            "l.enterpriseRpa LIKE %:search% OR " +
            "l.userInstagram LIKE %:search% OR " +
            "l.userFacebook LIKE %:search% OR " +
            "l.userTiktok LIKE %:search% OR " +
            "l.userLinkedin LIKE %:search% OR " +
            "l.userYoutube LIKE %:search% OR " +
            "l.magentoUser LIKE %:search% OR " +
            "l.userShopify LIKE %:search% OR " +
            "p.name LIKE %:search% OR " + // Búsqueda por nombre en BeanPerson
            "p.surname LIKE %:search% OR " + // Búsqueda por apellido paterno en BeanPerson
            "p.lastname LIKE %:search%) AND " + // Búsqueda por apellido materno en BeanPerson
            "(:departament IS NULL OR LOWER(p.departament) LIKE LOWER(CONCAT('%', :departament, '%'))) AND " +
            "(:enterprise IS NULL OR LOWER(p.enterprise) LIKE LOWER(CONCAT('%', :enterprise, '%'))) AND " +
            "(:status IS NULL OR p.status = :status)") // debe o no debe tener status?
    Page<BeanLicense> findAllBySearch(
            @Param("search") String search,
            @Param("departament") String departament,
            @Param("enterprise") String enterprise,
            @Param("status") Boolean status,
            Pageable pageable
    );

}
