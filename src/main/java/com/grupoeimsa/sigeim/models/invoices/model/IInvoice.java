package com.grupoeimsa.sigeim.models.invoices.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvoice extends JpaRepository<BeanInvoice, Integer> {
}
