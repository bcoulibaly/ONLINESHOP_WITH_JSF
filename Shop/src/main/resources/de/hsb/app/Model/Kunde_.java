package de.hsb.app.Model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-12-26T13:17:19.118+0100")
@StaticMetamodel(User.class)
public class Kunde_ {
	public static volatile SingularAttribute<User, Integer> id;
	public static volatile SingularAttribute<User, String> name;
	public static volatile SingularAttribute<User, String> vorname;
	public static volatile SingularAttribute<User, String> passwort;
	public static volatile SingularAttribute<User, String> strasse;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, String> plz;
	public static volatile SingularAttribute<User, String> ort;
	public static volatile SingularAttribute<User, String> benutzername;
	public static volatile SingularAttribute<User, Anrede> anrede;
	public static volatile SingularAttribute<User, Rolle> rolle;
	public static volatile SingularAttribute<User, Date> geburtsdatum;
}
