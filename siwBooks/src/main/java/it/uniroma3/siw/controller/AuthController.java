package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
public class AuthController {

	@Autowired
	private CredenzialiService credenzialiService;

	@Autowired
	private UtenteService utenteService;
	
	@GetMapping(value= "/login")
	public String showLoginForm(Model model, Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
			return "redirect:/";
		}
		else{
			return "login.html";
		}
	}

	@GetMapping(value = "/")
	public String homePage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("isAdmin", false);
			return "homePage.html";
		}
		else{
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			model.addAttribute("username", userDetails.getUsername());
			return "homePage.html";
		}
	}

	@GetMapping(value = "/registrazione")
	public String showRegisterForm (Model model) {
		model.addAttribute("utente", new Utente());
		model.addAttribute("credenziali", new Credenziali());
		return "registrazione.html";
	}

	@PostMapping(value = { "/registrazione" })
    public String registerUser(@Valid @ModelAttribute("utente") Utente utente,
		BindingResult userBindingResult,
		@Valid @ModelAttribute("credenziali") Credenziali credenziali,
		BindingResult credentialsBindingResult,
		Model model) {

        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
			utente.setId(utenteService.getMaxId() + 1);
			credenziali.setId(credenzialiService.getMaxId() + 1);
			credenziali.setUtente(utente);
			utenteService.saveUtente(utente);
            credenziali.setUtente(utente);
			credenzialiService.saveCredenziali(credenziali);
            model.addAttribute("utente", utente);
            return "homePage.html";
        }
        return "registrazione.html";
    }
}