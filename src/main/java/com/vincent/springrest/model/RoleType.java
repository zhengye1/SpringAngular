package com.vincent.springrest.model;

import java.io.Serializable;

public enum RoleType implements Serializable{

	USER("USER"),
	ADMIN("ADMIN");
	
	String roleType;
	
	private RoleType(String roleType){
		this.roleType = roleType;
	}
	
	public String getRoleType(){
		return this.roleType;
	}
}
