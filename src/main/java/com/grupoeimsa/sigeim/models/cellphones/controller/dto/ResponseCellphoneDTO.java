package com.grupoeimsa.sigeim.models.cellphones.controller.dto;


import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.history_photos.model.BeanHistoryPhotosCellphone;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.responsives.model.BeanResponsiveCellphone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseCellphoneDTO {

    private Long cellphoneId;

    private String equipamentName;

    private String legalName; //razon social

    private String company;

    private int shortDialing; //marcacion rapida

    private LocalDate dateRenovation;

    private String imei;

    private String comments;

    private BeanPerson person;

    private Boolean status;

    private Boolean whatsappBussiness;

    private List<BeanResponsiveCellphone> responsiveCellphone;

    private List<BeanHistoryPhotosCellphone> historyPhotosCellphone;

    public ResponseCellphoneDTO(BeanCellphone cellphone) {
        this.cellphoneId = cellphone.getCellphoneId();
        this.equipamentName = cellphone.getEquipamentName();
        this.legalName = cellphone.getLegalName();
        this.company = cellphone.getCompany();
        this.shortDialing = cellphone.getShortDialing();
        this.dateRenovation = cellphone.getDateRenovation();
        this.imei = cellphone.getImei();
        this.comments = cellphone.getComments();
        this.person = cellphone.getPerson();
        this.responsiveCellphone = cellphone.getResponsiveCellphones();
        this.historyPhotosCellphone = cellphone.getHistoryPhotosCellphones();
    }
}
