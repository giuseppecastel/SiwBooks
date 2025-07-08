-- Inserisci i libri
INSERT INTO libro (anno, id, immagine, titolo) VALUES (1987, 1,'norwegianwood.jpg','Norwegian Wood');
INSERT INTO libro (anno, id, immagine, titolo) VALUES (1984, 2, 'linsostenibile.jpg','L''insostenibile leggerezza dell''essere' );

-- Inserisci gli autori
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (1, 'Haruki', 'Murakami', '1949-01-12', NULL, 'Giappone', 'murakami.jpg');
INSERT INTO autore (id, nome, cognome, data_nascita, data_morte, nazione, immagine) VALUES (2, 'Milan', 'Kundera', '1929-04-01', '2023-07-11', 'Repubblica Ceca', 'kundera.jpg');

-- Infine, collega libro e autore (attenzione a nomi colonne)
INSERT INTO libro_autori (autori_id,libri_id) VALUES (1, 1);
INSERT INTO libro_autori (autori_id,libri_id) VALUES (2, 2);
