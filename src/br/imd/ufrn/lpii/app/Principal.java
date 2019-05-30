package br.imd.ufrn.lpii.app;

import java.util.ArrayList;
import java.util.Scanner;

import br.imd.ufrn.lpii.dominio.Arquivo;
import br.imd.ufrn.lpii.dominio.BancoDeDados;
import br.imd.ufrn.lpii.dominio.ProcessarArquivo;
import br.imd.ufrn.lpii.dominio.ProcessarConteudo;
import br.imd.ufrn.lpii.dominio.Similaridade;
import br.imd.ufrn.lpii.dominio.Site;

public class Principal {
	
	/*
	 * #####################################################################################
	 * 								IMPORTANTE
	 * #####################################################################################
	 * FALTA:
	 * 		-> TERMINAR AS FUNCOES DE SIMILARIDADE
	 * 		-> FUNCOES DOS BUTOES DA INTERFACE GRAFIACA
	 * 		-> POLIMORFISMO
	 * 		-> INTERFACES
	 * 		-> TRATAMENTO DE EXCEÇOES
	 */
	
	public static void main(String[] args) {
		
		Arquivo ar = new Arquivo();
		BancoDeDados bd = new BancoDeDados();
		ProcessarArquivo pc = new ProcessarArquivo(bd);
		Site site = new Site();
		Similaridade s = new Similaridade(bd);
		ProcessarConteudo p = new ProcessarConteudo();
		
		Scanner entrada = new Scanner(System.in);
		String EntradaTexto;
		
		ArrayList<String> mem = ar.Abrir("/Users/macosx/eclipse-workspace/ProjetoFinal/boatos.csv");
		
		pc.Processar(mem);
		
		System.out.println("Digite o link do site da fake news:");
		EntradaTexto = entrada.nextLine();
		String[] textoSite = site.processar(EntradaTexto);
		
		
		boolean validador = false;
		
		for(int i = 0; i < textoSite.length; i++) {
			
			System.out.print("Processando.....["+((i*100)/textoSite.length)+"%]\r");
			
			String hash = p.Processar(textoSite[i]);
			
			if(bd.buscaBancoDeDados(p.criarHash(hash))) {
				validador = true;
				System.out.print("Processando.....[100%]\r");
				
				break;
			}else if(s.verificarSimilaridade(hash)) {
				validador = true;	
				System.out.print("Processando.....[100%]\r");
				
				break;
			}
		}
		
		if(validador) {
			System.out.println("É uma fake News");
		}else {
			System.out.println("Nao é uma fake News");
		}
		
		
		
//		String hash = p.Processar(EntradaTexto);
//		//hash = p.criarHash(hash);
//		System.out.println("##### Com Texto Processado ########");
//		//System.out.println("similaridade " + s.algoritmoDeLevensthein("Aaeeee", "eeeeee"));
//		//boolean resposta = s.verificarSimilaridade(hash);
//		
////		if(resposta) {
////			System.out.println("essa resposta");
////		}else {
////			System.out.println("aquela");
////		}
//		
//		
//		if(s.verificarSimilaridade(hash)) {
//			System.out.println("É uma fakeNews");
//		}else {
//			System.out.println("Nao é uma fakeNews");
//		}
//		
//		System.out.println("##### Com Hash ########");
//		hash = p.criarHash(hash);
//		
//		if(s.verificarSimilaridade(hash)) {
//			System.out.println("É uma fakeNews");
//		}else {
//			System.out.println("Nao é uma fakeNews");
//		}
		
//		for (int i = 0; i < mem.size(); i++) {
//			System.out.println(mem.get(i));
//		}
//		//Usar jsoup para rastrear os arquivos
//		ProcessarConteudo p = new ProcessarConteudo();
//		
//		String mensagem = p.Processar(r.getConteudo());
//		
//		System.out.println(p.criarHash(mensagem));
	}

}
