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
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.LibroService;
import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
@Controller
public class AutoreController {

	@Autowired
	private AutoreService autoreService;

	@Autowired
	private LibroService libroService;

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
			return "redirect:/autori";
		}
		List<Libro> tuttiLibri = libroService.getLibri();
		model.addAttribute("autore", autore);
		model.addAttribute("tuttiLibri", tuttiLibri);
		return "modificaAutore.html";
	}


	@PostMapping("/admin/modificaAutore/{id}")
	public String salvaModifica(
	        @PathVariable("id") Long id,
	        @Valid @ModelAttribute("autore") Autore autore,
	        BindingResult bindingResult,
	        @RequestParam(required = false) List<Long> libriIds,
	        Model model) {

	    if (bindingResult.hasErrors()) {
	        model.addAttribute("tuttiLibri", libroService.getLibri());
	        return "modificaAutore.html";
	    }

	    Autore autoreEsistente = autoreService.getAutoreById(id);
	    if (autoreEsistente == null) {
	        return "redirect:/autori";
	    }

	    // Aggiorna i campi dell'autore
	    autoreEsistente.setNome(autore.getNome());
	    autoreEsistente.setCognome(autore.getCognome());
	    autoreEsistente.setDataNascita(autore.getDataNascita());
	    autoreEsistente.setDataMorte(autore.getDataMorte());
	    autoreEsistente.setNazione(autore.getNazione());

	    // Rimuovi autore dai libri attualmente associati
	    for (Libro libro : autoreEsistente.getLibri()) {
	        libro.getAutori().remove(autoreEsistente);
	        libroService.save(libro);
	    }
	    autoreEsistente.getLibri().clear();

	    // Aggiungi i nuovi libri selezionati
	    if (libriIds != null) {
	        for (Long libroId : libriIds) {
	            Libro libro = libroService.getLibroById(libroId);
	            if (libro != null) {
	                autoreEsistente.getLibri().add(libro);       // lato Autore
	                libro.getAutori().add(autoreEsistente);      // lato Libro
	                libroService.save(libro);                    // salva libro aggiornato
	            }
	        }
	    }

	    autoreService.save(autoreEsistente); // salva autore aggiornato

	    return "redirect:/admin/modificaAutore/" + id;
	}


	@PostMapping("/admin/autore/{id}/uploadPhoto")
	public String uploadAuthorPhoto(@PathVariable Long id, @RequestParam MultipartFile imageFile, Model model) throws IOException {
		try{
			Autore autore = autoreService.getAutoreById(id);
			if (autore == null) {
				return "redirect:/error";
			}

			String uploadDir = "src/main/resources/static/images/";
			String contentType = imageFile.getContentType();
			if(contentType != null) {
				if (contentType.contains("image/")) {
					String fileName = imageFile.getOriginalFilename();
					Path filePath = Path.of(uploadDir, fileName);
					Files.createDirectories(filePath.getParent());
					Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

					autore.setImmagine(fileName);
					autoreService.save(autore);
				}
			}
			return "redirect:/admin/modificaAutore/" + id;
		}
		catch(Exception e){
			return "redirect:/error";
		}
	}
}
