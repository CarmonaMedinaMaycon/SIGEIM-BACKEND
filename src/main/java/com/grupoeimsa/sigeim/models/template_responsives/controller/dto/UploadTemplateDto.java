package com.grupoeimsa.sigeim.models.template_responsives.controller.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadTemplateDto {
    private String templateName;
    private MultipartFile file;
}
