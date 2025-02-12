package com.grupoeimsa.sigeim.models.cellphones.model;

import com.grupoeimsa.sigeim.models.history_photos.model.BeanHistoryPhotosAssets;
import com.grupoeimsa.sigeim.models.history_photos.model.BeanHistoryPhotosCellphone;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveCellphone;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveEquipaments;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "cellphone")
public class BeanCellphone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cellphone_id", updatable = false, nullable = false)
    private Long cellphoneId;

    @Column(name = "legal_name", nullable = false)
    private String legalName;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "short_dialing", nullable = false)
    private int shortDialing; //marcacion rapida

    @Column(name = "date_renovation", nullable = false)
    private Date dateRenovation;

    @Column(name = "imei", nullable = false)
    private String imei;

    @Column(name = "comments")
    private String comments;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false)
    private BeanPerson person;

    @OneToMany(mappedBy = "cellphone", fetch = FetchType.LAZY)
    private List<BeanResponsiveCellphone> responsiveCellphones;


    @OneToMany(mappedBy = "cellphone", fetch = FetchType.LAZY)
    private List<BeanHistoryPhotosCellphone> historyPhotosCellphones;


}
