package it.uniroma3.siw.model;

import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Libro {
    
        @Id
        //@GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
    
        @NotBlank
        @NotNull
        private String titolo;
        

		@NotNull
        @Max(2025)
        private int anno;
        
        private Set<String> immagini;
        
        @ManyToMany
        private Set<Autore> autori;
    
        public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
            this.id = id;
        }

		public Set<String> getImmagini() {
			return immagini;
		}

		public void setImmagine(Set<String> immagini) {
			this.immagini = immagini;
		}
		
		public String getTitolo() {
			return titolo;
		}

		public void setTitolo(String titolo) {
			this.titolo = titolo;
		}

		public Integer getAnno() {
			return anno;
		}

		public void setAnno(Integer anno) {
			this.anno = anno;
		}

		public Set<Autore> getAutori() {
			return autori;
		}

		public void setAutori(Set<Autore> autori) {
			this.autori = autori;
		}
    }
