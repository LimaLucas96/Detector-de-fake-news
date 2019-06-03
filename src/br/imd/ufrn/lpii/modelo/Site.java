package br.imd.ufrn.lpii.modelo;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Site implements Externos{

	@Override
	public ArrayList<String> Abrir(String url) throws Exception {
		
		ArrayList<String> paragrafos = new ArrayList<String>();
		
		Document doc;
		
		doc = Jsoup.connect(url).get();
			
			
		
		Elements element = doc.select("p");
			
		for (Element step : element) {
			String temp = step.text();
		
			if(temp.length() > 200) {
				paragrafos.add(temp);
			}
		}
				
		
		return paragrafos;
	}

}
