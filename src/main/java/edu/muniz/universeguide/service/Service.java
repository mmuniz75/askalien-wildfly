package edu.muniz.universeguide.service;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import edu.muniz.universeguide.model.Model;
import edu.muniz.universeguide.model.Question;
import edu.muniz.universeguide.model.Video;



public abstract class Service {

	protected Model object;

	protected Integer objectID;
	
	protected List list;
	
	public void resetList(){
		list = null;
	}
	
	public Integer getObjectID() {
		return objectID;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
		object = em.find(object.getClass(), objectID);
		reset();
	}	

	public Model getObject() {
		return object;
	}

	public void setObject(Model object) {
		this.object = object;
	}

	@PersistenceContext
	protected EntityManager em;
	
	@Resource 
	protected UserTransaction utx;
	
	public void save() {
		try{
			reset();
			utx.begin();
			em.persist(object);
			utx.commit();
			objectID=0;
		}catch(Exception ex){
			try{utx.rollback();}catch(Exception e){}
			ex.printStackTrace();
			error = ex.getMessage();
		}
	}
	
	public String update() {
		try{
			reset();;
			utx.begin();
			Model objectToUpdate = em.find(object.getClass(), object.getId());
			objectToUpdate.populate(object);
			em.persist(objectToUpdate);
			utx.commit();
		}catch(Exception ex){
			try{utx.rollback();}catch(Exception e){}
			ex.printStackTrace();
			error = ex.getMessage();
		}
		return "";
	}
	
	protected String message;
	protected String error;
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	protected void reset(){
		this.message = null;
		this.error = null;
	}
	
	public Number getCount(String query) {
		reset();
		Number count = (Number)em.createQuery(query).getSingleResult();
		return count;
	}
	
}
