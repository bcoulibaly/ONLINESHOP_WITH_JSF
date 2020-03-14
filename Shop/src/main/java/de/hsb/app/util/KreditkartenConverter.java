package de.hsb.app.util;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "kreditkartenConverter")
public class KreditkartenConverter implements Converter {
	
	/*
	 * Sollte ein Leerzeichen, ein Komma oder ein Bindestrich in der Kreditkartennummer und PLZ und SECURITY CODE enthalten sein,
	 * wird mit diese mit dem Kreditkartenvalidator ein einheitliche Nummer umgewandelt.
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (value == null) {
			return null;
		}
		String s = value;
		if (value.contains(" ")) {
			s = value.replace(" ", "");
		}
		if (value.contains("-")) {
			s = value.replace("-", "");
		}
		return s;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return (String) value;
	}

}