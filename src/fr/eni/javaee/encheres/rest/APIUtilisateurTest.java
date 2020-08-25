package fr.eni.javaee.encheres.rest;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.dal.jdbc.ArticleJDBCDAOImpl;
import fr.eni.javaee.encheres.dal.jdbc.CategorieJDBCDAOImpl;
import fr.eni.javaee.encheres.dal.jdbc.UtilisateurJDBCDAOImpl;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.time.LocalDate;

@Path("/utilisateurs")
public class APIUtilisateurTest {


    @DELETE
    @Path("/{identifier: \\d+}")
    public Article deleteUtilisateur(@PathParam("identifier") int identifier) throws EException {
      return new ArticleJDBCDAOImpl().selectById(identifier);
    }
}
