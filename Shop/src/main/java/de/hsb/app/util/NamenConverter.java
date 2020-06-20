package de.hsb.app.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "namenConverter")
public class NamenConverter implements Converter {

	/*
	 * Den ersten Buchstaben des Namens oder Vornamens des Users wird automatisch
	 * Groß geschrieben
	 * 
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value.trim().equals(""))
			return null;
		else {
			try {
				if (!(value.matches("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)"))) 
					throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error",
							"Das eingegebene Daten enthält Nummer, Bitte keine Nummer in diesen Feld eingeben"));
				else {
				char newChar = value.charAt(0);
				newChar = Character.toUpperCase(newChar);
				value = value.replaceFirst(String.valueOf(value.charAt(0)), String.valueOf(newChar));
				return (String) value;
				}
			} catch (NumberFormatException exception) {
				throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Das eingegebene Daten enthält Nummer, Bitte keine Nummer in diesen Feld eingeben"));
			}
		}

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (String) value;
	}

	public String getConverterId() {
		return "namenConverter";
	}
}