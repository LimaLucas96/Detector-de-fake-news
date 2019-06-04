package br.imd.ufrn.lpii.modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import br.imd.ufrn.lpii.modelo.abstratics.Externos;

public class Arquivo implements Externos {
	
	private File arquivo;
	
	@Override
	public ArrayList<String> Abrir(String url) throws Exception {
		arquivo = new File(url);
		
		ArrayList<String> texto = new ArrayList<String>();
	
		FileReader fr = new FileReader(arquivo);
			
		BufferedReader br = new BufferedReader(fr);
			
		while(br.ready()) {
			texto.add(br.readLine());
		}
		
		br.close();
		fr.close();	
		
		return texto;
	}

}
