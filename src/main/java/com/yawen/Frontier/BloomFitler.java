package com.yawen.Frontier;

import java.util.BitSet;

public class BloomFitler {
	 
	private final static Object mutex=new Object();
	private static final int DEFAULT_SIZE = 2 << 24;//布隆过滤器的比特长度  
    private static final int[] seeds = {3,5,7, 11, 13, 31, 37, 61};//这里要选取质数，能很好的降低错误率  
    private static BitSet bits = new BitSet(DEFAULT_SIZE);  
    private static SimpleHash[] func ;  
   public BloomFitler(){
 	   func = new SimpleHash[seeds.length];
 	   for (int i = 0; i < seeds.length; i++) {  
            func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);  
        }  
    }
    public  void addValue(String value)  
    {  
    	synchronized (mutex) {
    		 for(SimpleHash f : func)//将字符串value哈希为8个或多个整数，然后在这些整数的bit上变为1  
    	            bits.set(f.hash(value),true);  
		}
       
    }  
      
    public void add(String value)  
    {  synchronized (mutex) {
        if(value != null) addValue(value);  
    }
    }  
      
    public  boolean contains(String value)  
    {  
    	synchronized (mutex) {
        if(value == null) return false;  
        boolean ret = true;  
        for(SimpleHash f : func)//这里其实没必要全部跑完，只要一次ret==false那么就不包含这个字符串  
            ret = ret && bits.get(f.hash(value));  
        return ret;  
    	}
    }  
    public static void main(String args[])
    {
    	BloomFitler bloomFitler=new BloomFitler();
    	Boolean boolean1=bloomFitler.contains("https://www.zhihu.com/question/29693016/answer/123859334?group_id=764549459378991104");
    	System.out.println(boolean1);
    }
      
   
}
