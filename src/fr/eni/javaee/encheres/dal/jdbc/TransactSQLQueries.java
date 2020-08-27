package fr.eni.javaee.encheres.dal.jdbc;

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


    // ENCHERE

    public static String SELECT_MAX_MONTANT_ENCHERE() {
        return "SELECT Enchere.articleVendu, Enchere.encherisseur, Enchere.dateEnchère, Enchere.montantEnchere FROM Enchere " +
                "INNER JOIN " +
                "(SELECT articleVendu, MAX(montantEnchere) AS montantEnchereMax FROM Enchere WHERE articleVendu = ? GROUP BY articleVendu) AS EnchereMax " +
                "ON Enchere.articleVendu = EnchereMax.articleVendu AND Enchere.montantEnchere = EnchereMax.montantEnchereMax";
    }

}
