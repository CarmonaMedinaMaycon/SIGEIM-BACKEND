package com.grupoeimsa.sigeim.models.users.controller;

import com.grupoeimsa.sigeim.models.users.controller.dto.RequestReActivateAccountDto;
import com.grupoeimsa.sigeim.models.users.controller.dto.RequestRegisterUserDto;
import com.grupoeimsa.sigeim.models.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sigeim/users")
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
}
