package com.grupoeimsa.sigeim.models.person.controller.dto;


import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseUpdatePersonDTO {
    private Long personId;
    private String name;
    private String surname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String departament;
    private String enterprise;
    private String position;
    private String comments;
    private LocalDate dateEnd ;

    public ResponseUpdatePersonDTO(BeanPerson beanPerson) {
        this.personId = beanPerson.getPersonId();
        this.name = beanPerson.getName();
        this.surname = beanPerson.getSurname();
        this.lastname = beanPerson.getLastname();
        this.email = beanPerson.getEmail();
        this.phoneNumber = beanPerson.getPhoneNumber();
        this.departament = beanPerson.getDepartament();
        this.enterprise = beanPerson.getEnterprise();
        this.position = beanPerson.getPosition();
        this.comments = beanPerson.getComments();
        this.dateEnd = beanPerson.getDateEnd();
    }
}
