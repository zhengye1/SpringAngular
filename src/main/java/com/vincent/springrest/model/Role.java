package com.vincent.springrest.model;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name="ROLE")
public class Role implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5141583263876494565L;

	@Id @Column(name="rid") @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="rolename", length=15, unique=true, nullable=false)
	private String roleName = RoleType.USER.getRoleType();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((roleName == null) ? 0 : roleName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public String toString(){
		return (new ReflectionToStringBuilder(this, ToStringStyle.JSON_STYLE)).toString();
	}
}
