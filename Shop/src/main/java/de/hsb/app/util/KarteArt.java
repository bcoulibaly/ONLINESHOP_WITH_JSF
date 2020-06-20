package de.hsb.app.util;

public enum KarteArt {
	
	MASTERCARD("Mastercard"), VISA("Visa");
	
	private final String label;

	private KarteArt(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
