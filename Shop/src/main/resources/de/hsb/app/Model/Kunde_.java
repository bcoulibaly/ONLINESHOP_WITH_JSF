package de.hsb.app.Model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-12-26T13:17:19.118+0100")
@StaticMetamodel(Kunde.class)
public class Kunde_ {
	public static volatile SingularAttribute<Kunde, Integer> id;
	public static volatile SingularAttribute<Kunde, String> name;
	public static volatile SingularAttribute<Kunde, String> vorname;
	public static volatile SingularAttribute<Kunde, String> passwort;
	public static volatile SingularAttribute<Kunde, String> strasse;
	public static volatile SingularAttribute<Kunde, String> email;
	public static volatile SingularAttribute<Kunde, String> plz;
	public static volatile SingularAttribute<Kunde, String> ort;
	public static volatile SingularAttribute<Kunde, String> benutzername;
	public static volatile SingularAttribute<Kunde, Anrede> anrede;
	public static volatile SingularAttribute<Kunde, Rolle> rolle;
	public static volatile SingularAttribute<Kunde, Date> geburtsdatum;
}
