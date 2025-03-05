package com.grupoeimsa.sigeim.models.users.service;

import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.models.users.controller.dto.RequestRegisterAdministratorDto;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import com.grupoeimsa.sigeim.models.users.model.ERole;
import com.grupoeimsa.sigeim.models.users.model.IUser;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class UserService {

    private final IUser userRepository;
    private final IPerson personRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUser userRepository, IPerson personRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String registerAdministrator(RequestRegisterAdministratorDto request) {
        if (userRepository.existsBeanUserByEmail(request.getEmail())) {
            throw new CustomException("email already exists");
        }

        BeanPerson newPerson = new BeanPerson();
        newPerson.setName(request.getName());
        newPerson.setSurname(request.getSurname());
        newPerson.setLastname(request.getLastname());
        newPerson.setEmail(request.getEmail());
        newPerson.setPhoneNumber(request.getPhoneNumber());
        newPerson.setDepartament(request.getDepartament());
        newPerson.setEnterprise(request.getEnterprise());
        newPerson.setPosition(request.getPosition());
        newPerson.setComments("Sin comentarios");
        newPerson.setDateStart(LocalDate.now());
        newPerson.setDateEnd(LocalDate.of(9999, 12, 31));
        newPerson.setEntryDate(LocalDate.now());
        newPerson.setStatus(true);

        BeanPerson savedPerson = personRepository.save(newPerson);

        BeanUser newUser = new BeanUser();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Encriptar contraseña
        newUser.setRole(ERole.ADMIN);
        newUser.setStatus(true);
        newUser.setPerson(savedPerson);

        userRepository.save(newUser);

        return "Usuario creado exitosamente";
    }

}
