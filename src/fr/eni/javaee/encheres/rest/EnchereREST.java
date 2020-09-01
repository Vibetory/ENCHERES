package fr.eni.javaee.encheres.rest;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bll.EnchereManager;

import javax.ws.rs.*;

@Path("/enchere")
public class EnchereREST {

    @GET
    @Path("/{articleVendu: \\d+}")
    public Object selectById(@PathParam("articleVendu") int articleVendu)  {
        try { return new EnchereManager().getById(articleVendu); }
        catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
    }
}