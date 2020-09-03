package fr.eni.javaee.encheres.rest;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bll.ArticleManager;
import fr.eni.javaee.encheres.bll.EnchereManager;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.Map;

@Path("/enchere")
public class EnchereREST {
    @Context
    private HttpServletRequest request;

    @GET
    @Path("/highest/{articleVendu: \\d+}")
    public Object getHighestBid(@PathParam("articleVendu") int articleVendu)  {
        try { return new EnchereManager().getHighestBid(articleVendu).getMontantEnchere(); }
        catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
    }

    @POST
    @Path("/new")
    public Object create(Map<String, Object> data)  {
        try {
            Article article = new ArticleManager().getById((int) data.get("articleVendu"));
            Utilisateur utilisateur = (Utilisateur) request.getSession().getAttribute("Utilisateur");
            Enchere enchere = new Enchere(article, utilisateur, (int) data.get("montantEnchere"));
            return new EnchereManager().add(enchere);
        }
        catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
    }
}