package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Retrait;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.dal.DAO;
import fr.eni.javaee.encheres.dal.DAOFactory;

public class CategorieManager extends GenericManager<Categorie> {
    private final DAO<Categorie> DAOCategorie;

    public CategorieManager() throws EException {
        super();
        this.DAOCategorie = DAOFactory.getCategorieDAO();
    }

    @Override
    protected int[] executeLogic(Categorie categorie, String operationCRUD) throws EException {
        boolean alreadyExists = checkUnicity(categorie);
        if (operationCRUD.equals("INSERT") && alreadyExists) { throw new EException(CodesExceptionBLL.ADD_ALREADY_EXIST_ERROR.get("Categorie")); }
        if (operationCRUD.equals("UPDATE") && !alreadyExists) { throw new EException(CodesExceptionBLL.UPDATE_NOT_EXIST_ERROR.get("Categorie")); }
        if (operationCRUD.equals("DELETE") && !alreadyExists) { throw new EException(CodesExceptionBLL.DELETE_NOT_EXIST_ERROR.get("Categorie")); }
        else { checkAttributes(categorie); }
        return operationCRUD.equals("INSERT") ? null : new int[] {categorie.getNoCategorie()};
    }

    private void checkAttributes(Categorie categorie) throws EException {
        if (categorie == null) { throw new EException(CodesExceptionBLL.BO_NULL_ERROR.get("Categorie")); }
        if (categorie.getLibelle() == null || categorie.getLibelle().isEmpty()) {
            throw new EException("Champs obligatoire. La catégorie n'a pas de libellé.");
        }
    }

    private boolean checkUnicity(Categorie categorie) throws EException {
        return DAOCategorie.selectByField("libelle", categorie.getLibelle()) != null;
    }

}
