package fr.eni.javaee.encheres.rest;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Utilisateur;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Path("/utilisateur")
public class UtilisateurREST {
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    @POST
    @Path("/signup")
    public Object create(Map<String, String> data) {
        try {
            Utilisateur newUtilisateur = new Utilisateur(
                    data.get("pseudo"),
                    data.get("nom"),
                    data.get("prenom"),
                    data.get("email"),
                    data.get("telephone"),
                    data.get("rue"),
                    data.get("codePostal"),
                    data.get("ville"),
                    data.get("motDePasse")
            );
            Utilisateur utilisateur = new UtilisateurManager().add(newUtilisateur);
            generateNewSession(utilisateur);
            return newUtilisateur;
        } catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
    }

    @PUT
    @Path("/modify")
    public Object update(Map<String, String> data) {
        try {
            String pseudo = (String) data.get("pseudo");
            String motDePasse = (String) data.remove("motDePasseActuel");
            Utilisateur utilisateur = new UtilisateurManager().getByPseudoAndPassword(pseudo, motDePasse);
            for (Map.Entry<String, String> attribute : data.entrySet()) {
                String method = "set" + attribute.getKey().substring(0, 1).toUpperCase() + attribute.getKey().substring(1);
                Utilisateur.class.getMethod(method, String.class).invoke(utilisateur, attribute.getValue());
            }
            return new UtilisateurManager().update(utilisateur);
        }  catch(EException | InvocationTargetException | NoSuchMethodException | IllegalAccessException exception){
                exception.printStackTrace();
                return exception;
        }
    }

    @DELETE
    @Path("/{noUtilisateur: \\d+}")
    public void delete(@PathParam("noUtilisateur") int noUtilisateur)  {
        try { new UtilisateurManager().delete(noUtilisateur); }
        catch (EException eException) { eException.printStackTrace(); }
    }


    @GET
    @Path("/signin")
    public Object authenticate(
            @QueryParam("pseudo") String pseudo,
            @QueryParam("motDePasse") String motDePasse,
            @QueryParam("rememberMe") boolean rememberMe
            ) {
        try {
            Utilisateur utilisateur = new UtilisateurManager().getByPseudoAndPassword(pseudo, motDePasse);
            if (utilisateur != null) { generateNewSession(utilisateur, rememberMe); }
            return utilisateur;
        }
        catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
    }

    @GET
    @Path("/signout")
    public Object logout() {
        for (int index = 0; index < request.getCookies().length; index ++) {
            Cookie cookie = request.getCookies()[index];
            if (cookie.getName().equals("CookieIDUtilisateur")) {
                cookie.setMaxAge(0);
                response.addCookie(cookie); // Send back an expired cookie.
            }
        }
        HttpSession session = request.getSession(false);
        if (session != null) { session.invalidate(); }
        return false;
    }

    @GET
    @Path("/{noUtilisateur: \\d+}")
    public Object selectById(@PathParam("noUtilisateur") int noUtilisateur)  {
        try { return new UtilisateurManager().getById(noUtilisateur); }
        catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
    }

    @GET
    public Object selectAll()  {
        try { return new UtilisateurManager().getAll(); }
        catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
    }

    @GET
    @Path("/session")
    public Object checkValidity(@CookieParam("CookieIDUtilisateur") String noUtilisateur) {
        try { return validateSession(noUtilisateur); }
        catch (EException eException){
            eException.printStackTrace();
            return eException;
        }
    }

    public void generateNewSession(Utilisateur utilisateur, boolean rememberMe) {
        HttpSession session = request.getSession(true);
        session.setAttribute("utilisateur", utilisateur.getNoUtilisateur());
        if (rememberMe) {
            String identifier = String.valueOf(utilisateur.getNoUtilisateur());
            Cookie cookie = new Cookie("CookieIDUtilisateur", identifier);
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
        }
    }

    public void generateNewSession(Utilisateur utilisateur) {
        generateNewSession(utilisateur, false);
    }

    /**
     * @return Object | Instance of the current user or "false" if the session is not active.
     */
    public Object validateSession(String noUtilisateur) throws EException {
        try {
            if (noUtilisateur != null) { return new UtilisateurManager().getById(Integer.parseInt(noUtilisateur)); }
            HttpSession session = request.getSession(false);
            if (session == null) { return false; }
            return new UtilisateurManager().getById((int) session.getAttribute("noUtilisateur"));
        } catch (EException eException) {
            throw new EException(CodesExceptionREST.SESSION_VALIDATION_ERROR, eException);
        }
    }
}
