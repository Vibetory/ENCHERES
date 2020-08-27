package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.dal.TransactSQLQueries;


import java.sql.SQLException;
import java.util.Collections;

public class EnchereManager extends GenericManager<Enchere> {

    public EnchereManager() throws EException {
        super();
    }

    @Override
    protected void executeLogic(Enchere object, boolean isUpdate) throws EException {

    }

    public Enchere getHighestBid(int identifier) throws EException, SQLException {
//        return Collections.max(
//                DAOBusinessObject.selectAllByField("articleVendu", identifier),
//                Comparator.comparingInt(Enchere::getMontantEnchere)
//        );
        return (Enchere) DAOBusinessObject.selectBy(TransactSQLQueries.SQL_SELECT_MAX_MONTANT_ENCHERE, Collections.singleton(identifier));
    }

    public Enchere getHighestBid(Article article) throws EException, SQLException {
        return getHighestBid(article.getNoArticle());
    }
}
