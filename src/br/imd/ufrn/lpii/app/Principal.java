package br.imd.ufrn.lpii.app;

import java.util.ArrayList;
import java.util.Scanner;

import br.imd.ufrn.lpii.dominio.BancoDeDados;
import br.imd.ufrn.lpii.dominio.Externos;
import br.imd.ufrn.lpii.dominio.Processar;
import br.imd.ufrn.lpii.dominio.Similaridade;
import br.imd.ufrn.lpii.temp.Arquivo;
import br.imd.ufrn.lpii.temp.Site;

public class Principal {
	
	private static Externos arquivo;
	private static BancoDeDados bd;
	private static Externos site;
	private static Similaridade s;
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
	 * 		-> TRATAMENTO DE EXCEÇOES
	 */
	
	public static void main(String[] args) {
		
		builder();
		
		Scanner entrada = new Scanner(System.in);
		String EntradaTexto;
		
		ArrayList<String> mem = arquivo.Abrir("/Users/macosx/eclipse-workspace/ProjetoFinal/boatos.csv");
		
		processar.processarArquivo(mem);
		
		System.out.println("Digite o link do site da fake news:");
		
		EntradaTexto = entrada.nextLine();
		ArrayList<String> textoSite = site.Abrir(EntradaTexto);
		
		boolean validador = false;
		
		for(int i = 0; i < textoSite.size(); i++) {
			
			System.out.print("Processando.....["+((i*100)/textoSite.size())+"%]\r");
			
			String hash = processar.ProcessarConteudo(textoSite.get(i));
			
			if(bd.buscaBancoDeDados(processar.criarHash(hash))) {
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
		
	}

	private static void builder() {
		arquivo = new Arquivo();
		bd = new BancoDeDados();
		processar = new Processar(bd);
		//pc = new ProcessarArquivo(bd);
		site = new Site();
		s = new Similaridade(bd);
		//p = new ProcessarConteudo();
	}
}
