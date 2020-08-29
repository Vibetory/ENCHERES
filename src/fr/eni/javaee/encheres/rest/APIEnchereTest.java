package fr.eni.javaee.encheres.rest;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bll.ArticleManager;
import fr.eni.javaee.encheres.bll.EnchereManager;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Path("/enchere")
public class APIEnchereTest {


    @GET
    public Enchere getBid()  {
        try {
            Article article = new ArticleManager().getById(1);
            return new EnchereManager().getHighestBid(article);
        } catch (EException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @POST
    public Enchere insert()  {
        try {
            Article article = new ArticleManager().getById(1);
            Utilisateur utilisateur = new UtilisateurManager().getById(12);
            return new EnchereManager().add(new Enchere(article, utilisateur, LocalDateTime.now(), 15785589));
        } catch (EException eException) {
            eException.printStackTrace();
        }
        return null;
    }
}