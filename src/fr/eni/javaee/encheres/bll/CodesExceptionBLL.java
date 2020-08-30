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
        put("Article", 21301);
        put("Categorie", 21302);
        put("Enchere", 21303);
        put("Retrait", 21304);
        put("Utilisateur", 21305);
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
        put("Article", 21401);
        put("Categorie", 21402);
        put("Enchere", 21403);
        put("Retrait", 21404);
        put("Utilisateur", 21405);
    }};
    // Business object doesn't exist in the database.
    public static final Map<String, Integer> DELETE_NOT_EXIST_ERROR = new HashMap<String, Integer> () {{
        put("Article", 21501);
        put("Categorie", 21502);
        put("Enchere", 21503);
        put("Retrait", 21504);
        put("Utilisateur", 21505);
    }};
    // Error while retrieving a business object by ID.
    public static final Map<String, Integer> GET_BY_ID_ERROR = new HashMap<String, Integer>() {{
        put("Article", 20601);
        put("Categorie", 20602);
        put("Enchere", 20603);
        put("Retrait", 20604);
        put("Utilisateur", 20605);
    }};
    public static final int AUTHENTICATION_ERROR = 23605;
    public static final int ARTICLE_GET_ALL_FROM_ERROR = 21601;
    public static final int ARTICLE_GET_ALL_BY_NAME_DESCRIPTION = 22601;
    public static final int ARTICLE_SET_ARTICLES_WON = 23601;
    public static final int ENCHERE_GET_HIGHEST_BID = 21603;
    public static final int ENCHERE_GET_ENCHERES_FROM = 22603;
    public static final int ENCHERE_DELETE_ALL_BY = 23603;
    public static final int UTILISATEUR_GET_BY_PSEUDO_ERROR = 21605;
    public static final int UTILISATEUR_GET_BY_EMAIL_ERROR = 22605;
    // Error while retrieving a list of business objects by ID.
    public static final Map<String, Integer> GET_ALL_ERROR = new HashMap<String, Integer>() {{
        put("Article", 20641);
        put("Categorie", 20642);
        put("Enchere", 20643);
        put("Retrait", 20644);
        put("Utilisateur", 20645);
    }};


}
