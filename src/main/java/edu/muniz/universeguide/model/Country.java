package edu.muniz.universeguide.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name="country",uniqueConstraints=@UniqueConstraint(columnNames="ip"))
public class Country implements Model,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5891646822377239876L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}

	@Transient
	private Long countQuestions;
	
	
	public Long getCountQuestions() {
		return countQuestions;
	}

	public void setCountQuestions(Long countQuestions) {
		this.countQuestions = countQuestions;
	}

	public Country() {
		super();
	}
	
	public Country(String ip, String country) {
		super();
		this.ip = ip;
		this.country = country;
	}
	
	public Country(String country,Long countQuestions) {
		super();
		this.countQuestions = countQuestions;
		this.country = country;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	@Column(length=50,unique=true)
	private String ip;
	
	@Column(length=50)
	private String country;

	@Override
	public void populate(Model sourceObject) {
		Country country = (Country)sourceObject;
		this.country = country.getCountry();
		this.ip = country.getIp();
		
	}


}
