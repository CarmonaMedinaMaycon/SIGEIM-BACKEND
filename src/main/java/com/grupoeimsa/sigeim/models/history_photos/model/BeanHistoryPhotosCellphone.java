package com.grupoeimsa.sigeim.models.history_photos.model;


import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "history_photos_cellphone")
public class BeanHistoryPhotosCellphone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_cellphone_id", updatable = false, nullable = false)
    private Long historyCellphoneId;

    @Column(name = "photo")
    private String photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cellphone_id", nullable = false)
    private BeanCellphone cellphone;
}
