package com.grupoeimsa.sigeim.models.users.controller;

import com.grupoeimsa.sigeim.models.users.controller.dto.RequestRegisterAdministratorDto;
import com.grupoeimsa.sigeim.models.users.service.UserService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> registerAccount(@RequestBody RequestRegisterAdministratorDto request) {
        String message = userService.registerAdministrator(request);
        return ResponseEntity.ok(message);
    }
}
