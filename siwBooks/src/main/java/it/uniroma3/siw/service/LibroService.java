package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.LibroRepository;

@Service
public class LibroService {
	
    @Autowired
    private LibroRepository libroRepository;
	
    public List<Libro> getLibri() {
        return (List<Libro>) libroRepository.findAll();
    }
    
    public Libro getLibroById(Long id) {
        return libroRepository.findById(id).get();
    }

}
