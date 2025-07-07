package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.repository.AutoreRepository;

@Service
public class AutoreService {
		
	    @Autowired
	    private AutoreRepository autoreRepository;
		
	    public List<Autore> getAutori() {
	        return (List<Autore>) autoreRepository.findAll();
	    }
	    
	    public Autore getAutoreById(Long id) {
	        return autoreRepository.findById(id).get();
	    }

	}
