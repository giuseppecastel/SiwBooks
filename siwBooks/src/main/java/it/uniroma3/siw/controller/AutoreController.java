package it.uniroma3.siw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.service.AutoreService;
import jakarta.validation.Valid;

@Controller
public class AutoreController {

	@Autowired
	private AutoreService autoreService;

	@GetMapping("/autori")
	public String mostraLibri(@RequestParam(required = false) String query, Model model) {
		List<Autore> tuttiAutori = autoreService.getAutori();
		List<Autore> autoriFiltrati;

		if (query != null && !query.isBlank()) {
			String queryLower = query.toLowerCase();

			autoriFiltrati = tuttiAutori.stream()
					.filter(autore -> 
					autore.getNome().toLowerCase().contains(queryLower) ||
					autore.getCognome().toLowerCase().contains(queryLower)
							)
					.collect(Collectors.toList());
		} else {
			autoriFiltrati = tuttiAutori;
		}

		model.addAttribute("autori", autoriFiltrati);

		// Mantieni il valore per la barra di ricerca
		Map<String, String> param = new HashMap<>();
		param.put("query", query != null ? query : "");
		model.addAttribute("param", param);




		return "autori.html";
	}

	@GetMapping("/autore/{id}")
	public String getLibro(@PathVariable("id") Long id, Model model) {
		model.addAttribute("autore", this.autoreService.getAutoreById(id));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
		model.addAttribute("isAdmin", isAdmin);
		return "autore.html";
	}

	@GetMapping("/admin/cancellaAutore/{id}")
	public String cancellaAutore(@PathVariable("id") Long id, Model model) {
		this.autoreService.deleteById(id);
		return "redirect:/";
	}
	
	@GetMapping("/admin/modificaAutore/{id}")
	public String modificaAutore(@PathVariable("id") Long id, Model model) {
	    Autore autore = autoreService.getAutoreById(id);
	    if (autore == null) {
	        // Se autore non esiste, magari redirect alla lista autori
	        return "redirect:/autori";
	    }
	    model.addAttribute("autore", autore);
	    return "modificaAutore.html";  // nome della pagina html senza estensione
	}

	@PostMapping("/admin/modificaAutore/{id}")
	public String salvaModifica(
	    @PathVariable("id") Long id,
	    @Valid @ModelAttribute("autore") Autore autore,
	    BindingResult bindingResult,
	    Model model) {

	    if (bindingResult.hasErrors()) {
	        // Torna alla pagina di modifica con gli errori e dati inseriti
	        return "modificaAutore.html";
	    }

	    Autore autoreEsistente = autoreService.getAutoreById(id);
	    if (autoreEsistente == null) {
	        // Se autore non trovato, torna a lista o pagina errore
	        return "redirect:/autori";
	    }

	    autoreEsistente.setNome(autore.getNome());
	    autoreEsistente.setCognome(autore.getCognome());
	    autoreEsistente.setDataNascita(autore.getDataNascita());
	    autoreEsistente.setDataMorte(autore.getDataMorte());
	    autoreEsistente.setNazione(autore.getNazione());

	    autoreService.save(autoreEsistente);

	    // Dopo il salvataggio, redirect magari alla lista autori o a pagina dettaglio
	    return "redirect:/autori";
	}


}
