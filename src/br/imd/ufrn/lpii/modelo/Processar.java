package br.imd.ufrn.lpii.modelo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;

import br.imd.ufrn.lpii.excepitions.FileException;

public class Processar {
	private BancoDeDados bd;
	private ArrayList<String> listaDePalavras;
	
	private static final String CODIGO_ARQUIVO = ",hoax,link,timestamp";
	public Processar(BancoDeDados bd) {
		this.bd = bd;
		listaDePalavras = new ArrayList<String>();
	}
	
	public void processarArquivo(ArrayList<String> texto) throws FileException {
		
		if(!texto.get(0).equals(CODIGO_ARQUIVO)) {
			throw new FileException("Arquivo errado!");
		}
		
		for(int i = 1; i < texto.size();i++) {
			separar(texto.get(i));
		}
	}
	
	public String ProcessarConteudo(String mensagem) {
		
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
		
		
		return mensagemProcessada;
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
	
	private void separar(String texto) {
		String[] aux = texto.split(",");
		int tam = aux.length;
		String ajuda = "";
		
		//System.out.println("###################################");
		Registro r = new MensagemProcessada();
		
		r.setId(aux[0]);
		r.setLink(aux[tam-2]);
		r.setTimestamp(aux[tam-1]);
		
		for(int i = 1; i < tam -2;i++) {
			ajuda = ajuda.concat(aux[i] + " ");
		}
		
		r.setConteudo(ajuda);
		
		r.setMensagemProcessada(ProcessarConteudo(r.getConteudo()));
		//System.out.println(r.getMensagemProcessada());
		//System.out.println(c.criarHash(r.getMensagemProcessada()));
		bd.Adicionar(criarHash(r.getMensagemProcessada()), r);		
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
	
	private void ordenar() {
		Collections.sort(listaDePalavras);
	}
	
	private String toHexFormat(final byte[] bytes) {
		final StringBuilder sb = new StringBuilder();
		  for (byte b : bytes) {
		    sb.append(String.format("%02x", b));
		  }
		 
		  return sb.toString();
	}
}
