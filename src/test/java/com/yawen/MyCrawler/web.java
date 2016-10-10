package com.yawen.MyCrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class web {

	public static void main(String[] args) {
		String string="<ul class='contactus' id='lxwm' > "
				+ "<li><span>公司名称:</span>百力塑料有限公司</li>  "
				+ "<li><span>公司地址:</span>;;</li> "
				+ " <li><span>电话:</span>13927719362</li> "
				+ "<li><span>传真:</span></li><li><span>手机:</span>13927719362</li>";
                  Document document=Jsoup.parse(string);
                  Elements elements=document.getElementsByClass("contactus").select("li");
                  for(Element element:elements)
                  {
                	 System.out.println(element.text());
                  }
                  
                  
                   
                    
                  

	}

}
