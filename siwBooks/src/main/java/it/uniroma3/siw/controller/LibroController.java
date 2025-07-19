package it.uniroma3.siw.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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

@Controller
public class LibroController {

	@Autowired
	private LibroService libroService;
	
	@Autowired
	private AutoreService autoreService;

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
	public String getLibro(
	        @PathVariable("id") Long id,
	        @RequestParam(name = "immagineIndex", required = false, defaultValue = "0") int immagineIndex,
	        Model model) {

	    Libro libro = this.libroService.getLibroById(id);
	    if (libro == null) {
	        return "redirect:/error";  // o altra gestione errore
	    }

	    List<String> immagini = libro.getImmagini();
	    int numeroImmagini = immagini.size();

	    if (immagineIndex < 0) immagineIndex = 0;
	    if (immagineIndex >= numeroImmagini) immagineIndex = numeroImmagini - 1;

	    String immagineCorrente = immagini.get(immagineIndex);

	    model.addAttribute("libro", libro);
	    model.addAttribute("immagineCorrente", immagineCorrente);
	    model.addAttribute("immagineIndex", immagineIndex);
	    model.addAttribute("numeroImmagini", numeroImmagini);

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    boolean isAdmin = auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
	    model.addAttribute("isAdmin", isAdmin);

	    // Qui non fai redirect ma ritorni la view con i dati:
	    return "libro";
	}



	@GetMapping("/admin/cancellaLibro/{id}")
	public String cancellaLibro(@PathVariable Long id) {
	    libroService.deleteById(id);
	    return "redirect:/";
	}

	@GetMapping("/admin/modificaLibro/{id}")
	public String modificaLibro(@PathVariable("id") Long id, Model model) {
	    Libro libro = libroService.getLibroById(id);
	    if (libro == null) {
	        return "redirect:/libri";
	    }
	    model.addAttribute("libro", libro);
	    model.addAttribute("tuttiAutori", autoreService.getAutori()); // usa lo stesso nome del POST
	    return "modificaLibro.html";
	}


	@PostMapping("/admin/modificaLibro/{id}")
	public String salvaModificaLibro(
	        @PathVariable("id") Long id,
	        @Valid @ModelAttribute("libro") Libro libro,
	        BindingResult bindingResult,
	        @RequestParam(required = false) List<Long> autoriIds,
	        Model model) {

		if (bindingResult.hasErrors()) {
		    model.addAttribute("tuttiAutori", autoreService.getAutori());
		    model.addAttribute("libro", libro);
		    return "modificaLibro.html";
		}


	    Libro libroEsistente = libroService.getLibroById(id);
	    if (libroEsistente == null) {
	        return "redirect:/libri";
	    }

	    // Aggiorna i campi base
	    libroEsistente.setTitolo(libro.getTitolo());
	    libroEsistente.setAnno(libro.getAnno());

	    // Rimuove il libro da tutti gli autori attuali
	    for (Autore autore : libroEsistente.getAutori()) {
	        autore.getLibri().remove(libroEsistente);
	        autoreService.save(autore);
	    }
	    libroEsistente.getAutori().clear();

	    // Aggiunge i nuovi autori selezionati
	    if (autoriIds != null) {
	        for (Long autoreId : autoriIds) {
	            Autore autore = autoreService.getAutoreById(autoreId);
	            if (autore != null) {
	                libroEsistente.getAutori().add(autore);
	                autore.getLibri().add(libroEsistente);
	                autoreService.save(autore);
	            }
	        }
	    }

	    libroService.save(libroEsistente); // salva libro aggiornato
	    return "redirect:/admin/modificaLibro/" + id;
	}



	@PostMapping("/admin/libro/{id}/uploadImages")
	public String uploadBookImages(@PathVariable Long id,
	                               @RequestParam("imageFiles") List<MultipartFile> imageFiles,
	                               Model model) throws IOException {
	    try {
	        Libro libro = libroService.getLibroById(id);
	        if (libro == null) {
	            return "redirect:/error";
	        }

	        String uploadDir = "src/main/resources/static/images/";
	        List<String> immagini = libro.getImmagini();
	        if (immagini == null) {
	            immagini = new ArrayList<>();
	        }

	        for (MultipartFile imageFile : imageFiles) {
	            String contentType = imageFile.getContentType();
	            if (contentType != null && contentType.contains("image/")) {
	                String fileName = imageFile.getOriginalFilename();
	                Path filePath = Path.of(uploadDir, fileName);
	                Files.createDirectories(filePath.getParent());
	                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	                if (!immagini.contains(fileName)) {
	                    immagini.add(fileName);
	                }
	            }
	        }

	        libro.setImmagine(immagini);
	        libroService.save(libro);

	        return "redirect:/admin/modificaLibro/" + id;
	    } catch (Exception e) {
	        e.printStackTrace();  // utile per debug
	        return "redirect:/error";
	    }
	}


}

