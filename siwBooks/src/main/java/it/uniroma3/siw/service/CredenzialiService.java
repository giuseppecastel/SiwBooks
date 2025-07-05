package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.repository.CredenzialiRepository;

@Service
public class CredenzialiService {
  
    @Autowired
    protected PasswordEncoder passwordEncoder;

  @Autowired
  protected CredenzialiRepository credenzialiRepository;
  
  @Transactional
  public Credenziali getCredentials(Long id) {
    Optional<Credenziali> result = this.credenzialiRepository.findById(id);
    return result.orElse(null);
  }

  @Transactional
  public Credenziali getCredentials(String username) {
    Optional<Credenziali> result = this.credenzialiRepository.findByUsername(username);
    return result.orElse(null);
  }
    
    @Transactional
    public Credenziali saveCredentials(Credenziali credenziali) {
        credenziali.setPassword(this.passwordEncoder.encode(credenziali.getPassword()));
        return this.credenzialiRepository.save(credenziali);
    }

    @Transactional
    public Credenziali save(Credenziali credenziali){
        return this.credenzialiRepository.save(credenziali);
    }
}