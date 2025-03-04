package com.grupoeimsa.sigeim.security.service;

import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import com.grupoeimsa.sigeim.models.users.model.IUser;
import com.grupoeimsa.sigeim.security.controller.dto.RequestAuthDto;
import com.grupoeimsa.sigeim.security.controller.dto.ResponseAuthDto;
import com.grupoeimsa.sigeim.security.jwt.JwtUtils;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserDetailsServicePer implements UserDetailsService {
    final IUser userRepository;
    final PasswordEncoder passwordEncoder;
    final JwtUtils jwtUtils;

    public UserDetailsServicePer(IUser userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        BeanUser user = userRepository.findBeanUserByEmail(email)
                .orElseThrow(() -> new CustomException("User or password incorrect"));

//        // Verifica si el usuario está verificado
//        if (!user.isVerified()) {
//            throw new CustomException("Account not verified");
//        }

        // Si está verificado, construye el objeto User de Spring Security
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));

        // En este punto, la contraseña no será null, ya que el usuario está verificado
        return new User(
                user.getEmail(),
                user.getPassword(),  // Aquí debes asegurarte de que `user.getPassword()` no sea null
                true,
                true,
                true,
                user.isStatus(),
                authorityList
        );
    }


    @Transactional
    public ResponseAuthDto login(RequestAuthDto authRequest) {
        // load user
        UserDetails userDetails = loadUserByUsername(authRequest.username());

        // if user not found
        if (userDetails == null)
            throw new CustomException("Email or password incorrect");

        BeanUser user2 = userRepository.findBeanUserByEmail(authRequest.username()).get();

        // validate password
        if (!passwordEncoder.matches(authRequest.password(), userDetails.getPassword())) {
            throw new CustomException("User or password incorrect");
        }

        if(!userDetails.isEnabled() && !userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).get(0).equals("ROLE_ADMIN")){
            throw new CustomException("account not verified");
        }

        // create Authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // create token
        String accessToken = jwtUtils.createToken(authentication);

        ResponseAuthDto response = new ResponseAuthDto();
        response.setUsername(authRequest.username());
        response.setToken(accessToken);
        response.setRole(userDetails.getAuthorities().toArray()[0].toString().split("_")[1]);

        BeanUser user = userRepository.findBeanUserByEmail(authRequest.username()).get();

        // create and return Auth reponse
        return response;
    }

}
