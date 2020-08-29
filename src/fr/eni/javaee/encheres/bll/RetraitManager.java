package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Retrait;

public class RetraitManager extends GenericManager<Retrait> {

    public RetraitManager() throws EException {
        super();
    }

    @Override
    protected int[] executeLogic(Retrait retrait, String operationCRUD) throws EException {
        boolean alreadyExists = checkUnicity(retrait);
        if (operationCRUD.equals("INSERT") && alreadyExists) { throw new EException(CodesExceptionBLL.ADD_ALREADY_EXIST_ERROR.get("Retrait")); }
        if (operationCRUD.equals("UPDATE") && !alreadyExists) { throw new EException(CodesExceptionBLL.UPDATE_NOT_EXIST_ERROR.get("Retrait")); }
        if (operationCRUD.equals("DELETE") && !alreadyExists) { throw new EException(CodesExceptionBLL.DELETE_NOT_EXIST_ERROR.get("Retrait")); }
        else { checkAttributes(retrait); }
        return new int[] {retrait.getNoArticleARetirer()};
    }

    private void checkAttributes(Retrait retrait) throws EException {
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

    private boolean checkUnicity(Retrait retrait) throws EException {
        return getById(retrait.getNoArticleARetirer()) != null;
    }
}
