package fr.eni.javaee.encheres.dal;

public class TransactSQLQueries {
    public static String SQL_SELECT_MAX_MONTANT_ENCHERE =
            "SELECT Enchere.articleVendu, Enchere.encherisseur, Enchere.dateEnchère, Enchere.montantEnchere FROM Enchere INNER JOIN " +
                    "(SELECT articleVendu, MAX(montantEnchere) AS montantEnchereMax FROM Enchere WHERE articleVendu = ? GROUP BY articleVendu) AS EnchereMax " +
                    "ON Enchere.articleVendu = EnchereMax.articleVendu AND Enchere.montantEnchere = EnchereMax.montantEnchereMax";
}
