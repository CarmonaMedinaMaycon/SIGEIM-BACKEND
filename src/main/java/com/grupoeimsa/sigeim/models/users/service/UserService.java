package com.grupoeimsa.sigeim.models.users.service;

import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.models.users.controller.dto.GetUsersFilterDto;
import com.grupoeimsa.sigeim.models.users.controller.dto.RequestReActivateAccountDto;
import com.grupoeimsa.sigeim.models.users.controller.dto.RequestRegisterUserDto;
import com.grupoeimsa.sigeim.models.users.controller.dto.UserDto;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import com.grupoeimsa.sigeim.models.users.model.ERole;
import com.grupoeimsa.sigeim.models.users.model.IUser;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<UserDto> getAllUsers(GetUsersFilterDto filters) {
        Pageable pageable = PageRequest.of(filters.getPage(), filters.getSize(), Sort.by("email"));

        Specification<BeanUser> spec = Specification.where(null);

        if (filters.getStatus() != null && !filters.getStatus().equalsIgnoreCase("Todos")) {
            Boolean active;

            if (filters.getStatus().equalsIgnoreCase("Activo")) {
                active = true;
            } else if (filters.getStatus().equalsIgnoreCase("Bloqueado")) {
                active = false;
            } else {
                active = null;
            }

            if (active != null) {
                spec = spec.and((root, query, cb) ->
                        cb.equal(root.get("status"), active));
            }
        }

        if (filters.getSearch() != null && !filters.getSearch().trim().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("email")), "%" + filters.getSearch().toLowerCase() + "%"));
        }

        Page<BeanUser> users = userRepository.findAll(spec, pageable);
        return users.map(UserDto::new); // convertir a DTO
    }

    public void deleteUser(Long userId) {
        Optional<BeanUser> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new CustomException("Usuario no encontrado con ID: " + userId);
        }

        BeanUser user = userOpt.get();

        // ⚠️ Validar si es el último ADMIN activo
        if (user.getRole().equals(ERole.ADMIN) && user.isStatus()) {
            long adminsActivos = userRepository.countByRoleAndStatus(ERole.ADMIN, true);

            if (adminsActivos <= 1) {
                throw new CustomException("No se puede eliminar. Debe existir al menos un usuario ADMIN activo en el sistema.");
            }
        }

        userRepository.deleteById(userId);
    }


    public void changeUserStatus(Long userId, String newStatus) {
        BeanUser user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Usuario no encontrado"));

        boolean activar = newStatus.equalsIgnoreCase("Activo");

        // ⚠️ Validación al intentar BLOQUEAR
        if (!activar) {
            boolean esAdminActivo = user.getRole().equals(ERole.ADMIN) && user.isStatus();

            if (esAdminActivo) {
                long adminsActivos = userRepository.countByRoleAndStatus(ERole.ADMIN, true);

                if (adminsActivos <= 1) {
                    throw new CustomException("Debe haber al menos un usuario ADMIN activo en el sistema");
                }
            }
        }

        user.setStatus(activar);

        if (activar) {
            user.setAttempts(0); // resetear intentos si se desbloquea
        }

        userRepository.save(user);
    }


}
