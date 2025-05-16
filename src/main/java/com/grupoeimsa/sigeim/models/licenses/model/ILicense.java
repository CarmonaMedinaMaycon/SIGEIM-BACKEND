package com.grupoeimsa.sigeim.models.licenses.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ILicense extends JpaRepository<BeanLicense, Long> {

    Optional<BeanLicense> findById(Long id);

    @Query("SELECT l FROM BeanLicense l " +
            "JOIN l.person p " + // Unir con BeanPerson
            "WHERE (:search IS NULL OR " +
            "l.accountOutlook LIKE %:search% OR " +
            "l.typeOutlook LIKE %:search% OR " +
            "l.supplierOutlook LIKE %:search% OR " +
            "l.aliasOutlook LIKE %:search% OR " +
            "l.mailboxOutlook LIKE %:search% OR " +
            "l.commentsOutlook LIKE %:search% OR " +
            "l.authPhoneNumber LIKE %:search% OR " +
            "l.authTwoFactorAuthenticationName LIKE %:search% OR " +
            "l.authDepartament LIKE %:search% OR " +
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
            "l.userTwitter LIKE %:search% OR " +
            "l.magentoUser LIKE %:search% OR " +
            "l.userShopify LIKE %:search% OR " +
            "l.userPayPal LIKE %:search% OR " +
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

    @Query("""
    SELECT l FROM BeanLicense l
    WHERE l.status = true
    AND NOT EXISTS (
        SELECT 1 FROM BeanResponsiveLicenses rl
        WHERE rl.license.licensesId = l.licensesId
        AND rl.status <> com.grupoeimsa.sigeim.models.responsives.model.EStatus.CANCELADA
    )
""")
    List<BeanLicense> findAvailableForAccessResponsive();



    List<BeanLicense> findByPersonPersonId(Long personId);


}
