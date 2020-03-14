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
	 * wenn der Kunde bei registrieren oder ändern seiner Daten den ersten
	 * Buchstaben nicht groß schreibt, dann wird dieser automatisch Groß
	 * geschrieben
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (value.trim().equals("")) {
			return null;

		} else {
			try {
				char newChar = value.charAt(0);
				newChar = Character.toUpperCase(newChar);
				value = value.replaceFirst(String.valueOf(value.charAt(0)),
						String.valueOf(newChar));
				return (String) value;

			} catch (NumberFormatException exception) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Das eingegebene Daten enthält Nummer, Bitte keine Nummer in diesen Feld eingeben"));
			}
		}

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		// TODO Auto-generated method stub
		return (String) value;
	}

	public String getConverterId() {
		return "namenConverter";
	}
}