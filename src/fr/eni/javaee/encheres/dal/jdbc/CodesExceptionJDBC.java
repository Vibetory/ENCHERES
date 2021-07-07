package fr.eni.javaee.encheres.dal.jdbc;

import java.util.HashMap;
import java.util.Map;

public abstract class CodesExceptionJDBC {
	public static final int DATABASE_ACCESS_ERROR = 10100;
	public static final int CONNECTION_ERROR = 10110;
	public static final int CRUD_INSERT_OR_UPDATE_ERROR = 10200;
	public static final Map<String, Integer> GENERATE_OBJECT_ERROR = new HashMap<String, Integer> () {{
		put("Article", 10201);
		put("Categorie", 10202);
		put("Enchere", 10203);
		put("Retrait", 10204);
		put("Utilisateur", 10205);
	}};
	public static final Map<String, Integer> GENERATE_STATEMENT_DATA_ERROR = new HashMap<String, Integer> () {{
		put("Article", 10221);
		put("Categorie", 10222);
		put("Enchere", 10223);
		put("Retrait", 10224);
		put("Utilisateur", 10225);
	}};
	public static final Map<String, Integer> CRUD_INSERT = new HashMap<String, Integer> () {{
		put("Article", 10301);
		put("Categorie", 10302);
		put("Enchere", 10303);
		put("Retrait", 10304);
		put("Utilisateur", 10305);
	}};
	public static final Map<String, Integer> CRUD_UPDATE = new HashMap<String, Integer> () {{
		put("Article", 10401);
		put("Categorie", 10402);
		put("Enchere", 10403);
		put("Retrait", 10404);
		put("Utilisateur", 10405);
	}};
	public static final Map<String, Integer> CRUD_DELETE_ERROR = new HashMap<String, Integer> () {{
		put("Article", 10501);
		put("Categorie", 10502);
		put("Enchere", 10503);
		put("Retrait", 10504);
		put("Utilisateur", 10505);
	}};
	public static final int CRUD_SELECT_BY_ERROR = 10600;
	public static final Map<String, Integer> CRUD_SELECT_ID_ERROR = new HashMap<String, Integer> () {{
		put("Article", 10601);
		put("Categorie", 10602);
		put("Enchere", 10603);
		put("Retrait", 10604);
		put("Utilisateur", 10605);
	}};
	public static final Map<String, Integer> CRUD_SELECT_FIELD_ERROR = new HashMap<String, Integer> () {{
		put("Article", 10611);
		put("Categorie", 10612);
		put("Enchere", 10613);
		put("Retrait", 10614);
		put("Utilisateur", 10615);
	}};
	public static final int CRUD_SELECT_ALL_BY_ERROR = 10620;
	public static final Map<String, Integer> CRUD_SELECT_ALL_ERROR = new HashMap<String, Integer> () {{
		put("Article", 10621);
		put("Categorie", 10622);
		put("Enchere", 10623);
		put("Retrait", 10624);
		put("Utilisateur", 10625);
	}};





}












