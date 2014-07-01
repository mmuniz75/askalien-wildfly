package edu.muniz.universeguide.service;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import edu.muniz.universeguide.model.Usage;


@ManagedBean
@SessionScoped
public class UsageService extends Service{
	
	public UsageService(){
		this.object = new Usage();
		year = (short)Calendar.getInstance().get(Calendar.YEAR);
	}

	private Short year;
	
	public Short getYear() {
		return year;
	}

	public void setYear(Short year) {
		this.year = year;
	}

	public List<Usage> getUsageFromYear(){
		reset();
			
		StringBuilder sql = new StringBuilder();
		sql.append("select obj from Usage obj ");
		sql.append("where year=" + year);
		sql.append(" order by month");
		
		List<Usage> usages = em.createQuery(sql.toString(),Usage.class).getResultList();
		
		return usages;
		
	}
	
	public List<Short> getYears(){
		List<Short> years = new ArrayList<Short>();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for(short i=2012;i<=currentYear;i++){
			years.add(i);
		}
		
		return years;
	}
	
	public void updateUsage(){
		try{
			utx.begin();
			Object delegate = em.getDelegate();
			Method method = delegate.getClass().getMethod("connection", null);
			Connection conn = (Connection)method.invoke(delegate, null); 
		    PreparedStatement ps = conn.prepareStatement("select update_usage()");
		    ps.execute();
			utx.commit();
		}catch(Exception ex){
			try{utx.rollback();}catch(Exception e){}
			ex.printStackTrace();
			error = ex.getMessage();
		}
	}
	
}
