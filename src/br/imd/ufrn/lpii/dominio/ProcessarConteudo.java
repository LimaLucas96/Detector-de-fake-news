package br.imd.ufrn.lpii.dominio;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;

public class ProcessarConteudo {
	private ArrayList<String> listaDePalavras;
	
	public ProcessarConteudo() {
		listaDePalavras = new ArrayList<String>();
	}
	public String Processar(String mensagem) {
		
		listaDePalavras.clear();
		
		String[] ArrayString = mensagem.split(" ");
		
		for (int i = 0; i < ArrayString.length; i++) {
			listaDePalavras.add(ArrayString[i].replaceAll("[\".,-/!)(*]", ""));
		}
		
		
		removerPalavras();
		lowerCaseConteudo();
		removerDuplicadas();
		ordenar();
		
		String mensagemProcessada = "";
		
		for (int i = 0; i < listaDePalavras.size()-1; i++) {
			mensagemProcessada = mensagemProcessada.concat(" "+listaDePalavras.get(i));
		}
		
		//System.out.println(mensagemProcessada);
		
		return mensagemProcessada;
	}
	
	private void removerPalavras() {
		int i = 0;
		
		while(i<listaDePalavras.size()) {
			if(listaDePalavras.get(i).length() <= 3) {
				listaDePalavras.remove(i);
			}else {
				i++;
			}
		}
	}
	
	private void lowerCaseConteudo() {
		for (int i = 0; i < listaDePalavras.size(); i++) {
			String nfdNormalizedString = Normalizer.normalize(listaDePalavras.get(i), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			
			listaDePalavras.set(i, nfdNormalizedString.toLowerCase());
		}
	}
	
	private void removerDuplicadas() {
		int i = 0;
		while(i<listaDePalavras.size()) {
			
			int j = 0;
			while(j<i) {
				if(listaDePalavras.get(i).equals(listaDePalavras.get(j))) {
					listaDePalavras.remove(j);
					i--;
				}else {
					j++;
				}
			}
			i++;
		}
	}
	
	private String ordenar() {
		Collections.sort(listaDePalavras);
		return null;
	}
	
	public String criarHash(String mensagem) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(mensagem.getBytes());
			return toHexFormat(md.digest());
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private String toHexFormat(final byte[] bytes) {
		final StringBuilder sb = new StringBuilder();
		  for (byte b : bytes) {
		    sb.append(String.format("%02x", b));
		  }
		 
		  return sb.toString();
	}
}
