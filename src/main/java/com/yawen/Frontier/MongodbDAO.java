package com.yawen.Frontier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.print.Doc;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.yawen.Fetcher.PageFetcher;
import com.yawen.Url.CrawlUrl;
import com.yawen.Url.CrawlUrlCodec;
import com.yawen.Util.JsonObjectUtil;

/**
 * @author wen
 *
 */
/**
 * @author wen
 *
 */
/**
 * @author wen
 *
 */
/**
 * @author wen
 *
 */
@SuppressWarnings("deprecation")
public class MongodbDAO {
	 protected static final Logger logger = LoggerFactory.getLogger(MongodbDAO.class);
	private static final String HOST="127.0.0.1";
	private static final String dbName="test";
	private static final String collectionName="crawler";
	private static MongoDatabase mongoDatabase;
	private static final Object mutex=new Object();
	private static  MongoClient mongoClient;
	private static MongoCollection<Document>collection;
	private static int Count=0;
	public static int getCount() {
		return Count;
	}

	public static void setCount(int count) {
		Count = count;
	}

	public MongodbDAO()
	{
		try {
			
			CodecRegistry codecRegistry = CodecRegistries.fromRegistries
					(CodecRegistries.fromCodecs
							(new CrawlUrlCodec()),
							MongoClient.getDefaultCodecRegistry());
			MongoClientOptions options = MongoClientOptions.builder()
		            .codecRegistry(codecRegistry).build();
		mongoClient = new MongoClient("127.0.0.1", options);
			mongoDatabase=mongoClient.getDatabase(dbName);
			collection=mongoDatabase.getCollection(collectionName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			   logger.warn("数据库操作异常");
               
		}
	}

	/**
	 * @param map
	 */
	public  void add(Map<String, Object>map)
	{
		synchronized (mutex) {
			try {
				Document document=new Document(map);
				
				
				collection.insertOne(document);
				Count++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				logger.error("数据库添加异常"+e.getMessage());
			}
		}
		
		
	}
	public long DBcount()
	{
		
		return collection.count();
	}
//	@Test
//	public void add()
//	{
//		DBObject dbObject=new BasicDBObject();
//		dbObject.put("_id", 5);
//		dbObject.put("url", new CrawlUrl());
//		DBObject dbObject2=new BasicDBObject();
//		dbObject2.put("_id", 6);
//		dbObject2.put("url", new CrawlUrl());
//		DBCollection dbCollection=db.getCollection(collectionName);
//		dbCollection.insert(dbObject);
//		
//	}
	public void add(List<Map<String,Object>>list )
	{
		 for (Map<String, Object> map : list) {  
	            add(map);  
	        }  
	}
	public  String caculateUrl(String Url)
	{
		return Url;
	}
	public  void remove(Document document)
	{
		
	
		
			try {
				collection.deleteOne(document);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
	
//	@Test
//	public  void find()
//	{
//		DBCollection dbCollection=db.getCollection(collectionName);
//		System.out.println(dbCollection.find());
//	}
	//获得下n条记录
	public List<CrawlUrl> getNext(int n)
	{
		synchronized (mutex) {
			List<CrawlUrl> result=new ArrayList<CrawlUrl>(n);
			
			Document orderBy=new Document("_id", -1);
			
			MongoCursor<Document> mongoCursor=collection.find().sort(orderBy).limit(n).iterator();
			while(mongoCursor.hasNext())
			{
				try {
					Document document= mongoCursor.next();
					
					remove(document);
					
					result.add(JsonObjectUtil.toCrawlUrl(document.get("entity")));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}
				
			}
			return result;
		}
		
	}
	public static void main(String []args)
	{
		
		MongodbDAO mongodbDAO=new MongodbDAO();
		MongoCursor<Document>mongoCursor= collection.find().limit(5).iterator();
		while(mongoCursor.hasNext())
		{
			Document document=(Document) mongoCursor.next().get("entity");
			mongodbDAO.remove(document);
		}
		
	}
}	
