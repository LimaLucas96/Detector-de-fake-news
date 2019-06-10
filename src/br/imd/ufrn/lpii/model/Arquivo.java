package br.imd.ufrn.lpii.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import br.imd.ufrn.lpii.model.abstratics.Externos;

public class Arquivo implements Externos {
	
	private File arquivo;
	
	@Override
	public ArrayList<String> abrir(String url) throws Exception {
		arquivo = new File(url); //inicia um objeto do tipo arquivo 
		
		ArrayList<String> texto = new ArrayList<String>();
	
		FileReader fr = new FileReader(arquivo); // para abrir o arquivo lendo os ccaracteres
			
		BufferedReader br = new BufferedReader(fr);  //ler o arquivo com base em caracteres
			
		while(br.ready()) {
			texto.add(br.readLine());  // adiciona a linha do arquivo no array
		}
		
		br.close(); // fecha o leitor
		fr.close();	//fecha o arquivo.
		
		return texto; // retorna o array com os dados do arquivo.
	}

}
