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
public class ResponsePersonDTO {
    private Long personId;
    private String name;
    private String surname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private Boolean status;
    private BeanUser user;
    private BeanCellphone cellphone;
    private BeanComputerEquipament computerEquipament;
    private BeanLicenses licenses;
    private BeanAccessCard accessCard;
    private BeanAssets assets;

    public ResponsePersonDTO(BeanPerson beanPerson) {
        this.personId = beanPerson.getPersonId();
        this.name = beanPerson.getName();
        this.surname = beanPerson.getSurname();
        this.lastname = beanPerson.getLastname();
        this.email = beanPerson.getEmail();
        this.phoneNumber = beanPerson.getPhoneNumber();
        this.status = beanPerson.getStatus();
        this.user = beanPerson.getUser();
        this.cellphone = beanPerson.getCellphone();
        this.computerEquipament = beanPerson.getComputerEquipament();
        this.licenses = beanPerson.getLicenses();
        this.accessCard = beanPerson.getAccessCard();
        this.assets = beanPerson.getAssets();

    }
}
