package edu.muniz.universeguide.dbutil;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import edu.muniz.universeguide.model.Country;
import edu.muniz.universeguide.model.Question;
import edu.muniz.universeguide.service.CountryService;
import edu.muniz.universeguide.service.Service;


@ManagedBean
@SessionScoped
public class PopulateDBService extends Service{
	
	@Inject
	CountryService countryService;
	
	private int updates;
	
	public int getUpdates() {
		return updates;
	}


	public void setUpdates(int updates) {
		this.updates = updates;
	}


	public void setCountries() throws Exception{
		updates = 0;
		StringBuilder sql = new StringBuilder("select obj from Question obj where country is null");
			
		List<Question> questions=  em.createQuery(sql.toString(),Question.class).getResultList();
				
		for(Question question:questions){
			
			String ip = question.getIp();
			if(ip.indexOf(".") > 0){
			    String country = countryService.getCountryFromIP(ip);
			    this.object=question;
			    question.setCountry(country);
			    this.update();
			    updates++;
			}    
		}
		
	}
	
	public void populateCountryTable() throws Exception{
		updates = 0;
		StringBuilder sql = new StringBuilder("SELECT new edu.muniz.universeguide.model.Country(ip,country)");
		sql.append("from Question "); 
        sql.append("where country <> 'Unknown Country' ");
        sql.append("group by ip,country");
		
		List<Country> countries=  em.createQuery(sql.toString(),Country.class).getResultList();
				
		for(Country country:countries){
			String ip = country.getIp();
		    String name = country.getCountry(); 
		    try{
		    	countryService.persistCountry(ip, name);
		    	updates++;
		    }catch(Exception ex){
				System.out.println("not populate country for IP = " + ip);
				System.out.println("country name =" + name);
				ex.printStackTrace();
			}	
		    
		}    
	}
	
	public void deleteMyQuestions() throws Exception{
		utx.begin();
		updates = em.createNativeQuery("delete from question where ip='187.21.228.122'").executeUpdate();
		utx.commit();
	}


}
