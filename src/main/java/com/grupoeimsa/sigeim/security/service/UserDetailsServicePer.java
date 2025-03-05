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

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()));

        return new User(
                user.getEmail(),
                user.getPassword(),
                true,
                true,
                true,
                user.isStatus(),
                authorityList
        );
    }


    @Transactional(noRollbackFor = CustomException.class)
    public ResponseAuthDto login(RequestAuthDto authRequest) {
        // load user
        UserDetails userDetails = loadUserByUsername(authRequest.username());

        // if user not found
        if (userDetails == null)
            throw new CustomException("Email or password incorrect");

        BeanUser user2 = userRepository.findBeanUserByEmail(authRequest.username()).get();

        // if user account is locked
        if (!user2.isStatus()){
            throw new CustomException("Account locked");
        }

        // validate password
        if (!passwordEncoder.matches(authRequest.password(), userDetails.getPassword())) {
            user2.setAttempts(user2.getAttempts()+1);
            System.out.println(user2.getAttempts());

            if (user2.getAttempts() == 3){
                user2.setStatus(false);
                user2.setLastTry(java.time.LocalTime.now());
            }

            user2.setLastTry(java.time.LocalTime.now());

            userRepository.save(user2);
            System.out.println("Number of attempts after saving user " + userRepository.findBeanUserByEmail(authRequest.username()).get().getAttempts());
            throw new CustomException("User or password incorrect from method that validates password");
        }


        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        ResponseAuthDto response = new ResponseAuthDto();
        response.setUsername(authRequest.username());
        response.setToken(accessToken);
        response.setRole(userDetails.getAuthorities().toArray()[0].toString().split("_")[1]);

        BeanUser user = userRepository.findBeanUserByEmail(authRequest.username()).get();

        return response;
    }

}
