package com.grupoeimsa.sigeim.models.users.service;

import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.models.users.controller.dto.RequestReActivateAccountDto;
import com.grupoeimsa.sigeim.models.users.controller.dto.RequestRegisterUserDto;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import com.grupoeimsa.sigeim.models.users.model.ERole;
import com.grupoeimsa.sigeim.models.users.model.IUser;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

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
    public String reActivateAccount(RequestReActivateAccountDto request){
        Optional<BeanUser> userOptional = userRepository.findBeanUserByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new CustomException("Account not found");
        }

        BeanUser user = userOptional.get();
        user.setStatus(true);
        user.setAttempts(0);
        user.setLastTry(null);
        userRepository.save(user);

        return "Account reactivated successfully";
    }


    @Transactional
    public String registerUser(RequestRegisterUserDto request) {
        if (userRepository.existsBeanUserByEmail(request.getEmail())) {
            throw new CustomException("email already exists");
        }

        BeanUser newUser = new BeanUser();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        switch (request.getRole()) {
            case 1:
                newUser.setRole(ERole.ADMIN);
                break;
            case 2:
                newUser.setRole(ERole.RRHH);
                break;
            case 3:
                newUser.setRole(ERole.GUESS);
                break;
            case 4:
                newUser.setRole(ERole.BETO);
                break;
            default:
                throw new CustomException("Role not supported");
        }
        newUser.setStatus(true);

        userRepository.save(newUser);

        return "Usuario creado exitosamente";
    }

}
