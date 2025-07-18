package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.AutoreRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AutoreService {
		
	    @Autowired
	    private AutoreRepository autoreRepository;
		
	    public List<Autore> getAutori() {
	        return (List<Autore>) autoreRepository.findAll();
	    }
	    
	    @Transactional
	    public void deleteById(Long id) {
	        Autore autore = autoreRepository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("Autore non trovato"));

	        // Rimuovi autore da ogni libro associato
	        for (Libro libro : autore.getLibri()) {
	            libro.getAutori().remove(autore);
	        }
	        // svuota la lista di libri dell'autore per sicurezza
	        autore.getLibri().clear();

	        // cancella l'autore
	        autoreRepository.delete(autore);
	    }
	    
	    public void save(Autore autore) {
	    	autoreRepository.save(autore);
	    }

	    
	    public Autore getAutoreById(Long id) {
	        return autoreRepository.findById(id).get();
	    }

	}
