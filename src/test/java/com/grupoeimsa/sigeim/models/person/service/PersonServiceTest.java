package com.grupoeimsa.sigeim.models.person.service;

import com.grupoeimsa.sigeim.models.person.controller.dto.ResponsePersonDTO;
import com.grupoeimsa.sigeim.models.person.controller.dto.ResponseRegisterPersonDTO;
import com.grupoeimsa.sigeim.models.person.model.BeanPerson;
import com.grupoeimsa.sigeim.models.person.model.IPerson;
import com.grupoeimsa.sigeim.utils.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private IPerson personRepository;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll_Success() {
        // Configuración del comportamiento de los mocks
        BeanPerson person = new BeanPerson();
        person.setPersonId(1L);
        person.setName("John");
        person.setSurname("Doe");

        Page<BeanPerson> personPage = new PageImpl<>(Collections.singletonList(person));
        when(personRepository.findAllBySearch(anyString(), any(Boolean.class), any(Pageable.class)))
                .thenReturn(personPage);

        // Llamar al método a probar
        Page<ResponsePersonDTO> result = personService.findAll("John", 0, 10);

        // Verificar que el método del repositorio fue llamado correctamente
        verify(personRepository, times(1)).findAllBySearch("John", true, PageRequest.of(0, 10));

        // Asegurarse de que la respuesta es la esperada
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getName());
        assertEquals("Doe", result.getContent().get(0).getSurname());
    }

    @Test
    void testFindAll_NoPersonsFound() {
        // Configuración del mock para simular que no se encontraron personas
        Page<BeanPerson> emptyPage = new PageImpl<>(Collections.emptyList());
        when(personRepository.findAllBySearch(anyString(), any(Boolean.class), any(Pageable.class)))
                .thenReturn(emptyPage);

        // Verificar que se lanza la excepción CustomException cuando no se encuentran personas
        assertThrows(CustomException.class, () -> personService.findAll("John", 0, 10));
    }

    @Test
    void testFindAll_EmptySearch() {
        // Configuración del comportamiento de los mocks para una búsqueda vacía
        BeanPerson person = new BeanPerson();
        person.setPersonId(1L);
        person.setName("John");
        person.setSurname("Doe");

        Page<BeanPerson> personPage = new PageImpl<>(Collections.singletonList(person));
        when(personRepository.findAllBySearch(anyString(), any(Boolean.class), any(Pageable.class)))
                .thenReturn(personPage);

        // Llamar al método a probar con una búsqueda vacía
        Page<ResponsePersonDTO> result = personService.findAll("", 0, 10);

        // Verificar que el método del repositorio fue llamado correctamente
        verify(personRepository, times(1)).findAllBySearch("", true, PageRequest.of(0, 10));

        // Asegurarse de que la respuesta es la esperada
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John", result.getContent().get(0).getName());
        assertEquals("Doe", result.getContent().get(0).getSurname());
    }

    @Test
    void testRegisterPersonal_Success() {
        // Configuración del DTO de entrada
        ResponseRegisterPersonDTO responsePersonDTO = new ResponseRegisterPersonDTO();
        responsePersonDTO.setName("John");
        responsePersonDTO.setSurname("Doe");
        responsePersonDTO.setLastname("Smith");
        responsePersonDTO.setEmail("john.doe@example.com");
        responsePersonDTO.setPhoneNumber("1234567890");
        responsePersonDTO.setDepartament("IT");
        responsePersonDTO.setEnterprise("Company");
        responsePersonDTO.setPosition("Manager");
        responsePersonDTO.setComments("New employee");
        responsePersonDTO.setDateStart(LocalDate.now());
        responsePersonDTO.setDateEnd(LocalDate.now());
        responsePersonDTO.setEntryDate(LocalDate.now());

        // Configuración del comportamiento de los mocks
        when(personRepository.existsByEmail(responsePersonDTO.getEmail())).thenReturn(false);
        when(personRepository.save(any(BeanPerson.class))).thenReturn(new BeanPerson());

        // Llamar al método a probar
        assertDoesNotThrow(() -> personService.registerPersonal(responsePersonDTO));

        // Verificar que los métodos hayan sido llamados correctamente
        verify(personRepository, times(1)).existsByEmail(responsePersonDTO.getEmail());
        verify(personRepository, times(1)).save(any(BeanPerson.class));
    }

    @Test
    void testRegisterPersonal_EmailAlreadyExists() {
        // Configuración del DTO de entrada
        ResponseRegisterPersonDTO responsePersonDTO = new ResponseRegisterPersonDTO();
        responsePersonDTO.setEmail("john.doe@example.com");

        // Configuración del mock para simular que el email ya existe
        when(personRepository.existsByEmail(responsePersonDTO.getEmail())).thenReturn(true);

        // Verificar que se lanza la excepción CustomException cuando el email ya existe
        CustomException exception = assertThrows(CustomException.class, () -> personService.registerPersonal(responsePersonDTO));
        assertEquals("email already exists", exception.getMessage());

        // Verificar que el método save no fue llamado
        verify(personRepository, never()).save(any(BeanPerson.class));
    }

    @Test
    void testRegisterPersonal_SQLException() {
        // Configuración del DTO de entrada
        ResponseRegisterPersonDTO responsePersonDTO = new ResponseRegisterPersonDTO();
        responsePersonDTO.setEmail("john.doe@example.com");

        // Configuración del mock para simular una SQLException envuelta en RuntimeException
        when(personRepository.existsByEmail(responsePersonDTO.getEmail())).thenReturn(false);
        when(personRepository.save(any(BeanPerson.class))).thenThrow(new RuntimeException(new SQLException()));

        // Verificar que se lanza una RuntimeException con causa SQLException
        Exception exception = assertThrows(RuntimeException.class, () -> personService.registerPersonal(responsePersonDTO));
        assertTrue(exception.getCause() instanceof SQLException);
    }
}