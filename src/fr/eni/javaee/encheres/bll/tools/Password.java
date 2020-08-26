package fr.eni.javaee.encheres.bll.tools;

import org.mindrot.jbcrypt.BCrypt;

public class Password {

    public static String hashPassword(String password) { return BCrypt.hashpw(password, BCrypt.gensalt(12)); }

    public static boolean checkPassword(String password, String storedHash) { return BCrypt.checkpw(password, storedHash); }
}
