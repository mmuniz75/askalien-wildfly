package edu.muniz.universeguide.rest;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import edu.muniz.universeguide.model.Answer;
import edu.muniz.universeguide.model.Question;
import edu.muniz.universeguide.service.QuestionService;


@Path("/answer")
public class AnswerRest extends Rest{
	
		
	@Inject
	private QuestionService questionService;
	
	@GET
	@Path("/detail")
	@Produces({"application/json"})
	public String getAnswer(@Context HttpServletRequest request,@QueryParam("id") Integer id,@QueryParam("search") String search) throws Exception{
		allowCrossDomainAccess();
		
		questionService.setQuestion(search);
		questionService.setIpFromRequest(request);
		questionService.setCountry("Unknown Country?");
		questionService.setAnswer(id);
		
		Question question = (Question)questionService.getObject();
		Answer answer = question.getAnswer();
		
		SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
		String date = dt1.format(answer.getVideo().getCreationDate());
	
		StringBuilder builder = new StringBuilder();
		builder.append("{\"answer\" : {");
		builder.append("\"number\": \"" + answer.getId() + "\",");
		builder.append("\"question\": \"" + answer.getSubject().replaceAll("\"", "'") + "\",") ;
		
		String content = answer.getContent().replaceAll("\"", "'");
		builder.append("\"content\":\"" + content + "\",") ;
		builder.append("\"questionId\": " + question.getId() + ",") ;
		builder.append("\"date\": \"" + date + "\",") ;
				
		builder.append("\"link\": \"" + answer.getUrl() + "\"") ;
		builder.append("}}");
		
		return builder.toString();
		
	}
		
}
