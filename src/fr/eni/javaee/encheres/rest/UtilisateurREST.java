package fr.eni.javaee.encheres.rest;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.tools.PasswordTool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/utilisateur")
public class UtilisateurREST {
    @Context
    private HttpServletRequest request;

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public Object create(Map<String, String> utilisateur) {
        try {
            Utilisateur newUtilisateur = new Utilisateur(
                    utilisateur.get("pseudo"),
                    utilisateur.get("nom"),
                    utilisateur.get("prenom"),
                    utilisateur.get("email"),
                    utilisateur.get("telephone"),
                    utilisateur.get("rue"),
                    utilisateur.get("codePostal"),
                    utilisateur.get("ville"),
                    utilisateur.get("motDePasse")
            );
            newUtilisateur = new UtilisateurManager().add(newUtilisateur);
            HttpSession session = getSession(request);
            session.setAttribute("noUtilisateur", newUtilisateur.getNoUtilisateur());
            session.setAttribute("pseudo", newUtilisateur.getPseudo());
            session.setAttribute("motDePasse", PasswordTool.hashPassword(newUtilisateur.getMotDePasse()));
            System.out.println(session.getId());
            return newUtilisateur;
        } catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
    }


    @GET
    @Path("/signin")
    public Object authenticate(@QueryParam("pseudo") String pseudo, @QueryParam("motDePasse") String motDePasse) {
        HttpSession session = getSession(request);
        Utilisateur utilisateur = null;
        try { utilisateur = new UtilisateurManager().getByPseudoAndPassword(pseudo, motDePasse); }
        catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
        if (utilisateur != null) {
            session.setAttribute("noUtilisateur", utilisateur.getNoUtilisateur());
            session.setAttribute("pseudo", utilisateur.getPseudo());
            session.setAttribute("motDePasse", PasswordTool.hashPassword(utilisateur.getMotDePasse()));
        }
        return utilisateur;
    }

    @GET
    @Path("/signout")
    public void logout() {
        HttpSession session = getSession(request);
        session.invalidate();
    }

    @GET
    @Path("/{noUtilisateur: \\d+}")
    public Object selectById(@PathParam("noUtilisateur") int identifier)  {
        try { return new UtilisateurManager().getById(identifier); }
        catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
    }

    @GET
    @Path("/all")
    public Object selectAll()  {
        try { return new UtilisateurManager().getAll(); }
        catch (EException eException) {
            eException.printStackTrace();
            return eException;
        }
    }

    @GET
    @Path("/session")
    public Object checkValidity() {
        try { return validateSession(request); }
        catch (EException eException){
            eException.printStackTrace();
            return eException;
        }
    }

    public HttpSession getSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) { session =  request.getSession(); }
        return session;
    }

    /**
     * @param request HttpServletRequest | Request in the current context.
     * @return Utilisateur | "null" if the session is not active.
     * @throws EException EException
     */
    public Utilisateur validateSession(HttpServletRequest request) throws EException {
        HttpSession session = request.getSession(false);
        if (session == null) { return null; }
        String pseudo = (String) session.getAttribute("pseudo");
        String motDePasse = (String) session.getAttribute("motDePasse");
        int noUtilisateur = (int) session.getAttribute("noUtilisateur");
        try {
            Utilisateur utilisateur = new UtilisateurManager().getById(noUtilisateur);
            if (utilisateur.getPseudo().equals(pseudo) &&
                    PasswordTool.checkPassword(motDePasse, PasswordTool.hashPassword(utilisateur.getMotDePasse()))) {
                return utilisateur;
            }
            return null;
        } catch (EException eException) {
            throw new EException("Exception à créer", eException);
        }
    }
}
