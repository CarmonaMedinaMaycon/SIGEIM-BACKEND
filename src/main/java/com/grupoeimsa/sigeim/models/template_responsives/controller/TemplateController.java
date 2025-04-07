package com.grupoeimsa.sigeim.models.template_responsives.controller;

import com.grupoeimsa.sigeim.models.template_responsives.controller.dto.UploadTemplateDto;
import com.grupoeimsa.sigeim.models.template_responsives.service.TemplateService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sigeim/templates")
@CrossOrigin(origins = {"*"})
public class TemplateController {
    private final TemplateService templateService;
    public TemplateController(final TemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadTemplate(@ModelAttribute UploadTemplateDto dto) {
        try {
            templateService.uploadTemplate(dto);
            return ResponseEntity.ok("Plantilla subida exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al subir la plantilla: " + e.getMessage());
        }
    }

    @GetMapping("/template")
    public ResponseEntity<byte[]> getTemplateByName(@RequestParam String name) {
        return templateService.descargarPlantilla(name);
    }

}
