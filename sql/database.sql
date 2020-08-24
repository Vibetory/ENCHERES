CREATE TABLE CATEGORIE (
    no_categorie   INTEGER IDENTITY(1,1) NOT NULL,
    libelle        VARCHAR(30) NOT NULL
)

ALTER TABLE CATEGORIE ADD constraint categorie_pk PRIMARY KEY (no_categorie)

CREATE TABLE ENCHERE (
    no_utilisateur   INTEGER NOT NULL,
    no_article       INTEGER NOT NULL,
    date_enchere     datetime NOT NULL,
	montant_enchere  INTEGER NOT NULL

)

ALTER TABLE ENCHERE ADD constraint enchere_pk PRIMARY KEY (no_utilisateur, no_article)

CREATE TABLE RETRAIT (
	no_article         INTEGER NOT NULL,
    rue              VARCHAR(30) NOT NULL,
    code_postal      VARCHAR(15) NOT NULL,
    ville            VARCHAR(30) NOT NULL
)

ALTER TABLE RETRAIT ADD constraint retrait_pk PRIMARY KEY  (no_article)

CREATE TABLE UTILISATEUR (
    no_utilisateur   INTEGER IDENTITY(1,1) NOT NULL,
    pseudo           VARCHAR(30) NOT NULL,
    nom              VARCHAR(30) NOT NULL,
    prenom           VARCHAR(30) NOT NULL,
    email            VARCHAR(20) NOT NULL,
    telephone        VARCHAR(15),
    rue              VARCHAR(30) NOT NULL,
    code_postal      VARCHAR(10) NOT NULL,
    ville            VARCHAR(30) NOT NULL,
    mot_de_passe     VARCHAR(30) NOT NULL,
    credit           INTEGER NOT NULL,
    administrateur   bit NOT NULL
)

ALTER TABLE UTILISATEUR ADD constraint utilisateur_pk PRIMARY KEY (no_utilisateur)

-- no_utilisateur has been split into two fields: no_vendeur and no_acquereur (in order to keep track of the original owner of a product and the new one).
-- Note: no_acquereur should be NULL until a sale is finished.
CREATE TABLE ARTICLE (
    no_article                  INTEGER IDENTITY(1,1) NOT NULL,
    nom_article                 VARCHAR(30) NOT NULL,
    description                 VARCHAR(300) NOT NULL,
	date_debut_encheres         DATE NOT NULL,
    date_fin_encheres           DATE NOT NULL,
    prix_initial				INTEGER,
    prix_vente                  INTEGER,
    no_vendeur					INTEGER NOT NULL,
	no_acquereur				INTEGER,
    no_categorie                INTEGER NOT NULL
)

ALTER TABLE ARTICLE ADD constraint article_pk PRIMARY KEY (no_article)

ALTER TABLE ARTICLE
    ADD CONSTRAINT enchere_vendeur_fk FOREIGN KEY ( no_vendeur ) REFERENCES UTILISATEUR ( no_utilisateur )
ON DELETE NO ACTION
    ON UPDATE NO ACTION

-- A new foreign key has been added.
ALTER TABLE ARTICLE
    ADD CONSTRAINT enchere_acquereur_fk FOREIGN KEY ( no_acquereur ) REFERENCES UTILISATEUR ( no_utilisateur )
ON DELETE NO ACTION
    ON UPDATE NO ACTION

ALTER TABLE ENCHERE
    ADD CONSTRAINT enchere_article_fk FOREIGN KEY ( no_article )
        REFERENCES ARTICLE ( no_article )
ON DELETE NO ACTION
    ON UPDATE no action

ALTER TABLE RETRAIT
    ADD CONSTRAINT retrait_article_fk FOREIGN KEY ( no_article )
        REFERENCES ARTICLE( no_article )
ON DELETE NO ACTION
    ON UPDATE no action

ALTER TABLE ARTICLE
    ADD CONSTRAINT article_categorie_fk FOREIGN KEY ( no_categorie )
        REFERENCES CATEGORIE ( no_categorie )
ON DELETE NO ACTION
    ON UPDATE NO ACTION

