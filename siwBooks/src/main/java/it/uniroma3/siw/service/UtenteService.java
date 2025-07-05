package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.UtenteRepository;


@Service
public class UtenteService {

    @Autowired
    protected UtenteRepository utenteRepository;

    private Utente currentUtente = null;

    @Transactional
    public Utente getUtente(Long id) {
        Optional<Utente> result = this.utenteRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Utente saveUtente(Utente utente) {
        utente.setRuolo(Utente.USER_ROLE);
        return this.utenteRepository.save(utente);
    }

    @Transactional
    public List<Utente> getAllUtenti() {
        List<Utente> result = new ArrayList<>();
        Iterable<Utente> iterable = this.utenteRepository.findAll();
        for(Utente Utente : iterable)
            result.add(Utente);
        return result;
    }

    @Transactional
    public Utente findByNomeUtente(String nomeUtente) {
        Optional<Utente> result = this.utenteRepository.findByNomeUtente(nomeUtente);
        return result.orElse(null);
    }

    public Utente getCurrentUtente() {
        return currentUtente;
    }

    public void setCurrentUtente(Utente currentUtente) {
        this.currentUtente = currentUtente;
    }

}
