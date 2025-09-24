package co.edu.escuelaing.properties.controller;

import co.edu.escuelaing.properties.model.Property;
import co.edu.escuelaing.properties.service.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropertyControllerTest {

    @Mock
    private PropertyService service;

    @InjectMocks
    private PropertyController controller;

    private Property property;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        property = new Property();
        property.setId(1L);
        property.setAddress("Calle 123");
        property.setPrice(200000.0);
        property.setSize(80.0);
        property.setDescription("Bonita casa");
    }

    @Test
    void testAll() {
        when(service.findAll()).thenReturn(Arrays.asList(property));
        List<Property> result = controller.all();
        assertEquals(1, result.size());
    }

    @Test
    void testGetOneFound() {
        when(service.findById(1L)).thenReturn(Optional.of(property));
        ResponseEntity<Property> response = controller.getOne(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Calle 123", response.getBody().getAddress());
    }

    @Test
    void testGetOneNotFound() {
        when(service.findById(2L)).thenReturn(Optional.empty());
        ResponseEntity<Property> response = controller.getOne(2L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreate() {
        when(service.create(property)).thenReturn(property);
        ResponseEntity<Property> response = controller.create(property);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Calle 123", response.getBody().getAddress());
    }

    @Test
    void testUpdateSuccess() {
        when(service.update(1L, property)).thenReturn(property);
        ResponseEntity<Property> response = controller.update(1L, property);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateNotFound() {
        when(service.update(2L, property)).thenThrow(new RuntimeException("No existe"));
        ResponseEntity<Property> response = controller.update(2L, property);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDelete() {
        doNothing().when(service).delete(1L);
        ResponseEntity<Void> response = controller.delete(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}