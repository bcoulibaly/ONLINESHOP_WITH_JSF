package de.hsb.app.util;

public enum Anrede {
	
	HERR("Herr"), FRAU("Frau"), OTHER("Other");
	
	private final String label;

	private Anrede(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
