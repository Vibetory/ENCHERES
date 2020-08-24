package fr.eni.javaee.encheres.dal;

import fr.eni.javaee.encheres.EException;
import fr.eni.javaee.encheres.dal.jdbc.UtilisateurJDBCDAOImpl;

public class TestDAL {
    public static void main(String[] args) {
        try {
            new UtilisateurJDBCDAOImpl().delete(1);
        } catch (EException eException) {
            eException.printStackTrace();
        }
    }
}
