package fr.eni.javaee.encheres.rest;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bll.RetraitManager;
import fr.eni.javaee.encheres.bll.UtilisateurManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.HashMap;

@Path("/retrait")
public class RetraitREST {

    @GET
    @Path("/{articleARetirer: \\d+}")
    public Object selectById(@PathParam("articleARetirer") int articleARetirer)  {
        try { return new RetraitManager().getById(articleARetirer); }
        catch (EException eException) {
            eException.printStackTrace();
            return new HashMap<String, String>() {{
                put("message", eException.getMessage());
            }};
        }
    }
}
