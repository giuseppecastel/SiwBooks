package it.uniroma3.siw.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.service.AutoreService;

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
		return "autore.html";
	}

}
