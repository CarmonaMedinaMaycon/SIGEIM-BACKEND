package com.grupoeimsa.sigeim.security.controller;


import com.grupoeimsa.sigeim.security.controller.dto.RequestAuthDto;
import com.grupoeimsa.sigeim.security.controller.dto.ResponseAuthDto;
import com.grupoeimsa.sigeim.security.service.UserDetailsServicePer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/sigeim/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    private final UserDetailsServicePer userDetailsServicePer;

    public AuthController(UserDetailsServicePer userDetailsServicePer) {
        this.userDetailsServicePer = userDetailsServicePer;
    }


    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public String getMapping (){
        return "hola mundo";
    }

    @PostMapping("/")
    public ResponseEntity<ResponseAuthDto> login(@RequestBody @Valid RequestAuthDto authRequest){
        return new ResponseEntity<>(
                this.userDetailsServicePer.login(authRequest),
                HttpStatus.OK
        );
    }

}
