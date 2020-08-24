package fr.eni.javaee.encheres.dal;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.dal.jdbc.UtilisateurJDBCDAO;

public class TestDAL {
    public static void main(String[] args) throws EException {
        new UtilisateurJDBCDAO().delete(1);
    }
}
