package com.grupoeimsa.sigeim.models.template_responsives.service;

import com.grupoeimsa.sigeim.models.template_responsives.controller.dto.UploadTemplateDto;
import com.grupoeimsa.sigeim.models.template_responsives.model.BeanTemplateResponsive;
import com.grupoeimsa.sigeim.models.template_responsives.model.ITemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class TemplateService {
    private final ITemplate repository;

    public TemplateService(ITemplate repository) {
        this.repository = repository;
    }

    public void uploadTemplate(UploadTemplateDto dto) throws IOException {
        String nombre = dto.getTemplateName();

        // Si existe una plantilla con el mismo nombre, elimínala
        repository.findByTemplateName(nombre).ifPresent(repository::delete);

        // Guarda la nueva plantilla
        BeanTemplateResponsive nueva = new BeanTemplateResponsive();
        nueva.setTemplateName(nombre);
        nueva.setTemplateFile(dto.getFile().getBytes());

        repository.save(nueva);
    }

    public ResponseEntity<byte[]> descargarPlantilla(String nombre) {
        Optional<BeanTemplateResponsive> optional = repository.findByTemplateName(nombre);

        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        byte[] docxBytes = optional.get().getTemplateFile();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", nombre + ".docx");

        return new ResponseEntity<>(docxBytes, headers, HttpStatus.OK);
    }
}
