package it.uniroma3.siw.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Utente {

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String nome;
	@NotBlank
	private String cognome;
	@NotBlank
	private String email;
	@NotBlank
	private String username;

	private String ruolo;
	
	@OneToMany(mappedBy = "utente", fetch = FetchType.EAGER)
	private List<Recensione> recensioni;

	public List<Recensione> getRecensioni() {
		return recensioni;
	}
	public void setRecensioni(List<Recensione> recensioni) {
		this.recensioni = recensioni;
	}

	public static final String DEFAULT_ROLE = "DEFAULT";
	public static final String ADMIN_ROLE = "ADMIN";

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "credenziali_id")
	private Credenziali credenziali;

	public Credenziali getCredenziali() {
		return credenziali;
	}
	public void setCredenziali(Credenziali credenziali) {
		this.credenziali = credenziali;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}

	public void setName(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setSurname(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getRuolo() {
		return ruolo;
	}
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}

}