package fr.eni.javaee.encheres.dal.jdbc;

import fr.eni.javaee.encheres.EException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

public class JDBC {
    private static DataSource dataSource;
    static {
        Context context;
        try {
            context = new InitialContext();
            dataSource = (DataSource) context.lookup("/java:comp/env/jdbc/pool_cnx");
        } catch (NamingException namingException) {
            namingException.printStackTrace();
            try { throw new EException(CodesExceptionJDBC.DATABASE_ACCESS_ERROR, namingException); }
            catch (EException eException) {
                System.out.println(eException.getMessage());;
                throw new RuntimeException();
            }
        }
    }

    public static Connection getConnection() throws EException {
        try { return dataSource.getConnection(); }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new EException(CodesExceptionJDBC.CONNECTION_ERROR, sqlException);
        }
    }
}
