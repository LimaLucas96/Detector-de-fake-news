package br.imd.ufrn.lpii.app;

import java.util.ArrayList;
import java.util.Scanner;

import br.imd.ufrn.lpii.modelo.Arquivo;
import br.imd.ufrn.lpii.modelo.BancoDeDados;
import br.imd.ufrn.lpii.modelo.Cosine;
import br.imd.ufrn.lpii.modelo.JaroWinkler;
import br.imd.ufrn.lpii.modelo.Levensthein;
import br.imd.ufrn.lpii.modelo.Processar;
import br.imd.ufrn.lpii.modelo.Site;
import br.imd.ufrn.lpii.modelo.abstratics.Externos;
import br.imd.ufrn.lpii.modelo.abstratics.Similaridade;

public class Principal {
	
	private static Externos arquivo;
	private static BancoDeDados bd;
	private static Externos site;
	private static Similaridade similaridade;
	private static Processar processar;
	/*
	 * #####################################################################################
	 * 								IMPORTANTE
	 * #####################################################################################
	 * FALTA:
	 * 		-> TERMINAR AS FUNCOES DE SIMILARIDADE
	 * 		-> FUNCOES DOS BUTOES DA INTERFACE GRAFIACA
	 * 		-> POLIMORFISMO
	 * 		-> INTERFACES
	 */
	
	public static void main(String[] args) throws Exception {
		
		builder();
		
		Scanner entrada = new Scanner(System.in);
		String EntradaTexto;
		
		ArrayList<String> mem = arquivo.Abrir("/Users/macosx/eclipse-workspace/ProjetoFinal/boatos.csv");
		
		processar.processarArquivo(mem);
		//bd.print();
		//System.out.println(similaridade.distancia("ABC", "ABCE"));
		System.out.println("Digite o link do site da fake news:");
		
		EntradaTexto = entrada.nextLine();
		ArrayList<String> textoSite = site.Abrir(EntradaTexto);
		
		boolean validador = false;
		
		System.out.println("\n############ Levens #############\n");
		for(int i = 0; i < textoSite.size(); i++) {
			
			System.out.print("Processando.....["+((i*100)/textoSite.size())+"%]\r");
			
			String hash = processar.ProcessarConteudo(textoSite.get(i));
			
			if(bd.buscaBancoDeDados(processar.criarHash(hash))) {
				validador = true;
				System.out.print("Processando.....[100%]\r");
				
				break;
			}else if(similaridade.verificarSimilaridade(hash)) {
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
		
		//------------------------------------------------------
		
		System.out.println("\n############ Cosine #############\n");
		similaridade = new Cosine(bd);
		for(int i = 0; i < textoSite.size(); i++) {
			
			System.out.print("Processando.....["+((i*100)/textoSite.size())+"%]\r");
			
			String hash = processar.ProcessarConteudo(textoSite.get(i));
			
			if(bd.buscaBancoDeDados(processar.criarHash(hash))) {
				validador = true;
				System.out.print("Processando.....[100%]\r");
				
				break;
			}else if(similaridade.verificarSimilaridade(hash)) {
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
		
		//------------------------------------------------------
		
				System.out.println("\n############ Jaro-Winkler #############\n");
				similaridade = new JaroWinkler(bd);
				for(int i = 0; i < textoSite.size(); i++) {
					
					System.out.print("Processando.....["+((i*100)/textoSite.size())+"%]\r");
					
					String hash = processar.ProcessarConteudo(textoSite.get(i));
					
					if(bd.buscaBancoDeDados(processar.criarHash(hash))) {
						validador = true;
						System.out.print("Processando.....[100%]\r");
						
						break;
					}else if(similaridade.verificarSimilaridade(hash)) {
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
	}

	private static void builder() {
		arquivo = new Arquivo();
		bd = new BancoDeDados();
		processar = new Processar(bd);
		site = new Site();
		similaridade = new Levensthein(bd);
	}
}
