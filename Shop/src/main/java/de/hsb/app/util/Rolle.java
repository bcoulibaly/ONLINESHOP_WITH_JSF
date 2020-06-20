package de.hsb.app.util;

public enum Rolle{
	ADMIN("admin"),KUNDE("kunde");
	
	private String typeRolle;
	
	Rolle(String typeRolle){
		this.typeRolle = typeRolle;
	}
	
	public String getTypeRolle() {
		return this.typeRolle;
	}
}
