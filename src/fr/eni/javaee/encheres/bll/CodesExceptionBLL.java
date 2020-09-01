package fr.eni.javaee.encheres.bll;

import java.util.HashMap;
import java.util.Map;

public class CodesExceptionBLL {
    // Error during the initialization using DAOFactory.
    public static final Map<String, Integer> INITIALIZATION_DAO_ERROR = new HashMap<String, Integer>() {{
        put("Article", 20001);
        put("Categorie", 20002);
        put("Enchere", 20003);
        put("Retrait", 20004);
        put("Utilisateur", 20005);
    }};
    // Business object is null.
    public static final Map<String, Integer> BO_NULL_ERROR = new HashMap<String, Integer>() {{
        put("Article", 20201);
        put("Categorie", 20202);
        put("Enchere", 20203);
        put("Retrait", 20204);
        put("Utilisateur", 20205);
    }};
    // Error while attempting to insert a business object to the database.
    public static final Map<String, Integer> ADD_ERROR = new HashMap<String, Integer> () {{
        put("Article", 20301);
        put("Categorie", 20302);
        put("Enchere", 20303);
        put("Retrait", 20304);
        put("Utilisateur", 20305);
    }};
    // Business object already exist in the database.
    public static final Map<String, Integer> ADD_ALREADY_EXIST_ERROR = new HashMap<String, Integer> () {{
        put("Article", 20311);
        put("Categorie", 20312);
        put("Enchere", 20313);
        put("Retrait", 20314);
        put("Utilisateur", 20315);
    }};
    // Error while attempting to update a business object to the database.
    public static final Map<String, Integer> UPDATE_ERROR = new HashMap<String, Integer> () {{
        put("Article", 20401);
        put("Categorie", 20402);
        put("Enchere", 20403);
        put("Retrait", 20404);
        put("Utilisateur", 20405);
    }};
    // Business object doesn't exist in the database.
    public static final Map<String, Integer> UPDATE_NOT_EXIST_ERROR = new HashMap<String, Integer> () {{
        put("Article", 20411);
        put("Categorie", 20412);
        put("Enchere", 20413);
        put("Retrait", 20414);
        put("Utilisateur", 20415);
    }};
    // Error while attempting to delete a business object to the database.
    public static final Map<String, Integer> DELETE_ERROR = new HashMap<String, Integer> () {{
        put("Article", 20501);
        put("Categorie", 20502);
        put("Enchere", 20503);
        put("Retrait", 20504);
        put("Utilisateur", 20505);
    }};
    // Business object doesn't exist in the database.
    public static final Map<String, Integer> DELETE_NOT_EXIST_ERROR = new HashMap<String, Integer> () {{
        put("Article", 20511);
        put("Categorie", 20512);
        put("Enchere", 20513);
        put("Retrait", 20514);
        put("Utilisateur", 20515);
    }};
    // Error while retrieving a business object by ID.
    public static final Map<String, Integer> GET_BY_ID_ERROR = new HashMap<String, Integer>() {{
        put("Article", 20601);
        put("Categorie", 20602);
        put("Enchere", 20603);
        put("Retrait", 20604);
        put("Utilisateur", 20605);
    }};
    // Error while retrieving a business object by field.
    public static final Map<String, Integer> GET_BY_FIELD_ERROR = new HashMap<String, Integer>() {{
        put("Article", 20611);
        put("Categorie", 20612);
        put("Enchere", 20613);
        put("Retrait", 20614);
        put("Utilisateur", 20615);
    }};
    // Error while retrieving a list of business objects by ID.
    public static final Map<String, Integer> GET_ALL_ERROR = new HashMap<String, Integer>() {{
        put("Article", 20621);
        put("Categorie", 20622);
        put("Enchere", 20623);
        put("Retrait", 20624);
        put("Utilisateur", 20625);
    }};
    // Error specific to the ArticleManager.
    public static final int ARTICLE_SET_ARTICLES_WON = 21421;
    public static final int ARTICLE_GET_ALL_FROM_ERROR = 21601;
    public static final int ARTICLE_GET_ALL_BY_NAME_DESCRIPTION = 21611;
    // Error specific to the EnchereManager.
    public static final int ENCHERE_DELETE_ALL_BY = 23503;
    public static final int ENCHERE_GET_ENCHERES_FROM = 23603;
    public static final int ENCHERE_GET_HIGHEST_BID = 23613;
    // Error specific to the UtilisateurManager.
    public static final int AUTHENTICATION_ERROR = 25605;
    public static final int UTILISATEUR_GET_BY_PSEUDO_ERROR = 25615;
    public static final int UTILISATEUR_GET_BY_EMAIL_ERROR = 25625;

}
