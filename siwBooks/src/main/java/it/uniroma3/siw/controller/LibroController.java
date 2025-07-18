package it.uniroma3.siw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.LibroService;

@Controller
public class LibroController {

	@Autowired
	private LibroService libroService;

	@GetMapping("/libri")
	public String mostraLibri(@RequestParam(required = false) String titolo, Model model) {
		List<Libro> tuttiLibri = libroService.getLibri();
		List<Libro> libriFiltrati;

		if (titolo != null && !titolo.isBlank()) {
			String titoloLower = titolo.toLowerCase();
			libriFiltrati = tuttiLibri.stream()
					.filter(l -> l.getTitolo().toLowerCase().contains(titoloLower))
					.collect(Collectors.toList());
		} else {
			libriFiltrati = tuttiLibri;
		}

		model.addAttribute("libri", libriFiltrati);

		// Per mantenere il valore nel campo input (es. barra di ricerca)
		Map<String, String> param = new HashMap<>();
		param.put("titolo", titolo != null ? titolo : "");
		model.addAttribute("param", param);

		return "libri.html";
	}

	@GetMapping("/libro/{id}")
	public String getLibro(@PathVariable("id") Long id, Model model) {
		model.addAttribute("libro", this.libroService.getLibroById(id));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    boolean isAdmin = auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
	    model.addAttribute("isAdmin", isAdmin);
		return "libro.html";
	}

	@GetMapping("/admin/cancellaLibro/{id}")
	public String cancellaLibro(@PathVariable Long id) {
	    libroService.deleteById(id);
	    return "redirect:/";
	}

}
