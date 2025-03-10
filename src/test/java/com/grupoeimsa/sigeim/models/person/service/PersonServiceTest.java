package com.grupoeimsa.sigeim.models.person.service;

import com.grupoeimsa.sigeim.models.person.controller.dto.ResponsePersonDTO;
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
}