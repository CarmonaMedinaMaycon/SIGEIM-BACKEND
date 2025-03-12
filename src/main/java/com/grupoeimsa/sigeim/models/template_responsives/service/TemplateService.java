package com.grupoeimsa.sigeim.models.template_responsives.service;

import com.grupoeimsa.sigeim.models.template_responsives.controller.dto.UploadTemplateDto;
import com.grupoeimsa.sigeim.models.template_responsives.model.BeanTemplateResponsive;
import com.grupoeimsa.sigeim.models.template_responsives.model.ITemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TemplateService {
    private final ITemplate repository;

    public TemplateService(ITemplate repository) {
        this.repository = repository;
    }

    public void uploadTemplate(UploadTemplateDto dto) throws IOException {
        BeanTemplateResponsive template = new BeanTemplateResponsive();
        template.setTemplateName(dto.getTemplateName());
        template.setTemplateFile(dto.getFile().getBytes());
        repository.save(template);
    }
}
