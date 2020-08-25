package fr.eni.javaee.encheres.rest;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.dal.jdbc.UtilisateurJDBCDAOImpl;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/utilisateurs")
public class APIUtilisateurTest {


    @DELETE
    @Path("/{identifier: \\d+}")
    public void deleteUtilisateur(@PathParam("identifier") int identifier) throws EException {
        new UtilisateurJDBCDAOImpl().delete(identifier);
    }
}
