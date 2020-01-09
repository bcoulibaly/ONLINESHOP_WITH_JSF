package de.hsb.app.Model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-12-26T13:17:18.909+0100")
@StaticMetamodel(Artikel.class)
public class Artikel_ {
	public static volatile SingularAttribute<Artikel, Integer> id;
	public static volatile SingularAttribute<Artikel, String> name;
	public static volatile SingularAttribute<Artikel, String> beschreibung;
	public static volatile SingularAttribute<Artikel, Double> preis;
	public static volatile SingularAttribute<Artikel, Integer> anzahl;
	public static volatile SingularAttribute<Artikel, String> imgName;
}
