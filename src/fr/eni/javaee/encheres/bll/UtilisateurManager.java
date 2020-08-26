package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.bll.tools.Password;

public class UtilisateurManager extends GenericManager<Utilisateur> {
    public UtilisateurManager() throws EException {
        super();
    }

    public Utilisateur getByPseudo(String pseudo) throws EException {
        try { return (Utilisateur) this.DAOBusinessObject.selectByField("pseudo", pseudo); }
        catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionBLL.UTILISATEUR_GET_BY_PSEUDO_ERROR, eException);
        }
    }

    public Utilisateur getByEmail(String email) throws EException {
        try { return (Utilisateur) this.DAOBusinessObject.selectByField("email", email); }
        catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionBLL.UTILISATEUR_GET_BY_EMAIL_ERROR, eException);
        }
    }

    public Utilisateur getByPseudoAndPassword(String pseudo, String password) throws EException {
        Utilisateur utilisateur = getByPseudo(pseudo);
        if (utilisateur != null && Password.checkPassword(password, utilisateur.getMotDePasse())) { return utilisateur; }
        else { throw new EException(CodesExceptionBLL.AUTHENTICATION_ERROR); }
    }

    @Override
    protected void doChecks(Utilisateur object) throws EException {
        try {
            doCheckAttributes(object);
        } catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionBLL.UTILISATEUR_ADD_CHECK_ERROR, eException);
        }
        object.setMotDePasse(Password.hashPassword(object.getMotDePasse()));
    }

    private void doCheckAttributes(Utilisateur utilisateur) throws EException {
        if (utilisateur == null) { throw new EException(CodesExceptionBLL.UTILISATEUR_NULL_ERROR); }
        StringBuilder errors = new StringBuilder();
        if (utilisateur.getPseudo() == null || utilisateur.getPseudo().isEmpty()) {
            errors.append("Champs obligatoire. L'utilisateur n'a pas de pseudonyme.").append("\n");
        }
        if (utilisateur.getPrenom() == null || utilisateur.getNom().isEmpty()) {
            errors.append("Champs obligatoire. L'utilisateur n'a pas de nom.").append("\n");
        }
        if (utilisateur.getPrenom() == null || utilisateur.getPrenom().isEmpty()) {
            errors.append("Champs obligatoire. L'utilisateur n'a pas de nom.").append("\n");
        }
        if (utilisateur.getEmail() == null || utilisateur.getEmail().isEmpty()) {
            errors.append("Champs obligatoire. L'utilisateur n'a pas d'e-mail.").append("\n");
        }
        if (utilisateur.getRue() == null || utilisateur.getRue().isEmpty()) {
            errors.append("Champs obligatoire. L'utilisateur n'a pas de rue renseignée pour son adresse.").append("\n");
        }
        if (utilisateur.getCodePostal() == null || utilisateur.getCodePostal().isEmpty()) {
            errors.append("Champs obligatoire. L'utilisateur n'a pas de code postal renseigné pour son adresse.").append("\n");
        }
        if (utilisateur.getVille() == null || utilisateur.getVille().isEmpty()) {
            errors.append("Champs obligatoire. L'utilisateur n'a pas de ville renseignée pour son adresse.").append("\n");
        }
        if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().isEmpty()) {
            errors.append("Champs obligatoire. L'utilisateur n'a pas de mot de passe.").append("\n");
        }
        if (!errors.toString().isEmpty()) { throw new EException(errors.toString()); }
        if (getByEmail(utilisateur.getEmail()) != null) { throw new EException(CodesExceptionBLL.UTILISATEUR_ADD_EMAIL_ERROR); }
        if (getByPseudo(utilisateur.getPseudo()) != null) { throw new EException(CodesExceptionBLL.UTILISATEUR_ADD_PSEUDO_ERROR); }
    }
}
