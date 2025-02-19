package com.grupoeimsa.sigeim.models.person.controller.dto;

import com.grupoeimsa.sigeim.models.acess_cards.model.BeanAccessCard;
import com.grupoeimsa.sigeim.models.assets.model.BeanAssets;
import com.grupoeimsa.sigeim.models.cellphones.model.BeanCellphone;
import com.grupoeimsa.sigeim.models.computing_equipaments.model.BeanComputerEquipament;
import com.grupoeimsa.sigeim.models.licenses.model.BeanLicenses;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseRegisterPersonDTO {
    private Long personId;
    private String name;
    private String surname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private Boolean status;
    private Boolean isUser = false;
    private BeanUser user;
    private Boolean isCellphone = false;
    private BeanCellphone cellphone;
    private Boolean isComputerEquipament = false;
    private BeanComputerEquipament computerEquipament;
    private Boolean isLicense = false;
    private BeanLicenses licenses;
    private Boolean isAccessCard = false;
    private BeanAccessCard accessCard;
    private Boolean isAssets = false;
    private BeanAssets assets;

    public ResponseRegisterPersonDTO(BeanPerson beanPerson) {
        this.personId = beanPerson.getPersonId();
        this.name = beanPerson.getName();
        this.surname = beanPerson.getSurname();
        this.lastname = beanPerson.getLastname();
        this.email = beanPerson.getEmail();
        this.phoneNumber = beanPerson.getPhoneNumber();
        this.status = beanPerson.getStatus();
        this.isUser = false;
        this.user = beanPerson.getUser();
        this.isCellphone = false;
        this.cellphone = beanPerson.getCellphone();
        this.isComputerEquipament = false;
        this.computerEquipament = beanPerson.getComputerEquipament();
        this.isLicense = false;
        this.licenses = beanPerson.getLicenses();
        this.isAccessCard = false;
        this.accessCard = beanPerson.getAccessCard();
        this.isAssets = false;
        this.assets = beanPerson.getAssets();

    }
}
