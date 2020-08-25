package fr.eni.javaee.encheres.rest;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Article;
import fr.eni.javaee.encheres.dal.jdbc.ArticleJDBCDAOImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/article")
public class APIArticleTest {


    @GET
    @Path("/{identifier: \\d+}")
    public Article selectById(@PathParam("identifier") int identifier)  {
        try {
            return new ArticleJDBCDAOImpl().selectById(identifier);
        } catch (EException eException) {
            eException.printStackTrace();
        }
        return null;
    }
}
