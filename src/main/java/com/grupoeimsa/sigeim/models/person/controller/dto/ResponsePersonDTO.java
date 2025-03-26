package com.grupoeimsa.sigeim.models.person.controller.dto;

import com.grupoeimsa.sigeim.models.acess_cards.model.BeanAccessCard;
import com.grupoeimsa.sigeim.models.assets.model.BeanAssets;
import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.licenses.model.BeanLicense;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponsePersonDTO {
    private Long personId;
    private String name;
    private String surname;
    private String lastname;
    private String whoRegistered;
    private String emailRegistered;
    private String email;
    private String phoneNumber;
    private String departament;
    private String enterprise;
    private String position;
    private String comments;
    private String commentsHardwareSoftware;
    private String commentsEmail;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private LocalDate entryDate;
    private Boolean status;
    private BeanUser user;
    private List<BeanCellphone> cellphone;
    private List<BeanComputerEquipament> computerEquipaments; // Cambio aquí
    private BeanLicense license;
    private BeanAccessCard accessCard;
    private BeanAssets assets;

    public ResponsePersonDTO(BeanPerson beanPerson) {
        this.personId = beanPerson.getPersonId();
        this.name = beanPerson.getName();
        this.surname = beanPerson.getSurname();
        this.lastname = beanPerson.getLastname();
        this.whoRegistered = beanPerson.getWhoRegistered();
        this.emailRegistered = beanPerson.getEmailRegistered();
        this.email = beanPerson.getEmail();
        this.phoneNumber = beanPerson.getPhoneNumber();
        this.departament = beanPerson.getDepartament();
        this.enterprise = beanPerson.getEnterprise();
        this.position = beanPerson.getPosition();
        this.comments = beanPerson.getComments();
        this.commentsHardwareSoftware = beanPerson.getCommentsHardwareSoftware();
        this.commentsEmail = beanPerson.getCommentsEmail();
        this.dateStart = beanPerson.getDateStart();
        this.dateEnd = beanPerson.getDateEnd();
        this.entryDate = beanPerson.getEntryDate();
        this.status = beanPerson.getStatus();
        this.user = beanPerson.getUser();
        this.cellphone = beanPerson.getCellphone();
        this.computerEquipaments = beanPerson.getComputerEquipaments(); // Cambio aquí
        this.license = beanPerson.getLicense();
        this.accessCard = beanPerson.getAccessCard();
        this.assets = beanPerson.getAssets();
    }
}
