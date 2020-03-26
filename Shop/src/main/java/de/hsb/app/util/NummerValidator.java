package de.hsb.app.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "nummerValidator")
public class NummerValidator implements Validator {

	/**
	 * Der PLZ, Security CODE sollen nur aus zahlen besetehen
	 * 
	 */
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		String gg = String.valueOf(value);
		
		try {
			 Long.valueOf(gg);
		} catch (NumberFormatException ex) {
			throw new ValidatorException(
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Validationsfehler",
							value
									+ ":  Den Input soll nur aus zahlen bestehen"));
		}
	}

	public String getValidatorId() {
		return "nummerValidator";
	}
}