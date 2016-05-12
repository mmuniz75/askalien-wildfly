package edu.muniz.universeguide.rest;

import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import edu.muniz.universeguide.model.Country;
import edu.muniz.universeguide.service.CountryService;


@Path("/country")
public class CountryRest {
	
	@Inject
	private CountryService countryService;
	
	@GET
	@Path("/count2")
	@Produces({"application/json"})
	public JsonArray getCountryCountArray() {
		List<Country> countries = countryService.getCountryQuestions();
		JsonArrayBuilder countryJson = Json.createArrayBuilder();
		countryJson.add(Json.createArrayBuilder().add("Country").add("Questions"));
				
		for(Country country : countries)
			countryJson.add(Json.createArrayBuilder().add(country.getCountry()).add(country.getCountQuestions()));
			
				
		return countryJson.build();
	}
	
	@GET
	@Path("/count")
	@Produces({"application/json"})
	public String getCountryCountString() {
		List<Country> countries = countryService.getCountryQuestions();
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		
		builder.append("[");
		builder.append("\"Country\",");
		builder.append("\"Questions\"");
		builder.append("],");
						
		for(Country country : countries){
			if(country.getCountry().equals("UNITED STATES"))
				continue;
			builder.append("[");
			builder.append("\"" + country.getCountry() + "\",");
			builder.append(country.getCountQuestions());
			builder.append("],");
		}
		builder.append("[");
		builder.append("\"end\",");
		builder.append("0");
		builder.append("]");
		
		builder.append("]");
		
		return builder.toString();
	}
		
}
