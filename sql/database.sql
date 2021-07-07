CREATE TABLE CATEGORIE
(
    noCategorie INTEGER IDENTITY (1,1) NOT NULL,
    libelle     VARCHAR(30)            NOT NULL UNIQUE
)

ALTER TABLE CATEGORIE
    ADD constraint categorie_pk PRIMARY KEY (noCategorie)

CREATE TABLE ENCHERE
(
    articleVendu   INTEGER  NOT NULL,
    encherisseur   INTEGER  NOT NULL,
    dateEnchere    DATETIME NOT NULL,
    montantEnchere INTEGER  NOT NULL

)

ALTER TABLE ENCHERE
    ADD constraint enchere_pk PRIMARY KEY (encherisseur, articleVendu)

CREATE TABLE RETRAIT
(
    articleARetirer INTEGER     NOT NULL,
    rue             VARCHAR(30) NOT NULL,
    codePostal      VARCHAR(15) NOT NULL,
    ville           VARCHAR(30) NOT NULL
)

ALTER TABLE RETRAIT
    ADD constraint retrait_pk PRIMARY KEY (articleARetirer)

CREATE TABLE UTILISATEUR
(
    noUtilisateur  INTEGER IDENTITY (1,1) NOT NULL,
    pseudo         VARCHAR(30)            NOT NULL UNIQUE,
    nom            VARCHAR(80)            NOT NULL,
    prenom         VARCHAR(80)            NOT NULL,
    email          VARCHAR(50)            NOT NULL UNIQUE,
    telephone      VARCHAR(15),
    rue            VARCHAR(50)            NOT NULL,
    codePostal     VARCHAR(10)            NOT NULL,
    ville          VARCHAR(30)            NOT NULL,
    motDePasse     VARCHAR(80)            NOT NULL,
    credits         INTEGER                NOT NULL,
    administrateur bit                    NOT NULL
)

ALTER TABLE UTILISATEUR
    ADD constraint utilisateur_pk PRIMARY KEY (noUtilisateur)

-- no_utilisateur has been split into two fields: no_vendeur and no_acquereur (in order to keep track of the original owner of a product and the new one).
-- Note: no_acquereur should be NULL until a sale is finished.
CREATE TABLE ARTICLE
(
    noArticle         INTEGER IDENTITY (1,1) NOT NULL,
    nomArticle        VARCHAR(30)            NOT NULL,
    description       VARCHAR(300)           NOT NULL,
    dateDebutEncheres DATETIME               NOT NULL,
    dateFinEncheres   DATETIME               NOT NULL,
    miseAPrix         INTEGER,
    prixVente         INTEGER,
    vendeur           INTEGER                NOT NULL,
    acquereur         INTEGER,
    categorie         INTEGER
)

ALTER TABLE ARTICLE
    ADD constraint article_pk PRIMARY KEY (noArticle)

ALTER TABLE ARTICLE
    ADD CONSTRAINT enchere_vendeur_fk FOREIGN KEY (vendeur) REFERENCES UTILISATEUR (noUtilisateur)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION

-- A new foreign key has been added.
ALTER TABLE ARTICLE
    ADD CONSTRAINT enchere_acquereur_fk FOREIGN KEY (acquereur) REFERENCES UTILISATEUR (noUtilisateur)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION

ALTER TABLE ENCHERE
    ADD CONSTRAINT enchere_article_fk FOREIGN KEY (articleVendu)
        REFERENCES ARTICLE (noArticle)
        ON DELETE NO ACTION
        ON UPDATE no action

ALTER TABLE RETRAIT
    ADD CONSTRAINT retrait_article_fk FOREIGN KEY (articleARetirer)
        REFERENCES ARTICLE (noArticle)
        ON DELETE NO ACTION
        ON UPDATE no action

ALTER TABLE ARTICLE
    ADD CONSTRAINT article_categorie_fk FOREIGN KEY (categorie)
        REFERENCES CATEGORIE (noCategorie)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION

