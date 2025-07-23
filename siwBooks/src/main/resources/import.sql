INSERT INTO libro (anno, id, immagini, titolo) VALUES (1987, 1, ARRAY['norwegianwood.jpg','nor.jpg'], 'Norwegian Wood');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (1984, 2, ARRAY['linsostenibile.jpg'], 'L''insostenibile leggerezza dell''essere');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (1994, 3, ARRAY['viti.jpg'], 'L''uccello che girava le viti del mondo');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (1997, 4, ARRAY['identita.jpg'], 'L''identità');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (2007, 5, ARRAY['ungiorno.jpg'], 'Un giorno questo dolore ti sarà utile');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (1997, 6, ARRAY['pastorale.jpg'], 'Pastorale americana');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (2000, 7, ARRAY['macchia.jpg'], 'La macchia umana');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (2010, 8, ARRAY['nemesi.jpg'], 'Nemesi');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (1942, 9, ARRAY['straniero1.jpg'], 'Lo straniero');
INSERT INTO libro (anno, id, immagini, titolo) VALUES (1947, 10, ARRAY['peste.jpg','peste2.jpg'], 'La peste');

INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (1, 'Haruki', 'Murakami', '1949-01-12', NULL, 'Giappone', 'murakami.jpg');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (2, 'Milan', 'Kundera', '1929-04-01', '2023-07-11', 'Repubblica Ceca', 'kundera.jpg');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (3, 'Peter', 'Cameron', '1959-11-29', NULL, 'Stati Uniti', 'cameron.jpg');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (4, 'Philip', 'Roth', '1933-03-19', '2019-05-22', 'Stati Uniti', 'roth.jpg');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (5, 'Eugenio', 'Montale', '1896-10-12', '1981-09-12', 'Italia', 'montale.jpg');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (6, 'Albert', 'Camus', '1913-11-07', '1960-01-04', 'Francia', 'camus.jpg');

INSERT INTO libro_autori (autori_id,libri_id) VALUES (1, 1);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (1, 3);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (2, 2);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (2, 4);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (3, 5);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (4, 6);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (4, 7);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (4, 8);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (6, 9);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (6, 10);

INSERT INTO utente (id, cognome, email, nome, username, ruolo) VALUES (1, 'ad', 'admin@gmail.com', 'ad', 'ad', 'ADMIN');
INSERT INTO credenziali (id, utente_id, username,password) VALUES (1, 1, 'ad','$2a$10$Gamj/fFAilTQ7AJ8SOhC7eABzvI9Up00XKTdHj7T9WJaUfoOlfyLe');

INSERT INTO recensione (voto, id, libro_id, utente_id, testo, titolo) VALUES (5, 1, 1, 1, 'Un viaggio profondo nella mente e nell’anima, Murakami al meglio.', 'Bellissimo e introspettivo');
INSERT INTO recensione (voto, id, libro_id, utente_id, testo, titolo) VALUES (3, 6, 1, 1, 'A volte geniale, a volte dispersivo', 'Uno strano viaggio');
INSERT INTO recensione (voto, id, libro_id, utente_id, testo, titolo) VALUES (4, 2, 2, 1, 'Il romanzo di Kundera che ti fa riflettere sull’esistenza.', 'Una lettura impegnativa');
INSERT INTO recensione (voto, id, libro_id, utente_id, testo, titolo) VALUES (4, 3, 5, 1, 'Peter Cameron crea atmosfere uniche, mi è piaciuto molto.', 'Storia originale e coinvolgente');
INSERT INTO recensione (voto, id, libro_id, utente_id, testo, titolo) VALUES (5, 4, 4, 1, 'Un libro che rimane nel cuore, da leggere assolutamente.', 'Profondo e toccante');
INSERT INTO recensione (voto, id, libro_id, utente_id, testo, titolo) VALUES (5, 5, 3, 1, 'Uno dei miei libri preferiti, consigliatissimo!', 'Semplicemente fantastico');

