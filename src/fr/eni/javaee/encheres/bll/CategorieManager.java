package fr.eni.javaee.encheres.bll;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.bo.Categorie;

public class CategorieManager extends GenericManager<Categorie> {
    public CategorieManager() throws EException {
        super();
    }

    @Override
    protected void executePreLogic(Categorie object, String operationCRUD) throws EException {

    }

}
