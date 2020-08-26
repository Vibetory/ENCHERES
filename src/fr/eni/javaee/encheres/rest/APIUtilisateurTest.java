package fr.eni.javaee.encheres.rest;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.bll.UtilisateurManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/utilisateur")
public class APIUtilisateurTest {


    @GET
    @Path("/{identifier: \\d+}")
    public Utilisateur selectById(@PathParam("identifier") int identifier)  {
        try { return new UtilisateurManager().getById(identifier); }
        catch (EException eException) { eException.printStackTrace(); }
        return null;
    }

    @GET
    public List<Utilisateur> selectAll()  {
        try { return new UtilisateurManager().getAll(); }
        catch (EException eException) { eException.printStackTrace(); }
        return null;
    }
}
