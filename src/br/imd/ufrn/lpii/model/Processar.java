package br.imd.ufrn.lpii.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;

import br.imd.ufrn.lpii.model.excepitions.FileException;

public class Processar {
	private BancoDeDados bd;
	private ArrayList<String> listaDePalavras;
	
	private static final String CODIGO_ARQUIVO = ",hoax,link,timestamp";
	public Processar(BancoDeDados bd) {
		this.bd = bd;
		listaDePalavras = new ArrayList<String>();
	}
	/**
	 * Metodo para processar o conteudo do arquivo dataset
	 * @param texto ArrayList contendo o conteudo do arquivo
	 * @throws FileException Se o codigo do arquivo nao for o correto
	 */
	public void processarArquivo(ArrayList<String> texto) throws FileException {
		
		if(!texto.get(0).equals(CODIGO_ARQUIVO)) {
			throw new FileException("Arquivo errado!");
		}
		
		for(int i = 1; i < texto.size();i++) {
		//	System.out.println(texto.get(i));
			separar(texto.get(i)); //separa a linha para criar registro
		}
	}
	/**
	 * Processa o conteudo da noticia
	 * @param mensagem string contendo a noticia
	 * @return retorna a noticia processada, ja em ordem alfabetica
	 */
	public String ProcessarConteudo(String mensagem) {
		
		listaDePalavras.clear(); //limpa o arraylist
		
		String[] ArrayString = mensagem.split(" "); // separa a noticia em um vetor de string
		
		for (int i = 0; i < ArrayString.length; i++) {
			listaDePalavras.add(ArrayString[i].replaceAll("[\".,-/!)(*]", "")); //remove todos os caracteres especiais
		}
		
		removerPalavras();
		lowerCaseConteudo();
		removerDuplicadas();
		ordenar();
		
		String mensagemProcessada = "";
		
		// une as palavras do array em uma string.
		for (int i = 0; i < listaDePalavras.size()-1; i++) {
			mensagemProcessada = mensagemProcessada.concat(" "+listaDePalavras.get(i)); 
		}
		
		
		return mensagemProcessada;
	}
	/**
	 * Gera um hash de SHA-1
	 * @param mensagem mensagem a qual vai ser gerado o hash
	 * @return string com o o hash de 40 caracteres
	 */
	public String criarHash(String mensagem) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1"); //criador de hash padrao do java
			md.update(mensagem.getBytes()); // recebe os Bytes da string
			return toHexFormat(md.digest()); //Retorna em Bytes o hash
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.getMessage();
			return null;
		}
	}
	/**
	 * Separa uma linha para em: id; conteudo; link; e time; alem disto, ele cria um registro e armazena na classe BancoDeDados
	 * @param texto string da linha do aquivo
	 */
	private void separar(String texto) {
		String[] aux = texto.split(","); // separa toda a linha por virgulas
		int tam = aux.length;
		String ajuda = "";
		
		//System.out.println("###################################");
		Registro r = new MensagemProcessada(); // cria um objeto do tipo Registo
		
		r.setId(aux[0]);
		r.setLink(aux[tam-2]);
		r.setTimestamp(aux[tam-1]);
		
		//cocatena todo o conteudo.
		for(int i = 1; i < tam -2;i++) {
			ajuda = ajuda.concat(aux[i] + " "); 
		}
		
		r.setConteudo(ajuda); // armazena o conteudo original
		
		r.setMensagemProcessada(ProcessarConteudo(r.getConteudo())); // aramazena o conteudo processado
		
		bd.Adicionar(criarHash(r.getMensagemProcessada()), r);	// aramazena no BancoDeDados o registro da noticia.	
	}
	/**
	 * Remove as palavras com menos de 3 caracteres.
	 */
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
	/**
	 * Deixa todas as letras em minusculo (lower Case)
	 */
	private void lowerCaseConteudo() {
		for (int i = 0; i < listaDePalavras.size(); i++) {
			String nfdNormalizedString = Normalizer.normalize(listaDePalavras.get(i), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			
			listaDePalavras.set(i, nfdNormalizedString.toLowerCase());
		}
	}
	/**
	 * Remove as palavras repetidas
	 */
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
	 /**
	  * Ordena as palavras em ordem alfabetica
	  */
	private void ordenar() { Collections.sort(listaDePalavras); }
	
	/**
	 * Transforma um vetor de bytes em uma string Hexadecimal
	 * @param bytes vetor de bytes
	 * @return String hexadecimal
	 */
	private String toHexFormat(final byte[] bytes) {
		final StringBuilder sb = new StringBuilder();
		  for (byte b : bytes) {
		    sb.append(String.format("%02x", b)); //cacatena o Byte em formato hexadecimal
		  }
		 
		  return sb.toString();
	}
}
