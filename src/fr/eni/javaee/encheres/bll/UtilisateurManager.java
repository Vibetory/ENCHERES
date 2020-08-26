package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.tools.PasswordTool;

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
        if (utilisateur != null && PasswordTool.checkPassword(password, utilisateur.getMotDePasse())) {
            return utilisateur;
        }
        else { throw new EException(CodesExceptionBLL.AUTHENTICATION_ERROR); }
    }

    @Override
    protected void executeLogic(Utilisateur object, boolean update) throws EException {
        try {
            checkAttributes(object);
            if (!update) { checkUnicity(object); }
        } catch (EException eException) {
            eException.printStackTrace();
            throw new EException(CodesExceptionBLL.UTILISATEUR_ADD_CHECK_ERROR, eException);
        }
        doHashPassword(object);
    }

    private void checkAttributes(Utilisateur utilisateur) throws EException {
        if (utilisateur == null) { throw new EException(CodesExceptionBLL.BO_NULL_ERROR.get("Utilisateur")); }
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
        if (utilisateur.getCredit() < 0) {
            errors.append("Champs incorrecte. Le nombre de crédits ne peut pas être négatif").append("\n");
        }
        if (!errors.toString().isEmpty()) { throw new EException(errors.toString()); }
    }

    private void checkUnicity(Utilisateur utilisateur) throws EException {
        if (getByEmail(utilisateur.getEmail()) != null) {
            throw new EException(CodesExceptionBLL.UTILISATEUR_ADD_EMAIL_ERROR);
        }
        if (getByPseudo(utilisateur.getPseudo()) != null) {
            throw new EException(CodesExceptionBLL.UTILISATEUR_ADD_PSEUDO_ERROR);
        }
    }

    private void doHashPassword(Utilisateur utilisateur) throws EException {
        utilisateur.setMotDePasse(PasswordTool.hashPassword(utilisateur.getMotDePasse()));
    }
}
