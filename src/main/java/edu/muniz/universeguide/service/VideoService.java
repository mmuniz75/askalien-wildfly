package edu.muniz.universeguide.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import edu.muniz.universeguide.model.Video;



@ManagedBean
@SessionScoped
public class VideoService extends Service{
		
	public VideoService(){
		this.object = new Video();
	}
	
	
	public Video getVideofromNumber(Integer number){
		reset();
			
		StringBuilder sql = new StringBuilder();
		sql.append("select obj from Video obj ");
		sql.append("where number=" + number);
		
		List<Video> videos = em.createQuery(sql.toString(),Video.class).getResultList();
		
		Video video = null;
		if(videos.size()>0)
			video = videos.get(0);
		
		return video;
		
	}
	
	
	public List<Video> getList() {
		if(list == null){
			reset();
			StringBuilder sql = new StringBuilder();
			sql.append("select obj from Video obj order by number desc");
			list = em.createQuery(sql.toString(),Video.class).getResultList();
		}	
    	return list;
	}

	public void add(){
		this.object = new Video();
	}

	public void save() {
		Video video = (Video)this.object;
		video.setId(null);
		super.save();
	}
	
	
	
		
}
