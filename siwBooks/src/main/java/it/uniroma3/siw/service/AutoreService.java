package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.AutoreRepository;
import it.uniroma3.siw.repository.LibroRepository;
import jakarta.transaction.Transactional;

@Service
public class AutoreService {

    @Autowired
    private AutoreRepository autoreRepository;

    @Autowired
    private LibroRepository libroRepository;

    public Autore getAutoreById(Long id) {
        return autoreRepository.findById(id).get();
    }

    public List<Autore> getAllAutori() {
        return (List<Autore>) autoreRepository.findAll();
    }

    @Transactional
    public void eliminaById(Long id) {
        Autore autore = autoreRepository.findById(id).orElse(null);
        if (autore != null) {
            for (Libro libro : autore.getLibri()) {
                libro.getAutori().remove(autore);
                libroRepository.save(libro);
            }
            autore.getLibri().clear();
            autoreRepository.delete(autore);
        }
    }

    public void save(Autore autore) {
        autoreRepository.save(autore);
    }
}