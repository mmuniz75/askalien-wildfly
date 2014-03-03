package edu.muniz.universeguide.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneHelper implements IndexingHelper {

	private static int MAX_RESULTS=100;
	private static String ID = "ID";
	private static String[] FIELDS = new String[]{"SUBJECT","CONTENT"};

	private static IndexingHelper instance;

	private LuceneHelper(String indexingPath) {
		this.indexingPath = indexingPath;
	};

	private String indexingPath;

	@Override
	public void indexObject(Integer id, String subject,String content) {
		indexObject(id, subject,content, true);
	}
	
	
	private static Boolean pathAlreadyCreated;
	
	private boolean IsPathAlreadyCreated(){
		if(pathAlreadyCreated==null){
			File path = new File(indexingPath);
			pathAlreadyCreated = path.exists();
		}			
		return pathAlreadyCreated;
	}

	private void indexObject(Integer id, String subject,String content, boolean create) {

		System.out.println("Staring indexing '" + subject + "' with ID=" + id);
		IndexWriter writer = null;
		try {
			
			File indexingFile = new File(indexingPath);
			System.out.println("Indexing using path :" + indexingFile.getAbsoluteFile() );
			Directory dir = FSDirectory.open(indexingFile);
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,
					analyzer);

			if (IsPathAlreadyCreated())
				iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			else {
				iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
				pathAlreadyCreated = true;
			}	
			
			writer = new IndexWriter(dir, iwc);
			String key = Integer.toString(id);

			Document doc = new Document();
			doc.add(new Field(ID, key, Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field(FIELDS[0], subject, Field.Store.NO, Field.Index.ANALYZED));
			doc.add(new Field(FIELDS[1], content, Field.Store.NO, Field.Index.ANALYZED));

			
			if (create)
				writer.addDocument(doc);
			else
				writer.updateDocument(new Term(ID, key), doc);

		} catch (IOException e) {
			System.out.println("Not possible to indexing in class "
					+ e.getClass() + "\n with message: " + e.getMessage());

		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (Exception ex) {
				}
		}

	}

	@Override
	public void updateIndexing(Integer id, String subject,String content) {
		indexObject(id, subject, content,false);

	}

	private String setFuzzy(String keywords){
		String fuzzy = keywords.replaceAll("~" , "");
		fuzzy = fuzzy.replaceAll(" " , "~ ");
		return fuzzy; 
	}
	
	@Override
	public Map<Integer,Float> getIdsFromSearch(String keywords) {

		//keywords = setFuzzy(keywords);
		
		Map<Integer,Float> ids = new HashMap<Integer,Float>();
		IndexReader reader = null;
		IndexSearcher searcher = null;
		try {
			reader = IndexReader.open(FSDirectory.open(new File(indexingPath)));
			searcher = new IndexSearcher(reader);

			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
			MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_35, FIELDS,analyzer);
			//parser.setDefaultOperator(Operator.AND);
			
			
			Query query = parser.parse(keywords);
			
			TopDocs results = searcher.search(query, null, MAX_RESULTS);
			ScoreDoc[] hits = results.scoreDocs;
			for (ScoreDoc hit : hits) {
			    Document doc = searcher.doc(hit.doc);
			    Float score = hit.score;
			    Integer id = Integer.valueOf(doc.get(ID)); 
			    ids.put(id, score);
			}   
	

		} catch (Exception ex) {
			System.out.println("Not possible to search with keyword=" + keywords + "\n with message: " + ex.getMessage());
		} finally {
			try {
				searcher.close();
				reader.close();
			} catch (Exception ex) {
			}
		}

		return ids;
	}

	public static IndexingHelper getInstance(String indexingPath) {
		if (instance == null)
			instance = new LuceneHelper(indexingPath);
		return instance;
	}

}
