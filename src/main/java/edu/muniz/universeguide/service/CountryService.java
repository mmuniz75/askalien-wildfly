package edu.muniz.universeguide.service;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import edu.muniz.universeguide.model.Country;

@ManagedBean
@SessionScoped
public class CountryService extends Service {
	
	
	public String getCountryFromIP(String ip) {
		String country = "";
		
		try{
			Country countryObj =  getCountryFromIPPersistence(ip);
			
			if(countryObj==null){
				country= getCountryFromIPFinder(ip);
				persistCountry(ip, country);
			}else
				country = countryObj.getCountry(); 
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return country;
	}
	
	
		
	public Country getCountryFromIPPersistence(String ip) {
		Country country = null;
		reset();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c ");
		sql.append("FROM Country c ");
		sql.append("where ip= '" + ip + "'");
		
		List<Country> countries = em.createQuery(sql.toString(),Country.class).getResultList();
		if(countries.size() > 0)
			country = (Country)countries.get(0);
		
		return country;
	}
	
	public void persistCountry(String ip,String country){
		try{
			Country countryObj = new Country(ip,country);
			this.object = countryObj;
			save();
		}catch(Exception ex){
			System.out.println("not possible save country for IP = " + ip);
			System.out.println("country=" + country);
			ex.printStackTrace();
		}
	}
	

	
	public String getCountryFromIPFinder(String ip) {
		String country="Unknown Country";
		try{
			ip = ip.trim();
			URL url = new URL("http://www.iplocationfinder.com/" + ip);
			
			String page = readPage(url);
			if(page.indexOf("Unknown Country") == -1) {
			    int starts = page.indexOf("Country");
			    page = page.substring(starts);
			    starts = page.indexOf("title") + 7;
			    page = page.substring(starts);
			    			    
			    int end = page.indexOf("(");
			    country = page.substring(0,end-1);
			    country = country.toUpperCase();
			}
			
		}catch(Exception ex){
			System.out.println("not possible find country for IP = " + ip);
			System.out.println("country=" + country);
			ex.printStackTrace();
		}
		return country;
	}
	
	
	/** 
	 * Get using iplocation.net
	 */
	public String getCountryFromIPLocation(String ip) {
		
		String country="Unknown Country";
		try{
			ip = ip.trim();
			URL url = new URL("http://www.iplocation.net/index.php?query=" + ip);
			
			String page = readPage(url);
			if(page.indexOf("Unknown Country") == -1) {
			    int starts = page.indexOf(ip) + ip.length();
			    page = page.substring(starts);
			    starts = page.indexOf(ip) + ip.length();
			    page = page.substring(starts);
			    
			    starts = page.indexOf(ip) + ip.length() + 9;
			    page = page.substring(starts);
			    int end = page.indexOf("</td>");
			    country = page.substring(0,end);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return country;
	}	
	
	
	
	public String getCountryFromIPGeoBytes(String ip) {
		
		String country="Unknown Country";
		try{
			
			URL url = new URL("http://www.geobytes.com/IpLocator.htm?GetLocation&template=php3.txt&IpAddress=" + ip.trim());
			
			String page = readPage(url);
			if(page.indexOf("Unknown Country") == -1) {
			    int starts = page.indexOf("<meta name=\"country\" content=\"") + 30;
			    page = page.substring(starts);
			    int end = page.indexOf("\r\n") - 2;
			    country = page.substring(0,end);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return country;
	}	
	
	
	private String readPage(URL url) throws Exception {
        InputStream in = url.openStream();
	    StringBuffer sb = new StringBuffer();

	    byte [] buffer = new byte[256];

	    while(true){
	        int byteRead = in.read(buffer);
	        if(byteRead == -1)
	            break;
	        for(int i = 0; i < byteRead; i++){
	            sb.append((char)buffer[i]);
	        }
	    }
	    return sb.toString();

    }
	
	public List<Country> getCountryQuestions() {
		reset();
		this.object = new Country();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT new edu.muniz.universeguide.model.Country(question.country,count(question.country)) ");
		sql.append("FROM Question question ");
		sql.append("WHERE question.country NOT IN ('','undefined','Unknown Country') ");
		sql.append("GROUP BY question.country ");
		sql.append("ORDER BY 2 desc");
		
		return em.createQuery(sql.toString(),Country.class).getResultList();
	}

}
