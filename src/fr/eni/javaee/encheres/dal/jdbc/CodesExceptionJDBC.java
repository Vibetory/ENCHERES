package fr.eni.javaee.encheres.dal.jdbc;

import java.util.HashMap;
import java.util.Map;

public abstract class CodesExceptionJDBC {
	public static final int DATABASE_ACCESS_ERROR = 10000;
	public static final int CONNECTION_ERROR = 10001;
	public static final int IDENTIFIER_STATEMENT_ERROR = 10010;
	public static final Map<String, Integer> CRUD_DELETE_ERROR = new HashMap<String, Integer> () {{
		CRUD_DELETE_ERROR.put("Article", 10020);
		CRUD_DELETE_ERROR.put("Categorie", 10021);
		CRUD_DELETE_ERROR.put("Enchere", 10022);
		CRUD_DELETE_ERROR.put("Retrait", 10023);
		CRUD_DELETE_ERROR.put("Utilisateur", 10024);
	}};
	public static final Map<String, Integer> GENERATE_OBJECT_ERROR = new HashMap<String, Integer> () {{
		CRUD_DELETE_ERROR.put("Article", 10030);
		CRUD_DELETE_ERROR.put("Categorie", 10031);
		CRUD_DELETE_ERROR.put("Enchere", 10032);
		CRUD_DELETE_ERROR.put("Retrait", 10033);
		CRUD_DELETE_ERROR.put("Utilisateur", 10034);
	}};
	public static final Map<String, Integer> CRUD_SELECT_ID_ERROR = new HashMap<String, Integer> () {{
		CRUD_DELETE_ERROR.put("Article", 10040);
		CRUD_DELETE_ERROR.put("Categorie", 10041);
		CRUD_DELETE_ERROR.put("Enchere", 10042);
		CRUD_DELETE_ERROR.put("Retrait", 10043);
		CRUD_DELETE_ERROR.put("Utilisateur", 10044);
	}};
	public static final Map<String, Integer> CRUD_SELECT_ALL_ERROR = new HashMap<String, Integer> () {{
		CRUD_DELETE_ERROR.put("Article", 10050);
		CRUD_DELETE_ERROR.put("Categorie", 10051);
		CRUD_DELETE_ERROR.put("Enchere", 10052);
		CRUD_DELETE_ERROR.put("Retrait", 10053);
		CRUD_DELETE_ERROR.put("Utilisateur", 10054);
	}};




}












