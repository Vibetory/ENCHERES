package fr.eni.javaee.encheres.dal;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.*;
import fr.eni.javaee.encheres.dal.jdbc.*;

public class DAOFactory {
    public static DAO<Article> getArticleDAO() throws EException { return new ArticleJDBCDAOImpl(); }
    public static DAO<Utilisateur> getUtilisateurDAO() throws EException { return new UtilisateurJDBCDAOImpl(); }
    public static DAO<Categorie> getCategorieDAO() throws EException { return new CategorieJDBCDAOImpl(); }
    public static DAO<Retrait> getRetraitDAO() throws EException { return new RetraitJDBCDAOImpl(); }
    public static DAO<Enchere> getEnchereDAO() throws EException { return new EnchereJDBCDAOImpl(); }
    public static DAO<?> getBusinessObjectDAO(String classSimpleName) throws EException {
        switch (classSimpleName) {
            case "Article":
                return getArticleDAO();
            case "Utilisateur":
                return getUtilisateurDAO();
            case "Categorie":
                return getCategorieDAO();
            case "Retrait":
                return getRetraitDAO();
            case "Enchere":
                return getEnchereDAO();
            default:
                throw new EException(CodesExceptionDAL.GENERIC_FACTORY_ERROR);
        }
    }
}
