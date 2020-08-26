package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;

import java.util.Collections;
import java.util.Comparator;

public class EnchereManager extends GenericManager<Enchere> {

    public EnchereManager() throws EException {
        super();
    }

    @Override
    protected void executeLogic(Enchere object, boolean update) throws EException {

    }


    public Enchere getHighestBid(int identifier) throws EException {
        return Collections.max(DAOBusinessObject.selectAllByField("articleVendu", identifier), Comparator.comparingInt(Enchere::getMontantEnchere));
    }

    public Enchere getHighestBid(Article article) throws EException {
        return getHighestBid(article.getNoArticle());
    }
}
