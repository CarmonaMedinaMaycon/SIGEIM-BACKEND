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
    private String whoRegistered;
    private String email;
    private String phoneNumber;
    private String departament;
    private String enterprise;
    private String position;
    private String comments;
    private LocalDate dateEnd ;
    private String emailRegistered;
    private String commentsHardwareSoftware;
    private String commentsEmail;

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
        this.whoRegistered = beanPerson.getWhoRegistered();
        this.emailRegistered = beanPerson.getEmailRegistered();
        this.commentsHardwareSoftware = beanPerson.getCommentsHardwareSoftware();
        this.commentsEmail = beanPerson.getCommentsEmail();
    }
}
