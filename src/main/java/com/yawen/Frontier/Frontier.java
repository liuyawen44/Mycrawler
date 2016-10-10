package com.yawen.Frontier;

import java.util.List;
import java.util.Map;

import com.yawen.Url.CrawlUrl;
import com.yawen.crawler.Configurable;
import com.yawen.crawler.CrawlConfig;

public class Frontier extends Configurable {
	private MongodbDAO mongodbDAO;
	public MongodbDAO getMongodbDAO() {
		return mongodbDAO;
	}
	public void setMongodbDAO(MongodbDAO mongodbDAO) {
		this.mongodbDAO = mongodbDAO;
	}
	public boolean isFinished() {
		return isFinished;
	}
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	public Object getMutex() {
		return mutex;
	}
	public Object getWaitingList() {
		return waitingList;
	}
	protected final Object mutex = new Object();
	protected final Object waitingList = new Object();
	private boolean isFinished;
	public Frontier(CrawlConfig config)
	{
		super(config);
		mongodbDAO=new MongodbDAO();
	}

	public void getURL(int n,List<CrawlUrl> result)
	{	while(true){
		 if (isFinished) {
             return;
         }
		synchronized (mutex) {
			List<CrawlUrl> curResults=mongodbDAO.getNext(n);
			result.addAll(curResults);
			 if(result.size()>0)
					return;
		
		}
		 try {
             synchronized (waitingList) {
                 waitingList.wait();
             }
         } catch (InterruptedException ignored) {
             // Do nothing
         }
		
		 if (isFinished) {
             return;
         }
        
	}
	}
	public void scheduleAll(List<Map<String, Object>> urls)
	{
		int maxCount=config.getMaxPageToFetch();
		synchronized (mutex) {
			int newPage=0;
			for (Map<String, Object> map : urls) {
				int count=mongodbDAO.getCount();
				if(newPage+count>=maxCount)
				{
					
					
					break;
				}
				try {
					mongodbDAO.add(map);
					newPage++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 synchronized (waitingList) {
	                waitingList.notifyAll();
	            }
		}
	}
	public void schedule(Map<String, Object> url)
	{
		int maxFetch=config.getMaxPageToFetch();
		synchronized(mutex)
		{
			int count=mongodbDAO.getCount();
			if(count<maxFetch)
			try {
				mongodbDAO.add(url);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			else {
				return;
			}
		}
	}
	public long getDBcount()
	{
		return mongodbDAO.DBcount();
	}
	public void finish() {
        isFinished = true;
        synchronized (waitingList) {
            waitingList.notifyAll();
        }
    }
}
