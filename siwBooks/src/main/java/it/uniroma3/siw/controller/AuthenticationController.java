package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.UtenteService;
import jakarta.validation.Valid;


@Controller
public class AuthenticationController {
  
  @Autowired
  private CredenzialiService credenzialiService;

  @Autowired
  private UtenteService utenteService;

  @GetMapping(value = "/register")
  public String showRegisterForm(Model model) {
    model.addAttribute("user", new Utente());
    model.addAttribute("credentials", new Credenziali());
    return "formRegister.html";
  }
  
  @GetMapping(value = "/login")
  public String showLoginForm(Model model, Authentication authentication) {
    if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
      return "redirect:/";
    }
    else{
      return "formLogin.html";
    }
  }

  @GetMapping(value = "/")
  public String homePage(Model model) {
    Utente currentUser = this.utenteService.getCurrentUtente();

    if(currentUser != null){
      model.addAttribute("username", currentUser.getCognome());
      model.addAttribute("isAdmin", currentUser.getRuolo().equals(Utente.ADMIN_ROLE));
    }
    else{
      model.addAttribute("isAdmin", false);
    }

    return "homePage.html";
  }
    
    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model, Authentication authentication) {
        return homePage(model);
    }

  @GetMapping(value = "/error")
    public String error(Model model) {
        return "error.html";
    }

  @PostMapping(value = "/register")
    public String registerUser(@Valid @ModelAttribute Utente utente,
    BindingResult userBindingResult,
    @Valid @ModelAttribute Credenziali credenziali,
    BindingResult credentialsBindingResult,
    Model model) {

        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
      credenziali.setUser(utente);
      utente.setCredenziali(credenziali);
      utenteService.saveUtente(utente);
      credenzialiService.saveCredentials(credenziali);
      this.utenteService.setCurrentUtente(utente);
      model.addAttribute("utente", utente);
      return "redirect:/login";
        }
        return "formRegister.html";
    }
  
}