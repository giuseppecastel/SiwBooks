package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.UtenteRepository;


@Service
public class UtenteService {

    @Autowired
    protected UtenteRepository utenteRepository;

    @Transactional
    public Utente getUtente(Long id) {
        Optional<Utente> result = this.utenteRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Utente saveUtente(Utente utente) {
        return this.utenteRepository.save(utente);
    }

    @Transactional
    public Utente findByUsername(String username) {
        Optional<Utente> result = this.utenteRepository.findByUsername(username);
        return result.orElse(null);
    }

    @Transactional
    public List<Utente> getAllUtenti() {
        List<Utente> result = new ArrayList<>();
        Iterable<Utente> iterable = this.utenteRepository.findAll();
        for(Utente utente : iterable)
            result.add(utente);
        return result;
    }

    public Long getMaxId(){
        Long maxId = 1L;

        for(Utente utente : this.getAllUtenti()){
            if(utente.getId() > maxId){
                maxId = utente.getId();
            }
        }

        return maxId;
    }
}
