package edu.muniz.universeguide.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import edu.muniz.universeguide.model.Answer;
import edu.muniz.universeguide.model.Video;
import edu.muniz.universeguide.util.IndexingHelper;
import edu.muniz.universeguide.util.LuceneHelper;

@ManagedBean
@RequestScoped
public class AnswerService extends Service{
	
	@Inject
	private VideoService videoService;
	
	private Integer filter;
	public Integer getFilter() {
		return filter;
	}


	public void setFilter(Integer filter) {
		this.filter = filter;
	}

	private boolean justFeedback;
	public boolean isJustFeedback() {
		return justFeedback;
	}


	public void setJustFeedback(boolean justFeedback) {
		this.justFeedback = justFeedback;
	}

	private static final String INDEXING_FOLDER="UNIVERSE_FILES_INDEXING";
	
	private String path;
	private String getIndexingPath(){
		if(this.path==null) { 
			String path = INDEXING_FOLDER;
			String root = System.getenv("OPENSHIFT_DATA_DIR");
			if(root!=null)
				path = root + File.separator + path;
			this.path = path;
		}
		return this.path;
	}
	
	public void setObjectID(Integer objectID) {
		if(objectID==0){
			reset();
			this.object = new Answer();
		}else{
			try{
				utx.begin();
				super.setObjectID(objectID);
				((Answer)this.object).getVideo().getNumber();
				utx.commit();
			}catch(Exception ex){
				ex.printStackTrace();
				error = ex.getMessage();
			}
		}	
		
	}


	public AnswerService(){
		this.object = new Answer();
	}
	
	public void save() {
		saveOrUpdate(true);
	}
	
	public String update() {
		saveOrUpdate(false);
		String page="";
		if(error==null)
			page = "listanswers";
		return page;
	}
	
	private void saveOrUpdate(boolean save) {
		
		Answer answer = (Answer)this.object;
		Video video = videoService.getVideofromNumber(answer.getVideo().getNumber());
		
		if(video!=null) {
			answer.setVideo(video);
			
			try{
				if(save){
					super.save();
					message= "message.answer.saved";
				}else
					super.update();
								
				IndexingHelper indexing = LuceneHelper.getInstance(getIndexingPath());
				
				String content = answer.getContent().replaceAll("\\<.*?>"," ");
				content = content.replaceAll("&nbsp;"," ");
				if(save)
					indexing.indexObject(answer.getId(), answer.getSubject(), content);
				else
					indexing.updateIndexing(answer.getId(), answer.getSubject(), content);					
				
				this.object = new Answer();
			}catch(Exception ex){
				ex.fillInStackTrace();
				if(error==null)
					error="message.answer.indexedfailed";
					
			}	
		}else
			error="message.video.notfound";
	}
		
	
	public List<Answer> searchAnswers(String question){
		IndexingHelper indexing = LuceneHelper.getInstance(getIndexingPath());
		Map<Integer,Float> scoredIds = indexing.getIdsFromSearch(question);
		Set<Integer> ids = scoredIds.keySet();
		StringBuilder ins = new StringBuilder();
		for(Integer id:ids){
			ins.append(id + ",");
		}
		ins.append("0");
		String sql = "select answer from Answer answer where id in (" + ins.toString() + ")";
		List<Answer> answers = em.createQuery(sql,Answer.class).getResultList();
		
		for(Answer answer:answers)
			answer.setScore(scoredIds.get(answer.getId()));
				
		Set<Answer> orderdAnswers = new TreeSet<Answer>(answers);
		return new ArrayList<Answer>(orderdAnswers);
	}


	
	public List<Answer> getList() {
		if(list == null){
			reset();
			this.object = new Answer();
			StringBuilder sql = new StringBuilder();
			sql.append("select obj from Answer obj");
			
			if(filter!=null && filter > 0 )
				sql.append(" where id=" + filter);
			
			sql.append(" order by obj.id");
	
			list = em.createQuery(sql.toString(),Answer.class).getResultList();
		}	
    	return list;	
	}
	
	public List<Answer> getTopAnswers() {
		
		if(list== null) {
			reset();
			this.object = new Answer();
			
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT new edu.muniz.universeguide.model.Answer(answer.id,answer.subject,count(question.id)) ");
			sql.append("FROM Question question join question.answer answer ");
			sql.append("where 1=1 ");
			
			if(justFeedback)
				sql.append("and question.feedback is not null ");
			
			sql.append("GROUP BY answer.id, answer.subject ");
			sql.append("ORDER BY 3 desc");
			
			list =  em.createQuery(sql.toString(),Answer.class).getResultList();
		}	
		return list;
	}

	public Number getCountAnswers() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(answer.id) ");
		sql.append("FROM Answer answer ");
		return getCount(sql.toString());
	}
	
}
