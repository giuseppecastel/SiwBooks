package it.uniroma3.siw.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.LibroService;
import it.uniroma3.siw.service.RecensioneService;
import it.uniroma3.siw.service.UtenteService;
import jakarta.validation.Valid;

@Controller
public class LibroController {

	@Autowired
	private LibroService libroService;
	
	@Autowired
	private AutoreService autoreService;
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private RecensioneService recensioneService;

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
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
		model.addAttribute("isAdmin", isAdmin);

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
	        return "redirect:/error";
	    }

	    List<String> immagini = libro.getImmagini();
	    String immagineCorrente = null;
	    int numeroImmagini = 0;

	    if (immagini != null && !immagini.isEmpty()) {
	        numeroImmagini = immagini.size();

	        if (immagineIndex < 0) immagineIndex = 0;
	        if (immagineIndex >= numeroImmagini) immagineIndex = numeroImmagini - 1;

	        immagineCorrente = immagini.get(immagineIndex);
	    }

	    List<Recensione> recensioni = recensioneService.findByLibro(libro);
	    model.addAttribute("recensioni", recensioni);

	    model.addAttribute("libro", libro);
	    model.addAttribute("immagineCorrente", immagineCorrente);
	    model.addAttribute("immagineIndex", immagineIndex);
	    model.addAttribute("numeroImmagini", numeroImmagini);

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	    boolean isAdmin = auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
	    boolean isDefault = auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("DEFAULT"));

	    model.addAttribute("isAdmin", isAdmin);
	    model.addAttribute("isDefault", isDefault);

	    // Aggiungi ID dell'utente loggato se presente
	    if (auth != null && auth.isAuthenticated()) {
	        Utente utente = utenteService.findByUsername(auth.getName());
	        if (utente != null) {
	            model.addAttribute("currentUserId", utente.getId());
	        }
	    }

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
	        
	        if (immagini.isEmpty()) {
	            immagini.add("default.jpg");
	        }

	        libro.setImmagine(immagini);
	        libroService.save(libro);

	        return "redirect:/admin/modificaLibro/" + id;
	    } catch (Exception e) {
	        e.printStackTrace();  // utile per debug
	        return "redirect:/error";
	    }
	}
	
	@GetMapping("/admin/formNuovoLibro")
	public String formNuovoLibro(Model model) {
	    model.addAttribute("libro", new Libro());  // nuovo libro vuoto senza id
	    model.addAttribute("tuttiAutori", autoreService.getAutori());
	    return "formNuovoLibro.html";
	}

	
	@PostMapping("/admin/nuovoLibro")
	public String salvaNuovoLibro(@Valid @ModelAttribute("libro") Libro libro,
	                              BindingResult bindingResult,
	                              @RequestParam(required = false) List<Long> autoriIds,
	                              Model model) {
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("tuttiAutori", autoreService.getAutori());
	        return "formNuovoLibro.html";
	    }

	    if (autoriIds != null) {
	        Set<Autore> autori = new HashSet<>();
	        for (Long autoreId : autoriIds) {
	            Autore autore = autoreService.getAutoreById(autoreId);
	            if (autore != null) {
	                autori.add(autore);
	            }
	        }
	        libro.setAutori(autori);
	    }
	    
	   libro.setId(libroService.getMaxId()+1);

	    libroService.save(libro);
	    return "redirect:/admin/modificaLibro/" + libro.getId(); // o altra pagina dopo il salvataggio
	}
	
	@GetMapping("/default/formNuovaRecensione")
    public String mostraFormNuovaRecensione(@RequestParam("libroId") Long libroId, Model model) {
        Libro libro = libroService.getLibroById(libroId);
        if (libro == null) {
            return "redirect:/error";
        }

        Recensione recensione = new Recensione();
        recensione.setLibro(libro);

        model.addAttribute("recensione", recensione);
        model.addAttribute("libro", libro);

        return "formNuovaRecensione";  // nome del template senza .html
    }

	@PostMapping("/default/nuovaRecensione")
	public String salvaRecensione(@ModelAttribute("recensione") Recensione recensione, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        model.addAttribute("libro", recensione.getLibro());
	        return "formNuovaRecensione";
	    }

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null && auth.isAuthenticated()) {
	        String username = auth.getName();
	        Utente utente = utenteService.findByUsername(username);
	        recensione.setUtente(utente);
	    }

	    if (recensione.getId() == null) {
	        Long maxId = recensioneService.getMaxId();
	        recensione.setId((maxId != null ? maxId : 0L) + 1);
	    }

	    recensioneService.save(recensione);

	    return "redirect:/libro/" + recensione.getLibro().getId();
	}
	
	@PostMapping("/default/cancellaRecensione/{id}")
	public String cancellaRecensione(@PathVariable("id") Long id, Principal principal, Model model) {
	    Recensione recensione = recensioneService.getRecensioneById(id);

	    if (recensione == null) {
	        return "redirect:/";
	    }

	    // Controllo che l'utente loggato sia l'autore della recensione
	    Utente utenteLoggato = utenteService.findByUsername(principal.getName());
	    if (!recensione.getUtente().getId().equals(utenteLoggato.getId())) {
	        return "redirect:/accessoNegato";
	    }

	    Long libroId = recensione.getLibro().getId();
	    recensioneService.deleteById(recensione.getId());
	    return "redirect:/libro/" + libroId;
	}
	
	@GetMapping("/default/modificaRecensione/{id}")
	public String mostraFormModificaRecensione(@PathVariable("id") Long id, Model model) {
	    Recensione recensione = recensioneService.getRecensioneById(id);
	    if (recensione == null) {
	        return "redirect:/error";
	    }

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !auth.isAuthenticated()) {
	        return "redirect:/login";
	    }

	    Utente utente = utenteService.findByUsername(auth.getName());
	    if (utente == null) {
	        return "redirect:/login";  // Oppure una pagina di errore custom
	    }

	    if (!recensione.getUtente().getId().equals(utente.getId())) {
	        return "redirect:/accessoNegato";  // Protegge da accessi non autorizzati
	    }

	    model.addAttribute("recensione", recensione);
	    model.addAttribute("libro", recensione.getLibro());

	    return "modificaRecensione.html";
	}


	@PostMapping("/default/modificaRecensione/{id}")
	public String salvaModificaRecensione(@PathVariable("id") Long id,
	                                      @ModelAttribute("recensione") @Valid Recensione recensioneModificata,
	                                      BindingResult bindingResult,
	                                      Model model) {
	    Recensione recensioneEsistente = recensioneService.getRecensioneById(id);
	    if (recensioneEsistente == null) {
	        return "redirect:/error";
	    }

	    // Controllo utente loggato come sopra
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !auth.isAuthenticated()) {
	        return "redirect:/login";
	    }
	    Utente utente = utenteService.findByUsername(auth.getName());
	    if (!recensioneEsistente.getUtente().getId().equals(utente.getId())) {
	        return "redirect:/accessoNegato";
	    }

	    if (bindingResult.hasErrors()) {
	        model.addAttribute("libro", recensioneEsistente.getLibro());
	        return "modificaRecensione";
	    }

	    // Aggiorna i campi modificabili
	    recensioneEsistente.setTitolo(recensioneModificata.getTitolo());
	    recensioneEsistente.setTesto(recensioneModificata.getTesto());
	    recensioneEsistente.setVoto(recensioneModificata.getVoto());

	    recensioneService.save(recensioneEsistente);

	    return "redirect:/libro/" + recensioneEsistente.getLibro().getId();
	}
	



}

