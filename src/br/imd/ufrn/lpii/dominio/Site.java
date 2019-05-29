package br.imd.ufrn.lpii.dominio;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Site {
	
	public String[] processar(String url) {
		ArrayList<String> paragrafos = new ArrayList<String>();
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			
			
		
			Elements element = doc.select("p");
			
			for (Element step : element) {
				String temp = step.text();
			
				if(temp.length() > 200) {
					paragrafos.add(temp);
				}
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] temp = new String[paragrafos.size()];
		
		for(int i = 0; i < temp.length;i++) {
			temp[i] = paragrafos.get(i);
		}
		
		return temp;
	}
}
