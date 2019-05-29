package br.imd.ufrn.lpii.dominio;

import java.util.ArrayList;

public class ProcessarArquivo {
	private BancoDeDados bd;
	private ProcessarConteudo c;
	
	public ProcessarArquivo(BancoDeDados bd) {
		this.bd = bd;
		c = new ProcessarConteudo();
	}
	
	public void Processar(ArrayList<String> texto) {
		
		for(int i = 1; i < texto.size();i++) {
			separar(texto.get(i));
		}
		
		//separar(texto.get(1));
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
		
		r.setMensagemProcessada(c.Processar(r.getConteudo()));
		//System.out.println(r.getMensagemProcessada());
		//System.out.println(c.criarHash(r.getMensagemProcessada()));
		bd.Adicionar(c.criarHash(r.getMensagemProcessada()), r);		
	}
}
