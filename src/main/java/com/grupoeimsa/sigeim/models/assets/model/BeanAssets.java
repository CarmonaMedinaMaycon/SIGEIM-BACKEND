package com.grupoeimsa.sigeim.models.assets.model;


import com.grupoeimsa.sigeim.models.history_photos.model.BeanHistoryPhotosAssets;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveCellphone;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "assets")
public class BeanAssets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assets_id", updatable = false, nullable = false)
    private Long assetsId;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false)
    private BeanPerson person;

    @OneToMany(mappedBy = "assets", fetch = FetchType.LAZY)
    private List<BeanHistoryPhotosAssets> historyPhotosAssets;

}
