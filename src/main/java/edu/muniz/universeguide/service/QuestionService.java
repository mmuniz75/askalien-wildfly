package edu.muniz.universeguide.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import edu.muniz.universeguide.model.Answer;
import edu.muniz.universeguide.model.Country;
import edu.muniz.universeguide.model.Question;


@ManagedBean
@SessionScoped
public class QuestionService extends Service{
	
	@Inject
	private CountryService countryService;
	
	@Inject
	private AnswerService answerService;
	
	private String question;
	private String ip;
	private String country;
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		if(this.country==null){
			if(country.indexOf("Unknown Country?") ==-1)
				this.country=country.trim();
			else {
				if(!ip.equals("x.x.x.x"))
					this.country = countryService.getCountryFromIP(ip);
				else
					this.country = "Unknown Country";
			}	
		}
	}

	private boolean justFeedback;
	private boolean justThisMonth;
	private Date startDate;
	private Date endDate;
	
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		this.justThisMonth = false;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		this.justThisMonth = false;
	}

	public boolean isJustThisMonth() {
		return justThisMonth;
	}

	public void setJustThisMonth(boolean justThisMonth) {
		this.justThisMonth = justThisMonth;
		this.endDate = null;
		this.startDate = null;
	}

	private Integer answerId;
	private String questionFilter;
	private String ipFilter;
	
	
	
	public String getIpFilter() {
		return ipFilter;
	}

	public void setIpFilter(String ipFilter) {
		this.ipFilter = ipFilter;
	}

	public String getQuestionFilter() {
		return questionFilter;
	}

	public void setQuestionFilter(String questionFilter) {
		this.questionFilter = questionFilter;
	}

	public Integer getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Integer answerId) {
		this.answerId = answerId;
		if(answerId==0) {
			this.justFeedback = false;
			this.justThisMonth = true;
		}else{
			this.justFeedback = false;
			this.justThisMonth = false;
		}	
	}

	public boolean isJustFeedback() {
		return justFeedback;
	}

	public void setJustFeedback(boolean justFeedback) {
		this.justFeedback = justFeedback;
	}

	public QuestionService(){
		this.justFeedback = false;
		this.justThisMonth = true;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip.trim();
	}

	private List<Answer> answers; 
	
	
	public void ask(){
		answers = answerService.searchAnswers(question);
	}
		
	public List<Answer>getAnswers(){
		return answers;
	}
		
	
	public void setAnswer(Integer answerID) {
		Answer answer = answerService.getAnswerById(answerID); 
		
		Question question = new Question();
		question.setText(this.question);
		question.setIp(this.ip);
		question.setCountry(this.country);
		
		question.setAnswer(answer);
		
		this.object = question;
		try{
			super.save();
		}catch(Exception ex){
			ex.fillInStackTrace();
		}
	}


	@Override
	public void setObjectID(Integer objectID) {
		StringBuilder sql = new StringBuilder();
		sql.append("select obj from Question obj ");
		sql.append("JOIN FETCH obj.answer a ");
		sql.append("JOIN FETCH a.video ");
		sql.append("where obj.id=" + objectID);
		
		List<Question> questions = em.createQuery(sql.toString(),Question.class).getResultList();
		
		if(questions.size()>0)
			this.object = questions.get(0);
	}
	
	private void startObject(Integer objectID){
		
	}

	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}
	
	public void sendFeedback() throws Exception{
		try{
			super.update();
			Question question = (Question)object;
			question.setFeedback("");
		}catch(Exception ex){
			ex.fillInStackTrace();
		}
		this.object = null;
		
	}
	
	public void unChooseAnswer(){
		this.object = null;
	}
	
	private long countCurrentQuestions;
	

	

	public long getCountCurrentQuestions() {
		return countCurrentQuestions;
	}

	public void setCountCurrentQuestions(long countCurrentQuestions) {
		this.countCurrentQuestions = countCurrentQuestions;
	}

	public List<Question> getList(){
		
		if(list == null) {
			StringBuilder sql = new StringBuilder("select obj from Question obj where 1=1 ");
			if(justFeedback)
				sql.append(" and obj.feedback is not null");
			
			if(answerId != null && answerId > 0)
				sql.append(" and obj.answer.id =" + answerId);
			
			if(questionFilter != null && questionFilter.length() > 0)
				sql.append(" and obj.text like '%" + questionFilter + "%'");
			
			if(ipFilter != null && ipFilter.length() > 0)
				sql.append(" and obj.ip like '%" + ipFilter + "%'");
			
			
			if(justThisMonth)		
				sql.append(" and obj.creationDate >= :monthDate");
			
			if(startDate != null)		
				sql.append(" and obj.creationDate >= :startDate");
			
			if(endDate != null)		
				sql.append(" and obj.creationDate <= :endDate");
							
			sql.append(" order by creationdate desc");
			
			Query query = em.createQuery(sql.toString(),Question.class);
		
			if(justThisMonth){
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.DATE, 1);
				cal.set(Calendar.HOUR, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.set(Calendar.AM_PM, Calendar.AM);
				query.setParameter("monthDate", cal.getTime());
			}
			
			if(startDate != null)		
				query.setParameter("startDate", startDate);
			
			if(endDate != null)		
				query.setParameter("endDate", endDate);
			
			List<Question> questions = query.getResultList();  
			
			countCurrentQuestions = questions.size();
			list = questions;
		}
		
		return list;
	}
	
	
	private Number countQuestions;
	public Number getCountQuestions() {
		if(countQuestions==null) {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(question.id) ");
			sql.append("FROM Question question ");
			countQuestions = getCount(sql.toString());
		}
		return countQuestions;
	}
	
	private Number countUsers;
	public Number getCountUsers() {
		if(countUsers==null) {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(DISTINCT question.ip) ");
			sql.append("FROM Question question ");
				
			countUsers= getCount(sql.toString());
		}
		return countUsers;
	}
	
	private Number countCountries;
	public Number getCountCountries() {
		if (countCountries==null){
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(DISTINCT question.country) ");
			sql.append("FROM Question question ");
				
			countCountries = getCount(sql.toString());
		}
		return countCountries; 
	}
	
	private List<Question> frequentUsers;
	public List<Question> getFrequentUsers() {
		if(frequentUsers==null) {
			reset();
			this.object = new Country();
			
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT new edu.muniz.universeguide.model.Question(question.ip,question.country,count(question.ip)) ");
			sql.append("where ip not in ('','x.x.x.x')");
			sql.append("group by ip,country");
			sql.append("having count(ip) > 10");
			sql.append("order by 3 desc");
			
			frequentUsers = em.createQuery(sql.toString(),Question.class).getResultList();
		}
		return frequentUsers;
	}
	
	private Number countFrequentUsers; 
	public Number getCountFrequentUsers() {
		if(countFrequentUsers==null) {
			reset();
			this.object = new Country();
			
			StringBuilder sql = new StringBuilder();
			sql.append("select count(*) from ");
			sql.append("(");
			sql.append("select ip,count(ip) from question ");
			sql.append("where ip not in ('','x.x.x.x') ");
			sql.append("group by ip ");
			sql.append("having count(ip) > 10 ");
			sql.append(") ");
			sql.append("as users");
			
			Number count = (Number)em.createNativeQuery(sql.toString()).getSingleResult();
			countFrequentUsers = count;
		}
		return countFrequentUsers;
	}
	
	
	public void resetCountList(){
		countQuestions = null;
		countUsers = null;
		countCountries = null;
		frequentUsers = null;
		countFrequentUsers = null; 
	}
	
}
