package br.imd.ufrn.lpii.modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Arquivo implements Externos {
	
	private File arquivo;
	
	@Override
	public ArrayList<String> Abrir(String url) {
		arquivo = new File(url);
		
		ArrayList<String> texto = new ArrayList<String>();
		
		try {
			FileReader fr = new FileReader(arquivo);
			
			BufferedReader br = new BufferedReader(fr);
			
			while(br.ready()) {
				texto.add(br.readLine());
			}
			
			br.close();
			fr.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return texto;
	}

}
