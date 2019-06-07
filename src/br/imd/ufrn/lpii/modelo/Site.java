package br.imd.ufrn.lpii.modelo;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.imd.ufrn.lpii.modelo.abstratics.Externos;

public class Site implements Externos{

	@Override
	public ArrayList<String> abrir(String url) throws Exception {
		
		ArrayList<String> paragrafos = new ArrayList<String>();
		final StringBuilder textoVermelho = new StringBuilder(); // Garanto que o objeto so vai ser istanciado uma unica vez.
		
		Document doc = Jsoup.connect(url).get();
		
		Elements element = doc.select("p");
		
		for (Element step : element) {
			
			String temp = step.text();
			String tempVermelho = step.select("span[style*=#ff0000]").text();
			
			if(tempVermelho.length() > 100) {
				textoVermelho.append(" " + tempVermelho);
			}
			
			if(temp.length() > 100) {
				paragrafos.add(temp);
			}
		}
		
		paragrafos.add(0,textoVermelho.toString());
		
		return paragrafos;
	}

}
