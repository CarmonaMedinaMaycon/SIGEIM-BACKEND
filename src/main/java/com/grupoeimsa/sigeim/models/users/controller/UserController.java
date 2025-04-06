package com.grupoeimsa.sigeim.models.users.controller;

import com.grupoeimsa.sigeim.models.users.controller.dto.ChangeUserStatusRequest;
import com.grupoeimsa.sigeim.models.users.controller.dto.GetUsersFilterDto;
import com.grupoeimsa.sigeim.models.users.controller.dto.RequestDeleteUserDto;
import com.grupoeimsa.sigeim.models.users.controller.dto.RequestReActivateAccountDto;
import com.grupoeimsa.sigeim.models.users.controller.dto.RequestRegisterUserDto;
import com.grupoeimsa.sigeim.models.users.controller.dto.UserDto;
import com.grupoeimsa.sigeim.models.users.service.UserService;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/sigeim/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerAccount(@RequestBody RequestRegisterUserDto request) {
        String message = userService.registerUser(request);
        return ResponseEntity.ok(message);
    }


    @PostMapping("/reactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> reActivateAccount(@RequestBody RequestReActivateAccountDto request) {
        String message = userService.reActivateAccount(request);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {

        GetUsersFilterDto filters = new GetUsersFilterDto();
        filters.setPage(page);
        filters.setSize(size);
        filters.setStatus(status);
        filters.setSearch(search);

        Page<UserDto> users = userService.getAllUsers(filters);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/status")
    public ResponseEntity<List<String>> getUserStatuses() {
        List<String> statuses = Arrays.asList("Activo", "Bloqueado");
        return ResponseEntity.ok(statuses);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestBody RequestDeleteUserDto request) {
        if (request.getUserId() == null) {
            throw new CustomException("El ID del usuario no puede ser null");
        }

        userService.deleteUser(request.getUserId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-status")
    public ResponseEntity<Void> changeUserStatus(@RequestBody ChangeUserStatusRequest request) {
        userService.changeUserStatus(request.getUserId(), request.getStatus());
        return ResponseEntity.ok().build();
    }
}
