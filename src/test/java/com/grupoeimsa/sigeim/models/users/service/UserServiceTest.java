package com.grupoeimsa.sigeim.models.users.service;

import com.grupoeimsa.sigeim.models.users.controller.dto.RequestRegisterUserDto;
import com.grupoeimsa.sigeim.models.users.model.BeanUser;
import com.grupoeimsa.sigeim.models.users.model.IUser;
import com.grupoeimsa.sigeim.utils.CustomException;
import com.grupoeimsa.sigeim.utils.TestNamePrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(TestNamePrinter.class)
class UserServiceTest {

    @Mock
    private IUser userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RequestRegisterUserDto request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new RequestRegisterUserDto();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setRole(1);  // Asumiendo que 1 es ADMIN
    }

    @Test
    void testRegisterUser_Success() {
        // Configuración del comportamiento de los mocks
        when(userRepository.existsBeanUserByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(new BeanUser());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Llamar al método a probar
        String response = userService.registerUser(request);

        // Verificar que los métodos hayan sido llamados correctamente
        verify(userRepository, times(1)).existsBeanUserByEmail(request.getEmail());
        verify(userRepository, times(1)).save(any(BeanUser.class));

        // Asegurarse de que la respuesta es la esperada
        assertEquals("Usuario creado exitosamente", response);
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Configuración del mock para simular que el email ya existe
        when(userRepository.existsBeanUserByEmail(request.getEmail())).thenReturn(true);

        // Verificar que se lanza la excepción CustomException cuando el email ya existe
        assertThrows(CustomException.class, () -> userService.registerUser(request));
    }

    @Test
    void testRegisterUser_InvalidRole() {
        // Configurar un rol no válido en el request
        request.setRole(99);

        // Verificar que se lanza una excepción cuando el rol no es válido
        assertThrows(CustomException.class, () -> userService.registerUser(request));
    }
}
