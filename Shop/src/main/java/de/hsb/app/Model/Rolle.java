package de.hsb.app.Model;

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
