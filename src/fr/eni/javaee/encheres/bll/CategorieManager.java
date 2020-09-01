package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.*;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategorieManager extends GenericManager<Categorie> {
    private final DAO<Categorie> DAOCategorie;

    // CONSTRUCTOR
    public CategorieManager() throws EException {
        super();
        this.DAOCategorie = DAOFactory.getCategorieDAO();
    }


    // LOGIC  & CHECKS

    /**
     * Get an array of the identifiers values for a given Categorie.
     */
    @Override
    protected int[] getIdentifiers(Categorie categorie) {
        return new int[] {categorie.getNoCategorie()};
    }

    /**
     * Executed after the operation.
     */
    @Override
    protected void executeUpdate(Categorie categorie, String operationCRUD) throws EException {
        Map<String, Object> fields = new HashMap<String, Object>() {{
            put("categorie", categorie.getNoCategorie());
        }};
        List<Article> articles = DAOFactory.getArticleDAO().selectAllByFields(fields);
        for (Article article : articles) {
            article.setCategorie(null);
            new ArticleManager().update(article);
        };
    }

    /**
     * Check all the attributes of a categorie.
     * @param categorie Categorie | Categorie to check.
     * @throws EException EException | Newly created exception.
     */
    protected void checkAttributes(Categorie categorie) throws EException {
        if (categorie == null) { throw new EException(CodesExceptionBLL.BO_NULL_ERROR.get("Categorie")); }
        if (categorie.getLibelle() == null || categorie.getLibelle().isEmpty()) {
            throw new EException("Champs obligatoire. La catégorie n'a pas de libellé.");
        }
    }

    /**
     * Check if an article already exists in the database.
     */
    protected boolean checkUnity(Categorie categorie) throws EException {
        return DAOCategorie.selectByField("libelle", categorie.getLibelle()) != null;
    }

}
