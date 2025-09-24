package co.edu.escuelaing.properties.service;

import co.edu.escuelaing.properties.model.Property;
import co.edu.escuelaing.properties.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository repository;

    public List<Property> findAll() {
        return repository.findAll();
    }

    public Optional<Property> findById(Long id) {
        return repository.findById(id);
    }

    public Property create(Property property) {
        return repository.save(property);
    }

    public Property update(Long id, Property property) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Propiedad no encontrada con id " + id);
        }
        property.setId(id);
        return repository.save(property);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}