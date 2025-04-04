package com.grupoeimsa.sigeim.security.service;

import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import com.grupoeimsa.sigeim.models.users.model.IUser;
import com.grupoeimsa.sigeim.security.controller.dto.RequestAuthDto;
import com.grupoeimsa.sigeim.security.controller.dto.RequestChangePasswordDto;
import com.grupoeimsa.sigeim.security.controller.dto.ResponseAuthDto;
import com.grupoeimsa.sigeim.security.jwt.JwtUtils;
import com.grupoeimsa.sigeim.utils.CustomException;
import com.grupoeimsa.sigeim.utils.SessionInformation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Transactional
public class UserDetailsServicePer implements UserDetailsService {
    final IUser userRepository;
    final PasswordEncoder passwordEncoder;
    final JwtUtils jwtUtils;
    private final JavaMailSender mailSender;

    private final Map<String, VerificationCodeInfo> verificationCodes = new HashMap<>();

    public UserDetailsServicePer(IUser userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.mailSender = mailSender;
    }

    public void sendVerificationCode(String userToChangeEmail) {
        String adminEmail = SessionInformation.getEmail();
        System.out.println(SessionInformation.getEmail());
        System.out.println(userToChangeEmail);
        BeanUser userToChange = userRepository.findBeanUserByEmail(String.valueOf(userToChangeEmail))
                .orElseThrow(() -> new CustomException("User not found"));

        // Generar código de verificación
        String verificationCode = generateVerificationCode();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5); // Código válido por 5 minutos

        // Asociar el código con el email del usuario objetivo
        verificationCodes.put(String.valueOf(userToChangeEmail), new VerificationCodeInfo(verificationCode, expirationTime));

        // Enviar el código de verificación al admin
        sendEmail(adminEmail, "Código de verificación para cambio de contraseña",
                "Tu código de verificación para cambiar la contraseña del usuario " + userToChange.getEmail() + " es: " + verificationCode + ". Expira en 5 minutos.");
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException("Failed to send verification email");
        }
    }

    @Transactional
    public void changePasswordWithCode(RequestChangePasswordDto request) {
        // Obtener el email del admin logueado
        String adminEmail = SessionInformation.getEmail();

        // Verificar si el usuario objetivo existe
        BeanUser userToChange = userRepository.findBeanUserByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("User not found"));

        // Verificar si el código es válido para este usuario
        VerificationCodeInfo storedCode = verificationCodes.get(request.getEmail());

        if (storedCode == null || !storedCode.getCode().equals(request.getVerificationCode())) {
            throw new CustomException("Invalid verification code");
        }

        // Validar que el código no haya expirado
        if (storedCode.getExpirationTime().isBefore(LocalDateTime.now())) {
            verificationCodes.remove(request.getEmail());
            throw new CustomException("Verification code expired");
        }

        // Validar que las contraseñas coincidan
        if (!request.getNewPassword().equals(request.getRepeatNewPassword())) {
            throw new CustomException("Passwords do not match");
        }

        // Encriptar la nueva contraseña y actualizarla
        String encryptedPassword = passwordEncoder.encode(request.getNewPassword());
        userToChange.setPassword(encryptedPassword);
        userRepository.save(userToChange);

        // Eliminar el código de verificación una vez usado
        verificationCodes.remove(request.getEmail());
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        BeanUser user = userRepository.findBeanUserByEmail(email)
                .orElseThrow(() -> new CustomException("Usuario o contraseña incorrectos"));

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
            throw new CustomException("Correo o contraseña incorrectos");

        BeanUser user = userRepository.findBeanUserByEmail(authRequest.username()).orElseThrow(
                () -> new CustomException("Usuario no encontrado")
        );

        // if user account is locked
        if (!user.isStatus()) {
            throw new CustomException("Cuenta bloqueada");
        }

        // validate password
        if (!passwordEncoder.matches(authRequest.password(), userDetails.getPassword())) {
            int newAttempts = user.getAttempts() + 1;
            user.setAttempts(newAttempts);
            user.setLastTry(LocalTime.now());

            if (newAttempts >= 3) {
                user.setStatus(false); // lock account
                System.out.println("Cuenta bloqueada por intentos fallidos.");
            }

            userRepository.save(user);
            throw new CustomException("Correo o contraseña incorrectos");
        }

        // 🟢 Login exitoso → reiniciar intentos
        user.setAttempts(0);
        userRepository.save(user);

        // Crear token
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

        return response;
    }


}
