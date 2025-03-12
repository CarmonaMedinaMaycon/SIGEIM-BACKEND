package com.grupoeimsa.sigeim.models.template_responsives.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "template_responsive")
public class BeanTemplateResponsive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id", updatable = false, nullable = false)
    private Long id;

    @Lob
    @Column(name = "template_file", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] templateFile;

    @Column(name = "template_name", nullable = false)
    private String templateName;

}
