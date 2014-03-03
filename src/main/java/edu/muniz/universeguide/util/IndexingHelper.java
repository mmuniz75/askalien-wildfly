package edu.muniz.universeguide.util;

import java.util.Map;

public interface IndexingHelper {

	public void indexObject(Integer id,String subject,String content);
	public void updateIndexing(Integer id,String subject,String content);
	public Map<Integer,Float> getIdsFromSearch(String keywords);
}
