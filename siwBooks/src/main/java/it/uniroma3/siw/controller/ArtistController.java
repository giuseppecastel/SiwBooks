package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.repository.AutoreRepository;

@Controller
public class ArtistController {
	
	@Autowired 
	private AutoreRepository autoreRepository;

	@GetMapping(value="/admin/formNuovoAutore")
	public String formNewArtist(Model model) {
		model.addAttribute("autore", new Autore());
		return "admin/formNuovoAutore.html";
	}
	
	@GetMapping(value="/admin/autori")
	public String indexArtist() {
		return "admin/allAutori.html";
	}
	
	@PostMapping("/admin/autore")
	public String newArtist(@ModelAttribute("autore") Autore autore, Model model) {
		if (!autoreRepository.existsByNameAndSurname(autore.getNome(), autore.getCognome())) {
			this.autoreRepository.save(autore); 
			model.addAttribute("artist", autore);
			return "redirect:artist/"+autore.getId();
		} else {
			model.addAttribute("messaggioErrore", "Questo artista esiste già");
			return "admin/formNuovoAutore.html"; 
		}
	}

	@GetMapping("/autore/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.autoreRepository.findById(id).get());
		return "autore.html";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {
		model.addAttribute("artists", this.autoreRepository.findAll());
		return "autori.html";
	}
}
