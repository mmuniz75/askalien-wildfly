package edu.muniz.universeguide.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="question")
public class Question implements Model,Serializable{

	private static final long serialVersionUID = -212382478014932790L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length=50)
	private String ip;
		
	@Basic(fetch=FetchType.LAZY)
	@Column(columnDefinition="TEXT")
	private String text;

	@Column(length=50)
	private String email;

	@Basic(fetch=FetchType.LAZY)
	@Column(columnDefinition="TEXT")
	private String feedback;

	@Basic(fetch=FetchType.LAZY)
	@ManyToOne
	private Answer answer;
	
	private Date creationDate;
	
	@Column(length=50)
	private String creator;
	
	@Transient
	private Long countUsers;
	
	@Column(length=50)
	private String country;

	
	public Long getCountUsers() {
		return countUsers;
	}

	public void setCountUsers(Long countUsers) {
		this.countUsers = countUsers;
	}

	public Question(){
		Calendar cal = Calendar.getInstance();
		creationDate = cal.getTime();
	}
	
	public Question(String ip,String country,Long users){
		this.ip=ip;
		this.country=country;
		this.countUsers = users;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getId() {
		return id;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public void populate(Model sourceObject) {
		Question newObject = (Question)sourceObject;
		this.creator = newObject.getCreator();
		this.email = newObject.getEmail();
		this.feedback = newObject.getFeedback();
		this.country = newObject.getCountry();
	}

}