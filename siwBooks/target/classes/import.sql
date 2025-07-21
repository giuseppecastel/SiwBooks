-- Inserisci i libri con array di immagini
INSERT INTO libro (anno, id, immagini, titolo) VALUES (1987, 1, ARRAY['norwegianwood.jpg','nor.jpg'], 'Norwegian Wood');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (1984, 2, ARRAY['linsostenibile.jpg'], 'L''insostenibile leggerezza dell''essere');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (1994, 3, ARRAY['viti.jpg'], 'L''uccello che girava le viti del mondo');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (1997, 4, ARRAY['identita.jpg'], 'L''identità');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (2007, 5, ARRAY['ungiorno.jpg'], 'Un giorno questo dolore ti sarà utile');


-- Inserisci gli autori
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (1, 'Haruki', 'Murakami', '1949-01-12', NULL, 'Giappone', 'murakami.jpg');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (2, 'Milan', 'Kundera', '1929-04-01', '2023-07-11', 'Repubblica Ceca', 'kundera.jpg');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (3, 'Peter', 'Cameron', '1959-11-29', NULL, 'Stati Uniti', 'cameron.jpg');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (4, 'Philip', 'Roth', '1933-03-19', '2019-05-22', 'Stati Uniti', 'roth.jpg');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (5, 'Eugenio', 'Montale', '1896-10-12', '1981-09-12', 'Italia', 'montale.jpg');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (6, 'Albert', 'Camus', '1913-11-07', '1960-01-04', 'Francia', 'camus.jpg');

-- Infine, collega libro e autore (attenzione a nomi colonne)
INSERT INTO libro_autori (autori_id,libri_id) VALUES (1, 1);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (1, 3);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (2, 2);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (2, 4);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (3, 5);

-- 1. Inserisci la credenziale PRIMA (senza utente_id per ora)
INSERT INTO utente (id, cognome, email, nome, username, ruolo) VALUES (1, 'ad', 'admin@gmail.com', 'ad', 'ad', 'ADMIN');
INSERT INTO credenziali (id, utente_id, username,password) VALUES (1, 1, 'ad','$2a$10$Gamj/fFAilTQ7AJ8SOhC7eABzvI9Up00XKTdHj7T9WJaUfoOlfyLe');

-- Inserimento recensioni
INSERT INTO recensione (voto, id, libro_id, utente_id, testo, titolo) VALUES (5, 1, 1, 1, 'Un viaggio profondo nella mente e nell’anima, Murakami al meglio.', 'Bellissimo e introspettivo');
INSERT INTO recensione (voto, id, libro_id, utente_id, testo, titolo) VALUES (3, 6, 1, 1, 'A volte geniale, a volte dispersivo', 'Uno strano viaggio');
INSERT INTO recensione (voto, id, libro_id, utente_id, testo, titolo) VALUES (4, 2, 2, 1, 'Il romanzo di Kundera che ti fa riflettere sull’esistenza.', 'Una lettura impegnativa');
INSERT INTO recensione (voto, id, libro_id, utente_id, testo, titolo) VALUES (4, 3, 5, 1, 'Peter Cameron crea atmosfere uniche, mi è piaciuto molto.', 'Storia originale e coinvolgente');
INSERT INTO recensione (voto, id, libro_id, utente_id, testo, titolo) VALUES (5, 4, 4, 1, 'Un libro che rimane nel cuore, da leggere assolutamente.', 'Profondo e toccante');
INSERT INTO recensione (voto, id, libro_id, utente_id, testo, titolo) VALUES (5, 5, 3, 1, 'Uno dei miei libri preferiti, consigliatissimo!', 'Semplicemente fantastico');

