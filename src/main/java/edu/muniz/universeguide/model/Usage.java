package edu.muniz.universeguide.model;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="usage")
public class Usage implements Model,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6431402678676165430L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Short year;
	private Byte month;
	private Integer numberUsers;
	private Integer newUsers;

	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}



	public Short getYear() {
		return year;
	}



	public void setYear(Short year) {
		this.year = year;
	}



	public Byte getMonth() {
		return month;
	}



	public void setMonth(Byte month) {
		this.month = month;
	}



	public Integer getNumberUsers() {
		return numberUsers==null?0:numberUsers;
	}

	public Integer getOldUsers() {
		return getNumberUsers()-getNewUsers();
	}

	

	public void setNumberUsers(Integer numberUsers) {
		this.numberUsers = numberUsers;
	}



	public Integer getNewUsers() {
		return newUsers==null?0:newUsers;
	}



	public void setNewUsers(Integer newUsers) {
		this.newUsers = newUsers;
	}



	public void populate(Model sourceObject) {
		Usage usage = (Usage)sourceObject;
		usage.setMonth(usage.getMonth());
		usage.setYear(usage.getYear());
		usage.setNewUsers(usage.getNewUsers());
		usage.setNumberUsers(usage.getNumberUsers());
	} 
	
	public String getMonthName(){
		String[] months = (new DateFormatSymbols(Locale.US)).getMonths();
		return months[month-1]; 
	}
		
	
}
