package co.edu.escuelaing.properties.service;

import co.edu.escuelaing.properties.model.Property;
import co.edu.escuelaing.properties.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PropertyServiceTest {

    @Mock
    private PropertyRepository repository;

    @InjectMocks
    private PropertyService service;

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
    void testFindAll() {
        when(repository.findAll()).thenReturn(Arrays.asList(property));
        List<Property> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals("Calle 123", result.get(0).getAddress());
    }

    @Test
    void testFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(property));
        Optional<Property> result = service.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testCreate() {
        when(repository.save(property)).thenReturn(property);
        Property created = service.create(property);
        assertNotNull(created);
        assertEquals("Calle 123", created.getAddress());
    }

    @Test
    void testUpdateExistingProperty() {
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.save(property)).thenReturn(property);

        Property updated = service.update(1L, property);
        assertEquals(1L, updated.getId());
    }

    @Test
    void testUpdateNonExistingPropertyThrowsException() {
        when(repository.existsById(2L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.update(2L, property));
    }

    @Test
    void testDelete() {
        doNothing().when(repository).deleteById(1L);
        service.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}