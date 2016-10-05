package edu.muniz.universeguide.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import edu.muniz.universeguide.model.Answer;
import edu.muniz.universeguide.service.QuestionService;


@Path("/question")
public class QuestionRest {
	
	@Inject
	private QuestionService questionService;
	
	@GET
	@Path("/ask")
	@Produces({"application/json"})
	public String ask(@QueryParam("question") String question) throws Exception{
		
		questionService.setQuestion(question);
		questionService.ask();
		List<Answer> answers = questionService.getAnswers();
	
		StringBuilder builder = new StringBuilder();
		builder.append("{\"questions\": [");
	
		int posi = 0;
		for(Answer answer : answers) {
			builder.append("{");
			builder.append("\"number\": \"" + answer.getId() + "\",");
			builder.append("\"question\": \"" + answer.getSubject().replaceAll("\"", "'") + "\"") ;
			builder.append("}");
			posi++;
			if(posi < answers.size())
				builder.append(",");
		}
			
		builder.append("]}");
		
		return builder.toString();
		
		
	}
		
}
