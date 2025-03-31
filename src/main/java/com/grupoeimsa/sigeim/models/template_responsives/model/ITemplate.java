package com.grupoeimsa.sigeim.models.template_responsives.model;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITemplate extends JpaRepository<BeanTemplateResponsive, Long> {
    Optional<BeanTemplateResponsive> findByTemplateName(String templateName);
}
