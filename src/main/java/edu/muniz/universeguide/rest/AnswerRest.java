package edu.muniz.universeguide.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import edu.muniz.universeguide.model.Answer;
import edu.muniz.universeguide.model.Question;
import edu.muniz.universeguide.service.QuestionService;


@Path("/answer")
public class AnswerRest {
	
	@Inject
	private QuestionService questionService;
	
	@GET
	@Path("/detail")
	@Produces({"application/json"})
	public String getAnswer(@QueryParam("id") Integer id,@QueryParam("search") String search,@QueryParam("ip") String ip) throws Exception{
		
		questionService.setQuestion(search);
		questionService.setIp(ip);
		questionService.setCountry("Unknown Country?");
		questionService.setAnswer(id);
		
		Question question = (Question)questionService.getObject();
		Answer answer = question.getAnswer();
	
		StringBuilder builder = new StringBuilder();
		builder.append("{\"answer\" : {");
		builder.append("\"number\": \"" + answer.getId() + "\",");
		builder.append("\"question\": \"" + answer.getSubject().replaceAll("\"", "'") + "\",") ;
		//String content = answer.getContent().replaceAll("\\<.*?\\>", "");
		//content = content.replaceAll("&nbsp", "");
		//String content = answer.getContent().replaceAll("\"", "'");
		
		String content = answer.getContent().replaceAll("\"", "'");
		builder.append("\"content\":\"" + content + "\",") ;
		builder.append("\"video\": " + answer.getVideo().getNumber()) ;
		builder.append("}}");
		
		return builder.toString();
		
	}
		
}
