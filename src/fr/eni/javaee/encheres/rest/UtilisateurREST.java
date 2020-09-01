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
import java.util.Map;

@Path("/utilisateur")
public class UtilisateurREST {
    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    @POST
    @Path("/signup")
    public Object create(Map<String, Object> utilisateur) {
        try {
            Utilisateur newUtilisateur = generateNewUtilisateur(utilisateur);
            if (newUtilisateur.getMotDePasse() == null) {
                throw new EException(CodesExceptionREST.UTILISATEUR_ADD_PASSWORD_MISSING_ERROR);
            }
            newUtilisateur = new UtilisateurManager().add(newUtilisateur);
            HttpSession session = generateNewSession(newUtilisateur);
            return newUtilisateur;
        } catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
    }

    @PUT
    @Path("/modify")
    public Object update(Map<String, Object> utilisateur) {
        try {
            Utilisateur newUtilisateur = generateNewUtilisateur(utilisateur);
            newUtilisateur = new UtilisateurManager().update(newUtilisateur);
            HttpSession session = generateNewSession(newUtilisateur);
            return newUtilisateur;
        } catch (EException eException) {
            eException.printStackTrace();
            return eException;
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

        Utilisateur utilisateur = null;
        try { utilisateur = new UtilisateurManager().getByPseudoAndPassword(pseudo, motDePasse); }
        catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
        if (utilisateur != null) {
            HttpSession session = generateNewSession(utilisateur, rememberMe);
        }
        return utilisateur;
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

    public Utilisateur generateNewUtilisateur(Map<String, Object> utilisateur) {
        Utilisateur newUtilisateur = new Utilisateur(
                (String) utilisateur.get("pseudo"),
                (String) utilisateur.get("nom"),
                (String) utilisateur.get("prenom"),
                (String) utilisateur.get("email"),
                (String) utilisateur.get("telephone"),
                (String) utilisateur.get("rue"),
                (String) utilisateur.get("codePostal"),
                (String) utilisateur.get("ville")
        );
        if (utilisateur.get("motDePasse") != null) {
            newUtilisateur.setMotDePasse((String) utilisateur.get("motDePasse"));
        }
        if (utilisateur.get("noUtilisateur") != null) {
            newUtilisateur.setNoUtilisateur((Integer) utilisateur.get("noUtilisateur"));
        }
        if (utilisateur.get("credits") != null) {
            newUtilisateur.setCredits((Integer) utilisateur.get("credits"));
        }
        if (utilisateur.get("administrateur") != null) {
            int administrateur = (int) utilisateur.get("administrateur");
            newUtilisateur.setAdministrateur((byte) administrateur);
        }
        return newUtilisateur;
    }

    public HttpSession generateNewSession(Utilisateur utilisateur, boolean rememberMe) {
        HttpSession session = request.getSession();
        session.setAttribute("utilisateur", utilisateur.getNoUtilisateur());
        if (rememberMe) {
            String identifier = String.valueOf(utilisateur.getNoUtilisateur());
            Cookie cookie = new Cookie("CookieIDUtilisateur", identifier);
            cookie.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(cookie);
        }
        return session;
    }

    public HttpSession generateNewSession(Utilisateur utilisateur) {
        return generateNewSession(utilisateur, false);
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
