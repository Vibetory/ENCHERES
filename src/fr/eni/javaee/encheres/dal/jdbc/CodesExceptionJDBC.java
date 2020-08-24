package fr.eni.javaee.encheres.dal.jdbc;

import java.util.HashMap;
import java.util.Map;

public abstract class CodesExceptionJDBC {
	public static final int DATABASE_ACCESS_ERROR = 10000;
	public static final int CONNECTION_ERROR = 10001;
	public static final int IDENTIFIER_ERROR = 10002;
	public static final Map<String, Integer> CRUD_DELETE_ERROR = new HashMap<String, Integer> () {{
		CRUD_DELETE_ERROR.put("Article", 10003);
		CRUD_DELETE_ERROR.put("Categorie", 10004);
		CRUD_DELETE_ERROR.put("Enchere", 10005);
		CRUD_DELETE_ERROR.put("Retrait", 10006);
		CRUD_DELETE_ERROR.put("Utilisateur", 10007);
	}};



}












