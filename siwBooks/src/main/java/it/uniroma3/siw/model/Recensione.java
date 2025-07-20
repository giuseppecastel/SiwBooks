package it.uniroma3.siw.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Recensione {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotBlank
    private String titolo;

    @Min(1)
    @Max(5)
    private int voto;

    @NotBlank
    @NotNull
    private String testo;

    @ManyToOne
    private Utente Utente;

    @ManyToOne
    private Libro libro;
    
    public Recensione(){
    }

    public Recensione(Long id){
        this.setId(id);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public int getVoto() {
		return voto;
	}

	public void setVoto(int voto) {
		this.voto = voto;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public Utente getUtente() {
		return Utente;
	}

	public void setUtente(Utente utente) {
		Utente = utente;
	}

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}
}