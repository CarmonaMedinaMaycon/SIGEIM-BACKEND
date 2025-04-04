package com.grupoeimsa.sigeim.models.invoices.model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IInvoice extends JpaRepository<BeanInvoice, Long>, JpaSpecificationExecutor<BeanInvoice> {
    Optional<BeanInvoice> findByInvoiceFolio(String invoiceFolio);

    @Query("SELECT i FROM BeanInvoice i WHERE " +
            "(:supplier IS NULL OR i.supplier = :supplier) " +
            "AND (:invoiceFolio IS NULL OR i.invoiceFolio = :invoiceFolio)")
    Page<BeanInvoice> findByFilters(@Param("supplier") String supplier,
                                    PageRequest pageRequest);
}
