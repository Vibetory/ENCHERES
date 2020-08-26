package fr.eni.javaee.encheres.rest;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bll.ArticleManager;
import fr.eni.javaee.encheres.bll.EnchereManager;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/enchere")
public class APIEnchereTest {


    @GET
    public Enchere insert()  {
        try {
            Article article = new ArticleManager().getById(1);
            return new EnchereManager().getHighestBid(article);
        } catch (EException eException) {
            eException.printStackTrace();
        }
        return null;
    }
}