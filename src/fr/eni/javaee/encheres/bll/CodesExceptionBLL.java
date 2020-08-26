package fr.eni.javaee.encheres.bll;

import java.util.HashMap;
import java.util.Map;

public class CodesExceptionBLL {
    public static final Map<String, Integer> INITIALIZATION_DAO_ERROR = new HashMap<String, Integer>() {{
        put("Article", 20001);
        put("Categorie", 20002);
        put("Enchere", 20003);
        put("Retrait", 20004);
        put("Utilisateur", 20005);
    }};
    public static final Map<String, Integer> BO_NULL_ERROR = new HashMap<String, Integer>() {{
        put("Article", 20201);
        put("Categorie", 20202);
        put("Enchere", 20203);
        put("Retrait", 20204);
        put("Utilisateur", 20205);
    }};
    public static final Map<String, Integer> ADD_ERROR = new HashMap<String, Integer> () {{
        put("Article", 20301);
        put("Categorie", 20302);
        put("Enchere", 20303);
        put("Retrait", 20304);
        put("Utilisateur", 20305);
    }};
    public static final int UTILISATEUR_ADD_EMAIL_ERROR = 21305;
    public static final int UTILISATEUR_ADD_PSEUDO_ERROR = 22305;
    public static final int UTILISATEUR_ADD_CHECK_ERROR = 29305;
    public static final Map<String, Integer> GET_BY_ID_ERROR = new HashMap<String, Integer>() {{
        put("Article", 20601);
        put("Categorie", 20602);
        put("Enchere", 20603);
        put("Retrait", 20604);
        put("Utilisateur", 20605);
    }};
    public static final int UTILISATEUR_GET_BY_EMAIL_ERROR = 21605;
    public static final int UTILISATEUR_GET_BY_PSEUDO_ERROR = 22605;
    public static final int AUTHENTICATION_ERROR = 23605;
    public static final int ARTICLE_GET_ALL_FROM_ERROR = 21621;
    public static final Map<String, Integer> GET_ALL_ERROR = new HashMap<String, Integer>() {{
        put("Article", 20641);
        put("Categorie", 20642);
        put("Enchere", 20643);
        put("Retrait", 20644);
        put("Utilisateur", 20645);
    }};


}
