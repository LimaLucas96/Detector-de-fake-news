package br.imd.ufrn.lpii.model;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.imd.ufrn.lpii.model.abstratics.Externos;

public class Site implements Externos{

	@Override
	public ArrayList<String> abrir(String url) throws Exception {
		
		ArrayList<String> paragrafos = new ArrayList<String>();
		final StringBuilder textoVermelho = new StringBuilder(); // Garanto que o objeto so vai ser istanciado uma unica vez.
		
		Document doc = Jsoup.connect(url).get(); // se conecta ao site da url
		
		Elements element = doc.select("p"); // procurar por todas as tags de paragrafos do HTML
		
		for (Element step : element) {
			
			String temp = step.text(); // coleta o texto dentro da tag
			String tempVermelho = step.select("span[style*=#ff0000]").text(); // coleta todo o texto que estiver em vermelho
			//filtra os texto com mais de 100 characteres
			if(tempVermelho.length() > 100) {
				textoVermelho.append(" " + tempVermelho); // une o texto vermelho com mais de 100 caracteres
			}
			
			if(temp.length() > 100) {
				paragrafos.add(temp); //adiciona o texto com mais de 100 cacarteres em um array
			}
		}
		// se tiver mais de dois caracteres no adiciona o texto vermelho ao array
		if(textoVermelho.length() > 2) {
			paragrafos.add(0,textoVermelho.toString()); 
		}
		
		return paragrafos;
	}

}
