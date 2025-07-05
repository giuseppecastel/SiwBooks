package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.LibroService;
import jakarta.validation.Valid;

@Controller
public class LibroController {
	@Autowired 
	private LibroService libroService;
	
	@Autowired 
	private AutoreService autoreService;

	@GetMapping(value="/admin/formNuovoLibro")
	public String formNewMovie(Model model) {
		model.addAttribute("libro", new Libro());
		return "admin/formNuovoLibro.html";
	}

	@GetMapping(value="/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", libroService.getLibroById(id));
		return "admin/formUpdateMovie.html";
	}

	@GetMapping(value="/admin/indexLibro")
	public String indexLibro() {
		return "admin/indexLibro.html";
	}
	
	@GetMapping(value="/admin/gestisciLibri")
	public String manageMovies(Model model) {
		model.addAttribute("libri", this.libroService.getAllLibri());
		return "admin/gestisciLibri.html";
	}
	
	@GetMapping(value="/admin/setAutoreLibro/{autoreId}/{libroId}")
	public String setDirectorToMovie(@PathVariable("autoreId") Long autoreId, @PathVariable("libroId") Long libroId, Model model) {
		
		Autore autore = this.autoreService.getAutoreById(autoreId);
		Libro libro = this.libroService.getLibroById(libroId);
		libro.getAutori().add(autore);
		this.autoreService.save(autore);
		
		model.addAttribute("libro", libro);
		return "admin/formAggiornaLibro.html";
	}
	
	
	@GetMapping(value="/admin/addAutore/{id}")
	public String addDirector(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artists", autoreService.getAllAutori());
		model.addAttribute("libro", libroService.getLibroById(id));
		return "admin/aggiungiAutoreLibro.html";
	}
}
