package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;

import java.util.Collection;

public class TransactSQLQueries {

    // GENERIC

    public static String INSERT(String table, int numberOfParameters) {
        StringBuilder unknownParameters = new StringBuilder();
        String separator = "";
        while (numberOfParameters > 0) {
            unknownParameters.append(separator).append("?");
            separator = ", ";
            numberOfParameters --;
        }
        return "INSERT INTO " + table + " VALUES ( " + unknownParameters.toString() + " ) ";
    }

    public static String UPDATE(String table, String fields, String conditions) {
        return "UPDATE " + table + " SET " + fields + " WHERE " + conditions;
    }

    public static String DELETE(String table, String conditions) {
        return "DELETE FROM " + table+ " WHERE " + conditions;
    }

    public static String SELECT(String fields, String table, String conditions) {
        return "SELECT " + fields + " FROM " + table + " WHERE " + conditions;
    }

    public static String SELECT_ALL(String fields, String table) {
        return "SELECT " + fields + " FROM " + table;
    }


    // ARTICLE
    public static String SELECT_ARTICLES_LIKE(String variable, String categorie) throws EException {
        return "SELECT " + new ArticleJDBCDAOImpl().generateQueryFields() + " " +
                "FROM Article " +
                "WHERE (nomArticle LIKE '%" + variable + "%' OR description LIKE '%" + variable + "%') " +
                (categorie == null ? "" : "AND categorie = " + categorie + " ") +
                "ORDER BY nomArticle";
    }

    // ENCHERE

    public static String SELECT_ENCHERE_MAX() throws EException {
        return "SELECT " + new EnchereJDBCDAOImpl().generateQueryFields() + " " +
                "FROM Enchere " +
                "INNER JOIN " +
                "(SELECT articleVendu AS articleVenduMax, MAX(montantEnchere) AS montantEnchereMax " +
                "FROM Enchere " +
                "WHERE articleVendu = ? " +
                "GROUP BY articleVendu) " +
                "ON Enchere.articleVendu = EnchereMax.articleVenduMax AND Enchere.montantEnchere = EnchereMax.montantEnchereMax";
    }

}
