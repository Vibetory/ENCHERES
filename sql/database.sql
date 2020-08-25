CREATE TABLE CATEGORIE
(
    noCategorie INTEGER IDENTITY (1,1) NOT NULL,
    libelle     VARCHAR(30)            NOT NULL
)

ALTER TABLE CATEGORIE
    ADD constraint categorie_pk PRIMARY KEY (noCategorie)

CREATE TABLE ENCHERE
(
    encherisseur   INTEGER  NOT NULL,
    articleVendu   INTEGER  NOT NULL,
    dateEnchère    DATETIME NOT NULL,
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
    pseudo         VARCHAR(30)            NOT NULL,
    nom            VARCHAR(30)            NOT NULL,
    prenom         VARCHAR(30)            NOT NULL,
    email          VARCHAR(20)            NOT NULL,
    telephone      VARCHAR(15),
    rue            VARCHAR(30)            NOT NULL,
    codePostal     VARCHAR(10)            NOT NULL,
    ville          VARCHAR(30)            NOT NULL,
    motDePasse     VARCHAR(30)            NOT NULL,
    credit         INTEGER                NOT NULL,
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
    dateDebutEncheres DATE                   NOT NULL,
    dateFinEncheres   DATE                   NOT NULL,
    miseAPrix         INTEGER,
    prixVente         INTEGER,
    vendeur           INTEGER                NOT NULL,
    acquereur         INTEGER,
    categorie         INTEGER                NOT NULL
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

