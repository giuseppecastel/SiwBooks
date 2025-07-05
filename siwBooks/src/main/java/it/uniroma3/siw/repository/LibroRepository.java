package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Libro;

public interface LibroRepository extends CrudRepository<Libro, Long> {

	public List<Libro> findByYear(int year);

	public boolean existsByTitleAndYear(String title, int year);

	public void aggiungiAutoreById(Long libroId, Long autoreId);

	public void rimuoviAutoreById(Long bookId, Long authorId);	
}