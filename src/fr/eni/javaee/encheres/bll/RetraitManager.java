package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Retrait;

public class RetraitManager extends GenericManager<Retrait> {

    // CONSTRUCTOR
    public RetraitManager() throws EException {
        super();
    }


    // LOGIC  & CHECKS

    @Override
    protected int[] getIdentifiers(Retrait retrait) {
        return  new int[] {retrait.getNoArticleARetirer()};
    }

    @Override
    protected void executeUpdate(Retrait retrait, String operationCRUD) throws EException {
        if(operationCRUD.equals("DELETE")) {
            retrait.getArticleARetirer().setRetraitEffectue(true);
        }
    }

    /**
     * Check all the attributes of an retrait.
     * @param retrait Retrait | Retrait to check.
     * @throws EException EException | Newly created exception.
     */
    protected void checkAttributes(Retrait retrait) throws EException {
        if (retrait == null) { throw new EException(CodesExceptionBLL.BO_NULL_ERROR.get("Retrait")); }
        StringBuilder errors = new StringBuilder();
        if (retrait.getArticleARetirer() == null) {
            errors.append("Champs obligatoire. Le retrait n'a pas d'article associé.").append("\n");
        }
        if (retrait.getRue() == null || retrait.getRue().isEmpty()) {
            errors.append("Champs obligatoire. L'adresse de retrait n'a pas de rue renseignée.").append("\n");
        }
        if (retrait.getCodePostal() == null || retrait.getCodePostal().isEmpty()) {
            errors.append("Champs obligatoire. L'adresse de retrait n'a pas de code postal renseigné.").append("\n");
        }
        if (retrait.getVille() == null || retrait.getVille().isEmpty()) {
            errors.append("Champs obligatoire. L'adresse de retrait n'a pas de ville renseignée.").append("\n");
        }
        if (!errors.toString().isEmpty()) { throw new EException(errors.toString()); }
    }

    /**
     * Check if a "retrait" already exists in the database.
     */
    protected boolean checkUnity(Retrait retrait) throws EException {
        return getById(retrait.getNoArticleARetirer()) != null;
    }
}
