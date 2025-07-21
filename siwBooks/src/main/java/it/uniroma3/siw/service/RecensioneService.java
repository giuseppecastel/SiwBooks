package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.repository.RecensioneRepository;

@Service
public class RecensioneService {
    @Autowired
    private RecensioneRepository recensioneRepository;

    public void save(Recensione recensione) {
        recensioneRepository.save(recensione);
    }

    public Recensione getRecensioneById(Long id) {
        return recensioneRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        recensioneRepository.deleteById(id);
    }

    public Long getMaxId(){
        Long maxId = 1L;

        for(Recensione recensione : recensioneRepository.findAll()){
            if(recensione.getId() > maxId){
                maxId = recensione.getId();
            }
        }

        return maxId;
    }
    
    public List<Recensione> findByLibro(Libro libro) {
        return recensioneRepository.findByLibro(libro);
    }

}